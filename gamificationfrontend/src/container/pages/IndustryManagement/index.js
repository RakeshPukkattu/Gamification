import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Form, Modal, Drawer, Collapse, Radio, Select, Alert } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory, useLocation } from 'react-router-dom';
import axios from 'axios';
import { setItem, getItem } from '../../../utility/localStorageControl';

const Dashboard = () => {
  const location = useLocation();
  const { Panel } = Collapse;
  const { Column } = Table;
  const history = useHistory();
  const [industryData, setIndustryData] = useState([]);
  const [reference, setReference] = useState([]);
  const [showModal2, setShowModal2] = useState(false);
  const [showModal3, setShowModal3] = useState(false);
  const [id, setId] = useState('');
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(true);
  const [form] = Form.useForm();
  const [name, setName] = useState('');
  const [assign, setAssign] = useState('');
  const [visible, setVisible] = useState(false);
  const [mcq, setMcq] = useState('');
  const [mmcq, setMmcq] = useState(null);
  const [allUsers, setAllUsers] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
  const [themes, setThemes] = useState([]);
  const [games, setGames] = useState([]);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [assignedGroups, setAssignedGroups] = useState([]);
  const [alertType, setAlertType] = useState('success');

  useEffect(() => {
    if (location.state !== null && location.state !== undefined) {
      setShowAlert(true);
      setAlertText(location.state.detail);
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, []);

  useEffect(() => {
    getAllIndustries();
  }, []);

  const getAllIndustries = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}industryManagement/industries`;
    var config = {
      method: 'get',
      url: URL,
      headers: {
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${user.accessToken}`,
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        const rev = result.industries.reverse();
        console.log(rev);
        let industryTableData = [];
        rev.map(industryData => {
          const { industryID, industryName, industryStatus, games, themes } = industryData;
          return industryTableData.push({
            key: industryID,
            name: industryName,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      // onChange={e => {
                      //   if (isAssigned) {
                      //     setShowAlert(true);
                      //     setAlertText(`${industryName} has been Assigned`);
                      //     setAlertType('error');
                      //     setTimeout(() => {
                      //       setShowAlert(false);
                      //       setAlertType('success');
                      //     }, 3000);
                      //   } else {
                      //     updateStatus(e, industryID, industryName);
                      //   }
                      // }}
                      onChange={e => {
                        updateStatus(e, industryID, industryName);
                      }}
                      checked={industryStatus}
                      style={{ marginLeft: '30px' }}
                      //disabled={isAssigned}
                    />
                  </p>
                </div>
              </>
            ),
            modify: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    setId(industryID);
                    editIndustry(industryName);
                  }}
                  //disabled={enableDisableStatus === true ? (isAssigned === false ? false : true) : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            assign: (
              <>
                {games.length === 0 ? (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => assignGame(industryID)}>
                    <FeatherIcon icon="plus-square" size={16} />
                  </Button>
                ) : (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => editAssignGame(industryID)}>
                    <FeatherIcon icon="edit" size={16} />
                  </Button>
                )}
              </>
            ),
            assign2: (
              <>
                {themes.length === 0 ? (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => assignTheme(industryID)}>
                    <FeatherIcon icon="plus-square" size={16} />
                  </Button>
                ) : (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => editAssignTheme(industryID)}>
                    <FeatherIcon icon="edit" size={16} />
                  </Button>
                )}
              </>
            ),
            viewAssign: (
              <Button
                className="btn-icon"
                to="#"
                shape="circle"
                onClick={e => showDrawer(industryData)}
                // onClick={() => {
                //   if (games.length === 0) {
                //     setShowAlert(true);
                //     setAlertText(`${industryName} has been Not Assigned any games`);
                //     setAlertType('error');
                //     setTimeout(() => {
                //       setShowAlert(false);
                //       setAlertType('success');
                //     }, 3000);
                //   } else {
                //     showDrawer(industryData);
                //   }
                // }}
                //disabled={!isAssigned}
              >
                <FeatherIcon icon="eye" size={16} />
              </Button>
            ),
          });
        });
        setIndustryData(industryTableData);
        setReference(industryTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}industryManagement/modifyIndustry/${id}`;
    const userValues = {
      industryStatus: e,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
    var config = {
      method: 'put',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user.accessToken}`,
      },
      data: data,
    };

    axios(config)
      .then(function(response) {
        getAllIndustries();
        setShowAlert(true);
        setAlertType('success');
        setAlertText(`${name} status successfully Updated`);
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleSubmit = values => {
    const found = industryData.some(row => row.name.toLowerCase() === values.industryName.toLowerCase());
    if (found) {
      setError('Industry Name already exists');
      setFlag(true);
    } else {
      setLoading(true);
      const user = getItem('user');
      const api = process.env.REACT_APP_BACKEND_API;
      const industryValues = {
        name: values.industryName,
      };
      var data = new FormData();
      data.append('userJson', JSON.stringify(industryValues));
      const URL = `${api}industryManagement/addIndustry`;
      var config = {
        method: 'post',
        url: URL,
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${user.accessToken}`,
        },
        data: data,
      };
      axios(config)
        .then(function(response) {
          setLoading(false);
          const result = response.data;
          console.log(result);
          handleCancel2();
          setShowAlert(true);
          setAlertText(`${values.industryName} successfully Added!`);
          setAlertType('success');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
          getAllIndustries();
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  };

  const handleSubmit2 = values => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}industryManagement/modifyIndustry/${id}`;
    const userValues = {
      industryName: values.industryName,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
    var config = {
      method: 'put',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user.accessToken}`,
      },
      data: data,
    };

    axios(config)
      .then(function(response) {
        handleCancel2();
        getAllIndustries();
        setShowAlert(true);
        setAlertType('success');
        setAlertText(`${values.industryName} successfully Updated`);
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
      })
      .catch(function(error) {
        console.log(error);
      });
    // const found = industryData.some(row => row.name.toLowerCase() === values.industryName.toLowerCase());
    // if (found) {
    //   setError('Industry Name already exists');
    //   setFlag(true);
    // } else {
    //   setLoading(true);
    //   console.log(values);
    //   // const user = getItem('user');
    //   // const api = process.env.REACT_APP_BACKEND_API;
    //   // const industryValues = {
    //   //   name: values.industryName,
    //   // };
    //   // var data = new FormData();
    //   // data.append('userJson', JSON.stringify(industryValues));
    //   // const URL = `${api}industryManagement/addIndustry`;
    //   // var config = {
    //   //   method: 'post',
    //   //   url: URL,
    //   //   headers: {
    //   //     'Content-Type': 'multipart/form-data',
    //   //     Authorization: `Bearer ${user.accessToken}`,
    //   //   },
    //   //   data: data,
    //   // };
    //   // axios(config)
    //   //   .then(function(response) {
    //   //     setLoading(false);
    //   //     const result = response.data;
    //   //     console.log(result);
    //   //     handleCancel2();
    //   //     setShowAlert(true);
    //   //     setAlertText(`${values.industryName} with same name already exists!`);
    //   //     setAlertType('success');
    //   //     setTimeout(() => {
    //   //       setShowAlert(false);
    //   //     }, 3000);
    //   //     getAllIndustries();
    //   //   })
    //   //   .catch(function(error) {
    //   //     console.log(error);
    //   //   });
    // }
  };
  const onClose = () => {
    setVisible(false);
  };
  const showDrawer = industry => {
    console.log(industry);
    setGames(industry.games);
    setThemes(industry.themes);
    setVisible(true);
  };

  const industryTableColumns = [
    {
      title: 'Industry Name',
      dataIndex: 'name',
      key: 'name',
      width: 150,
    },
    {
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
      align: 'center',
    },
    {
      title: 'Modify',
      dataIndex: 'modify',
      key: 'modify',
      width: 150,
      align: 'center',
    },
    {
      title: 'Assign/Modify Games',
      dataIndex: 'assign',
      key: 'assign',
      align: 'center',
      width: 200,
    },
    {
      title: 'Assign/Modify Theme',
      dataIndex: 'assign2',
      key: 'assign2',
      align: 'center',
      width: 200,
    },
    {
      title: 'Details',
      dataIndex: 'viewAssign',
      key: 'viewAssign',
      align: 'center',
      width: 70,
    },
  ];

  const filterFunction = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setIndustryData(filteredData);
  };

  const handleCancel2 = () => {
    setShowModal2(false);
    setShowModal3(false);
  };

  const specialCharacter = event => {
    setFlag(false);
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const addIndustry = name => {
    form.setFieldsValue({
      industryName: name === 'null' ? '' : name,
    });
    setShowModal2(true);
  };
  const editIndustry = name => {
    form.setFieldsValue({
      industryName: name === 'null' ? '' : name,
    });
    setShowModal3(true);
  };
  const assignGame = id => {
    history.push(`/admin/assigngamescomponent/${id}`);
  };

  const editAssignGame = id => {
    history.push(`/admin/editassigngamescomponent/${id}`);
  };
  const assignTheme = id => {
    history.push(`/admin/assignthemescomponent/${id}`);
  };

  const editAssignTheme = id => {
    history.push(`/admin/editassignthemescomponent/${id}`);
  };

  useEffect(() => {
    if (name === '') {
      setLoading(true);
    } else {
      setLoading(false);
    }
  }, [name]);

  return (
    <>
      <PageHeader title="Industry Management Dashboard" />
      <Main>
        <Row gutter={25}>
          <Col sm={24} xs={24}>
            <ExportStyleWrap>
              <Cards headless>
                <div
                  style={{
                    marginBottom: '10px',
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    position: 'absolute',
                    marginLeft: '20px',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type={alertType} showIcon /> : ''}
                </div>
                <div className="sDash_export-box" style={{ marginBottom: '20px' }}>
                  <div>
                    <Button className="btn-export" type="primary" size="medium" onClick={() => addIndustry('null')}>
                      Add Industry
                    </Button>
                  </div>
                  <div>
                    <Input
                      placeholder="Search Industry Name"
                      style={{ width: '100%' }}
                      id="myInput"
                      onKeyUp={filterFunction}
                    />
                  </div>
                </div>
                <UserTableStyleWrapper>
                  <TableWrapper className="table-responsive">
                    <Table
                      id="myTable"
                      //style={{ height: '400px' }}
                      columns={industryTableColumns}
                      scroll={{ y: 340, x: 600 }}
                      dataSource={industryData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: industryData.length,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`,
                      }}
                    />
                  </TableWrapper>
                </UserTableStyleWrapper>
              </Cards>
            </ExportStyleWrap>
          </Col>
        </Row>
      </Main>
      <Modal
        title="Add Industry"
        wrapClassName="sDash_export-wrap"
        visible={showModal2}
        footer={null}
        onCancel={handleCancel2}
      >
        <Form name="AddIndustry" form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item
            name="industryName"
            label="Industry Name"
            rules={[{ message: 'Please enter industry name', required: true }]}
          >
            <Input
              placeholder="Industry Name"
              onKeyPress={specialCharacter}
              maxlength="40"
              onChange={e => setName(e.target.value)}
            />
          </Form.Item>
          <div className="sDash-button-grp">
            <Button
              className="btn-signin"
              htmlType="submit"
              type={loading ? '' : 'primary'}
              size="large"
              disabled={loading}
            >
              Create
            </Button>
          </div>
          <div>
            <p className="danger text-center" style={{ color: 'red' }}>
              {flag ? error : ''}
            </p>
          </div>
        </Form>
      </Modal>
      <Drawer width={440} title="Assigned Games" placement="right" closable={false} onClose={onClose} visible={visible}>
        {games.length > 0 ? (
          <UserTableStyleWrapper>
            <TableWrapper className="table-responsive">
              <Table id="myTable" scroll={{ y: 240 }} dataSource={games} pagination={false}>
                <Column
                  title="Games"
                  dataIndex="gameName"
                  key="gameName"
                  render={(text, record) => (
                    <>
                      <div className="user-info">
                        <figure>
                          <img
                            style={{ width: '40px', cursor: 'pointer' }}
                            src={
                              record.thumbNailKey === null
                                ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                : record.thumbNailKey
                            }
                            alt="themeImage"
                          />
                        </figure>
                        <figcaption>
                          <Heading className="user-name" as="h6">
                            {record.gameName}
                          </Heading>
                        </figcaption>
                      </div>
                    </>
                  )}
                />
              </Table>
            </TableWrapper>
          </UserTableStyleWrapper>
        ) : (
          ''
        )}
        <p style={{ marginTop: '20px' }}>Assigned Themes</p>
        {themes.length > 0 ? (
          <UserTableStyleWrapper>
            <TableWrapper className="table-responsive">
              <Table id="myTable" scroll={{ y: 240 }} dataSource={themes} pagination={false}>
                <Column
                  title="Themes"
                  dataIndex="themeName"
                  key="themeName"
                  render={(text, record) => (
                    <>
                      <div className="user-info">
                        <figure>
                          <img
                            style={{ width: '40px', cursor: 'pointer' }}
                            src={
                              record.thumbNailKey === null
                                ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                : record.thumbNailKey
                            }
                            alt="themeImage"
                          />
                        </figure>
                        <figcaption>
                          <Heading className="user-name" as="h6">
                            {record.themeName}
                          </Heading>
                        </figcaption>
                      </div>
                    </>
                  )}
                />
              </Table>
            </TableWrapper>
          </UserTableStyleWrapper>
        ) : (
          ''
        )}
      </Drawer>
      <Modal
        title="Edit Industry"
        wrapClassName="sDash_export-wrap"
        visible={showModal3}
        footer={null}
        onCancel={handleCancel2}
      >
        <Form name="EditIndustry" form={form} onFinish={handleSubmit2} layout="vertical">
          <Form.Item
            name="industryName"
            label="Industry Name"
            rules={[{ message: 'Please enter industry name', required: true }]}
          >
            <Input
              placeholder="Industry Name"
              onKeyPress={specialCharacter}
              maxlength="40"
              onChange={e => setName(e.target.value)}
            />
          </Form.Item>
          <div className="sDash-button-grp">
            <Button
              className="btn-signin"
              htmlType="submit"
              type={loading ? '' : 'warning'}
              size="large"
              disabled={loading}
            >
              Update
            </Button>
          </div>
          <div>
            <p className="danger text-center" style={{ color: 'red' }}>
              {flag ? error : ''}
            </p>
          </div>
        </Form>
      </Modal>
    </>
  );
};

export default Dashboard;

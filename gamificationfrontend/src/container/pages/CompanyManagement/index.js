import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Select, Drawer, Alert, Spin, Collapse, Modal, Popconfirm } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory, useLocation } from 'react-router-dom';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';

const Dashboard = () => {
  const { Column } = Table;
  const { Panel } = Collapse;
  const location = useLocation();
  const history = useHistory();
  const [selectFilter, setSelectFilter] = useState('');
  const [show, setShow] = useState(false);
  const [showdelete, setShowDelete] = useState(true);
  const [companysData, setCompanysData] = useState([]);
  const [reference, setReference] = useState([]);
  const [singleCompany, setSingleCompany] = useState(null);
  const [allUsers, setAllUsers] = useState([]);
  const [userReference, setUserReference] = useState([]);
  const [visible, setVisible] = useState(false);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('success');
  const [loading2, setLoading2] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [update, setUpdate] = useState('');

  useEffect(() => {
    console.log(location);
    if (location.state !== null && location.state !== undefined) {
      setShowAlert(true);
      setAlertText(location.state.detail);
      setAlertType('success');
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, []);

  const handleOk = () => {
    updateStatus(update.status, update.id, update.name);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const showDrawer = company => {
    setSingleCompany(company);
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
  });
  const companysTableData = [];

  useEffect(() => {
    getAllCompanys();
  }, []);

  const getAllCompanys = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}companyManagement/allCompanies`;
    var config = {
      method: 'get',
      url: URL,
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        const companys = result.company.reverse();
        console.log(companys);
        let companysTableData = [];
        companys.map(company => {
          const { id, companyName, startDate, status, assignedComponents, duration } = company;
          return companysTableData.push({
            key: id,
            name: companyName,
            startDate: startDate,
            duration: duration,
            assign: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    if (!(assignedComponents === null)) {
                      setShowAlert(true);
                      setAlertText(`Components had been Assigned Already`);
                      setAlertType('error');
                      setTimeout(() => {
                        setShowAlert(false);
                        setAlertType('success');
                      }, 3000);
                    } else {
                      history.push(`/admin/assigncomponent/${id}`);
                    }
                  }}
                  //disabled={!(assignedComponents.theme.length === 0)}
                >
                  <FeatherIcon icon="plus-square" size={16} />
                </Button>
              </>
            ),
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => {
                        setIsModalVisible(true);
                        setUpdate({ status: e, id: id, name: companyName });
                      }}
                      checked={status}
                      style={{ marginLeft: '30px' }}
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
                    if (!status) {
                      setShowAlert(true);
                      setAlertText(`Company Status is Disabled`);
                      setAlertType('error');
                      setTimeout(() => {
                        setShowAlert(false);
                        setAlertType('success');
                      }, 3000);
                    } else {
                      history.push(`/admin/editcompany/${id}`);
                    }
                  }}
                  //disabled={!status}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            details: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    if (assignedComponents === null) {
                      setShowAlert(true);
                      setAlertText(`Assign Component for the company`);
                      setAlertType('error');
                      setTimeout(() => {
                        setShowAlert(false);
                        setAlertType('success');
                      }, 3000);
                    } else {
                      showDrawer(company);
                    }
                  }}
                  //disabled={assignedComponents.theme.length === 0}
                >
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        });
        setCompanysData(companysTableData);
        setReference(companysTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };
  const updateStatus2 = (e, id, name) => {
    confirm('Press a button!');
  };
  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}companyManagement/updateStatus/${id}`;
    const userValues = {
      status: e,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
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
        setShowAlert(true);
        setAlertText(`${name} status successfully Updated`);
        setAlertType('success');
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
        getAllCompanys();
        setIsModalVisible(false);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const usersTableData = [];

  const companysTableColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => {
        return a.name.toString().localeCompare(b.name.toString());
      },
    },
    {
      title: 'Start Date',
      dataIndex: 'startDate',
      key: 'startDate',
      align: 'center',
    },
    {
      title: 'Duration',
      dataIndex: 'duration',
      key: 'duration',
      align: 'center',
      width: 150,
    },
    {
      title: 'Assign Components',
      dataIndex: 'assign',
      key: 'assign',
      align: 'center',
    },
    {
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      align: 'center',
    },
    {
      title: 'Modify',
      dataIndex: 'modify',
      key: 'modify',
      align: 'center',
    },
    {
      title: 'Details',
      dataIndex: 'details',
      key: 'details',
      align: 'center',
    },
  ];

  const selectedFilter = e => {
    setShow(true);
    setSelectFilter(e);
  };

  const filterFunction = e => {
    var selectedFilter = selectFilter;
    var x = 0;
    if (x === 0) {
      const currValue = e.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
      setCompanysData(filteredData);
    }
  };

  const addCompany = () => {
    history.push('/admin/addcompany');
  };

  return (
    <>
      <PageHeader title="Company Management Dashboard" />
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
                    marginLeft: '-40px',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type={alertType} showIcon /> : ''}
                </div>
                <div className="sDash_export-box" style={{ marginBottom: '20px' }}>
                  <div>
                    <Button className="btn-export" type="primary" size="medium" onClick={addCompany}>
                      Create Company
                    </Button>
                  </div>
                  <div>
                    <Input placeholder="Search Here" id="myInput" onKeyUp={filterFunction} />
                  </div>
                </div>

                <UserTableStyleWrapper>
                  <TableWrapper className="table-responsive">
                    <Table
                      id="myTable"
                      columns={companysTableColumns}
                      scroll={{ y: 340, x: true }}
                      dataSource={companysData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: companysData.length,
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
      {singleCompany === null ? (
        ''
      ) : (
        <Drawer
          width={440}
          title={singleCompany.companyName}
          placement="right"
          closable={false}
          onClose={onClose}
          visible={visible}
        >
          <Button
            className="btn-export"
            type="warning"
            size="medium"
            onClick={() => {
              history.push(`/admin/editassigncomponent/${singleCompany.id}`);
            }}
            style={{ marginBottom: '20px' }}
          >
            Modify
          </Button>
          <p>Components and Games Assigned </p>
          <Collapse accordion style={{ marginTop: '10px' }}>
            <Panel header="Theme">
              <UserTableStyleWrapper style={{ marginTop: '20px' }}>
                <TableWrapper className="table-responsive">
                  <Table
                    id="myTable"
                    scroll={{ y: 240 }}
                    dataSource={singleCompany.assignedComponents.theme}
                    pagination={false}
                  >
                    <Column
                      title="Theme"
                      dataIndex="name"
                      key="name"
                      render={(text, record) => (
                        <>
                          <div className="user-info">
                            <figure>
                              <img
                                style={{ width: '40px', cursor: 'pointer' }}
                                src={
                                  record.thumbNailKey === null
                                    ? 'https://media.contentapi.ea.com/content/dam/gin/images/2021/06/battlefield-2042-key-art.jpg.adapt.crop1x1.767p.jpg'
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
            </Panel>
          </Collapse>
          <Collapse accordion style={{ marginTop: '10px' }}>
            <Panel header="Components">
              <UserTableStyleWrapper>
                <TableWrapper className="table-responsive">
                  <Table
                    id="myTable"
                    scroll={{ y: 240 }}
                    dataSource={singleCompany.assignedComponents.components}
                    pagination={false}
                  >
                    <Column title="Component Name" dataIndex="title" key="title" />
                    {/* <Column
                      title="Component Status"
                      dataIndex="status"
                      key="status"
                      render={(text, record) => (
                        <>
                          <div className="notification-list-single">
                            <p>
                              <Popconfirm
                                title="Are you sure to update the status of component"
                                onConfirm={updateStatus2}
                                onVisibleChange={() => console.log('visible change')}
                              >
                                <Switch
                                  //onChange={e => updateStatus(e)}
                                  checked={true}
                                  style={{ marginLeft: '30px' }}
                                />
                              </Popconfirm>
                            </p>
                          </div>
                        </>
                      )}
                    /> */}
                  </Table>
                </TableWrapper>
              </UserTableStyleWrapper>
            </Panel>
          </Collapse>
          <Collapse accordion style={{ marginTop: '10px' }}>
            <Panel header="Games">
              <UserTableStyleWrapper style={{ marginTop: '20px' }}>
                <TableWrapper className="table-responsive">
                  <Table
                    id="myTable"
                    scroll={{ y: 240 }}
                    dataSource={singleCompany.assignedComponents.games}
                    pagination={false}
                  >
                    <Column
                      title="Game Name"
                      dataIndex="name"
                      key="name"
                      render={(text, record) => (
                        <>
                          <div className="user-info">
                            <figure>
                              <img
                                style={{ width: '40px', cursor: 'pointer' }}
                                src={
                                  record.thumbNailKey === null
                                    ? 'https://media.contentapi.ea.com/content/dam/gin/images/2021/06/battlefield-2042-key-art.jpg.adapt.crop1x1.767p.jpg'
                                    : record.thumbNailKey
                                }
                                alt="gameImage"
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
                    {/* <Column
                      title="Game Status"
                      dataIndex="status"
                      key="status"
                      render={(text, record) => (
                        <>
                          <div className="notification-list-single">
                            <p>
                              <Popconfirm
                                title="Are you sure to update the status of game"
                                onConfirm={updateStatus2}
                                onVisibleChange={() => console.log('visible change')}
                              >
                                <Switch
                                  //onChange={e => updateStatus2(e)}
                                  checked={record.status}
                                  style={{ marginLeft: '30px' }}
                                />
                              </Popconfirm>
                            </p>
                          </div>
                        </>
                      )}
                    /> */}
                  </Table>
                </TableWrapper>
              </UserTableStyleWrapper>
            </Panel>
          </Collapse>
        </Drawer>
      )}
      <Modal title="Confirm" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
        <p>Are you sure to update the status of the component. :)</p>
      </Modal>
    </>
  );
};

export default Dashboard;

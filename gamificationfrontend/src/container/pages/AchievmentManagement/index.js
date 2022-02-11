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
  const [bonusData, setBonusData] = useState([]);
  const [reference, setReference] = useState([]);
  const [showModal2, setShowModal2] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(true);
  const [form] = Form.useForm();
  const [name, setName] = useState('');
  const [assign, setAssign] = useState('');
  const [visible, setVisible] = useState(false);
  const [mcq, setMcq] = useState('');
  const [mmcq, setMmcq] = useState('');
  const [dropdown, setDropdown] = useState('');
  const [allUsers, setAllUsers] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [selectedGroups, setSelectedGroups] = useState([]);
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
    getAllBonuss();
  }, []);

  const getAllBonuss = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}surveyManagement/surveys`;
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
        const rev = result.surveys.reverse();
        let bonusTableData = [];
        rev.map(surveyData => {
          const { surveyId, surveyName, enableDisableStatus, isAssigned, totalQuestions } = surveyData;
          return bonusTableData.push({
            key: surveyId,
            name: surveyName,
            questions: totalQuestions,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => {
                        if (isAssigned) {
                          setShowAlert(true);
                          setAlertText(`${surveyName} has been Assigned`);
                          setAlertType('error');
                          setTimeout(() => {
                            setShowAlert(false);
                            setAlertType('success');
                          }, 3000);
                        } else {
                          updateStatus(e, surveyId, surveyName);
                        }
                      }}
                      checked={enableDisableStatus === true ? true : false}
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
                    if (enableDisableStatus) {
                      if (isAssigned) {
                        setShowAlert(true);
                        setAlertText(`${surveyName} has been Assigned`);
                        setAlertType('error');
                        setTimeout(() => {
                          setShowAlert(false);
                          setAlertType('success');
                        }, 3000);
                      } else {
                        editBonus(surveyId);
                      }
                    } else {
                      setShowAlert(true);
                      setAlertText(`${surveyName} has been disabled`);
                      setAlertType('error');
                      setTimeout(() => {
                        setShowAlert(false);
                        setAlertType('success');
                      }, 3000);
                    }
                  }}
                  //disabled={enableDisableStatus === true ? (isAssigned === false ? false : true) : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            assign: (
              <>
                {isAssigned === false ? (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => assignBonus(surveyId)}>
                    <FeatherIcon icon="plus-square" size={16} />
                  </Button>
                ) : (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => editAssignBonus(surveyId)}>
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
                onClick={() => {
                  if (!isAssigned) {
                    setShowAlert(true);
                    setAlertText(`${surveyName} has been Not Assigned`);
                    setAlertType('error');
                    setTimeout(() => {
                      setShowAlert(false);
                      setAlertType('success');
                    }, 3000);
                  } else {
                    showDrawer(surveyId);
                  }
                }}
                //disabled={!isAssigned}
              >
                <FeatherIcon icon="eye" size={16} />
              </Button>
            ),
          });
        });
        setBonusData(bonusTableData);
        setReference(bonusTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  // const updateStatus = (e, id, name) => {
  //   const user = getItem('user');
  //   const api = process.env.REACT_APP_BACKEND_API;
  //   const URL = `${api}surveyManagement/updateSurveyStatus/${id}`;
  //   const userValues = {
  //     surveyStatus: e,
  //   };
  //   var data = new FormData();
  //   data.append('userJson', JSON.stringify(userValues));
  //   var config = {
  //     method: 'post',
  //     url: URL,
  //     headers: {
  //       'Content-Type': 'multipart/form-data',
  //       Authorization: `Bearer ${user.accessToken}`,
  //     },
  //     data: data,
  //   };

  //   axios(config)
  //     .then(function(response) {
  //       getAllBonuss();
  //       setShowAlert(true);
  //       setAlertText(`${name} status successfully Updated`);
  //       setTimeout(() => {
  //         setShowAlert(false);
  //       }, 3000);
  //       const result = response.data;
  //     })
  //     .catch(function(error) {
  //       console.log(error);
  //     });
  // };

  // const handleSubmit = values => {
  //   if (parseInt(values.mcq) < 2) {
  //     setError('Minimum 2 questions for bonus');
  //     setFlag(true);
  //   }

  //   // else if (parseInt(values.mmcq) < 2) {
  //   //   setError('Minimum 2 questions for each type must be in a Bonus');
  //   //   setFlag(true);
  //   // } else if (parseInt(values.dropdown) < 2) {
  //   //   setError('Minimum 2 questions for each type must be in a Bonus');
  //   //   setFlag(true);
  //   // }
  //   else if (parseInt(values.mcq) >= 2) {
  //     const found = bonusData.some(row => row.name.toLowerCase() === values.bonusName.toLowerCase());
  //     if (found) {
  //       setError('Bonus Name already exists');
  //       setFlag(true);
  //     } else {
  //       const bonus = {
  //         name: values.bonusName,
  //         questions: values.mcq,
  //         // mmcqQuestion: values.mmcq,
  //         // dropdownQuestions: values.dropdown,
  //       };
  //       setItem('bonus', bonus);
  //       history.push('/admin/addBonusQuestion');
  //       handleCancel2();
  //     }
  //   }
  // };
  // const onClose = () => {
  //   setVisible(false);
  // };
  // const showDrawer = id => {
  //   const user = getItem('user');
  //   const api = process.env.REACT_APP_BACKEND_API;
  //   const URL = `${api}surveyManagement/surveyAssignments/${id}`;
  //   var config = {
  //     method: 'get',
  //     url: URL,
  //     headers: {
  //       'Access-Control-Allow-Origin': '*',
  //       Authorization: `Bearer ${user.accessToken}`,
  //     },
  //   };
  //   axios(config)
  //     .then(function(response) {
  //       const result = response.data;
  //       const users = result.assignedUsers;
  //       const groups = result.assignedGroups;
  //       setAssignedUsers(users);
  //       setAssignedGroups(groups);
  //     })
  //     .catch(function(error) {
  //       console.log(error);
  //     });
  //   setVisible(true);
  // };

  const bonusTableColumns = [
    {
      title: 'User Name',
      dataIndex: 'name',
      key: 'name',
      width: 150,
    },
    {
      title: 'Total Achievments',
      dataIndex: 'questions',
      key: 'questions',
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Add/Edit Questions',
    //   dataIndex: 'add',
    //   key: 'add',
    //   width: 200,
    // },
    {
      title: 'No of Survey Attempted',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
      align: 'center',
    },
    // {
    //   title: 'Question Sorting',
    //   dataIndex: 'sort',
    //   key: 'sort',
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'View Details',
      dataIndex: 'viewAssign',
      key: 'viewAssign',
      align: 'center',
      width: 70,
    },
  ];

  // // const sorting = id => {
  // //   history.push(`/admin/sort/${id}`);
  // // };

  // const editBonus = id => {
  //   history.push(`/admin/editQuestion/${id}`);
  // };

  const filterFunction = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setBonusData(filteredData);
  };

  // const handleCancel2 = () => {
  //   setShowModal2(false);
  // };

  // const handleCancel = () => {
  //   setShowModal(false);
  // };

  // const specialCharacter = event => {
  //   setFlag(false);
  //   var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
  //   var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
  //   if (!regex.test(key)) {
  //     event.preventDefault();
  //     return false;
  //   }
  // };

  // const addBonus = bonus => {
  //   setShowModal2(true);
  // };
  // const assignBonus = id => {
  //   history.push(`/admin/assignbonus/${id}`);
  // };

  // const editAssignBonus = id => {
  //   history.push(`/admin/editassignbonus/${id}`);
  // };

  // useEffect(() => {
  //   if (name === '' || mcq === '') {
  //     setLoading(true);
  //   } else {
  //     setLoading(false);
  //   }
  // }, [name, mcq]);

  // const specialCharacter2 = event => {
  //   setFlag(false);
  //   var regex = new RegExp('^[0-9]+$');
  //   var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
  //   if (!regex.test(key)) {
  //     event.preventDefault();
  //     return false;
  //   }
  // };

  // useEffect(() => {
  //   if (selectedUsers.length > 0 || selectedGroups.length > 0) {
  //     setLoading2(false);
  //   } else {
  //     setLoading2(true);
  //   }
  // }, [selectedUsers, selectedGroups]);

  return (
    <>
      <PageHeader title="Achievement Management Dashboard" />
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
                    {/* <Button className="btn-export" type="primary" size="medium" onClick={addBonus}>
                      Add Bonus
                    </Button> */}
                  </div>
                  <div>
                    <Input
                      placeholder="Search Person"
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
                      columns={bonusTableColumns}
                      scroll={{ y: 340, x: 600 }}
                      dataSource={bonusData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: bonusData.length,
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
    </>
  );
};

export default Dashboard;

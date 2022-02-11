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
import {useSelector} from 'react-redux';

const Dashboard = () => {
  const location = useLocation();
  const { Panel } = Collapse;
  const { Column } = Table;
  const history = useHistory();
  const [surveyData, setSurveyData] = useState([]);
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
  const user = useSelector(state=> state?.auth?.user);
  const [admins, setAdmins] = useState([]);
  const [countryTemp, setCountryTemp] = useState([]);
  const [countries, setCountries] = useState([]);

  useEffect(() => {
    if (location.state !== null && location.state !== undefined) {
      setShowAlert(true);
      setAlertText(location.state.detail);
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, []);

  // useEffect(() => {
  //   getAllSurveys();
  // }, []);

  useEffect(() => {
           
            getAllAdmins();
            if(user.role == '"ADMIN"'){
              console.log(user.role  + "first");
              getCurrentUserCountry();
              //getAllUsersInCountry();
            }else if(user.role == '"SuperAdmin"'){
              getAllSurveys();
            }
            //new end
  }, []);

  const getCurrentUserCountry = () => {
    const emailTemp = user.email;
    console.log(emailTemp); 
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/getUser/${emailTemp}`;   
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
        console.log(result.country);
        setCountryTemp(result.country);
        const countryTemp = result.country;
        console.log(countryTemp + "now");
        getAllSurveysInCountry(countryTemp);
      })
      .catch(function(error) {
        console.log(error);
      });
  }

  const getAllAdmins = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/admins`;   
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
        const admins = result.users;
        console.log(admins);
        setAdmins(admins);
      })
      .catch(function(error) {
        console.log(error);
      });
  }

  useEffect(() => {
    getAllCountries();
  }, []);

  const getAllCountries = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    //const URL = `${api}countrylanguageManagement/countries`;
    const URL = `${api}countryManagement/countries`;
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
        const countries = result.countries.reverse();
        console.log('bgb', countries);

        let countryTableData = countries.filter(i=>i.enableDisableStatus);
        // let countryTableData = [];
        // countries.map(countryData => {
        //   const { id, countryName, enableDisableStatus} = countryData;
        //   if (enableDisableStatus === true) {
        //     return countryTableData.push(countryData);
        //   }
        //});
        setCountries(countryTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };


  const getAllSurveysInCountry = (countryTemp) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}surveyManagement/surveyInCountry/${countryTemp}`;
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
        console.log(rev);
        let surveyTableData = [];
        rev.map(surveyData => {
          const { surveyId, surveyName, enableDisableStatus, isAssigned, questions } = surveyData;
          return surveyTableData.push({
            key: surveyId,
            name: surveyName,
            questions: questions.length,
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
            sort: (
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
                        sorting(surveyId);
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
                  <FeatherIcon icon="move" size={16} />
                </Button>
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
                        editSurvey(surveyId);
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
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => assignSurvey(surveyId)}>
                    <FeatherIcon icon="plus-square" size={16} />
                  </Button>
                ) : (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => editAssignSurvey(surveyId)}>
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
        setSurveyData(surveyTableData);
        setReference(surveyTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getAllSurveys = () => {
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
        console.log(rev);
        let surveyTableData = [];
        rev.map(surveyData => {
          const { surveyId, surveyName, enableDisableStatus, isAssigned, questions } = surveyData;
          return surveyTableData.push({
            key: surveyId,
            name: surveyName,
            questions: questions.length,
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
            sort: (
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
                        sorting(surveyId);
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
                  <FeatherIcon icon="move" size={16} />
                </Button>
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
                        editSurvey(surveyId);
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
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => assignSurvey(surveyId)}>
                    <FeatherIcon icon="plus-square" size={16} />
                  </Button>
                ) : (
                  <Button className="btn-icon" to="#" shape="circle" onClick={() => editAssignSurvey(surveyId)}>
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
        setSurveyData(surveyTableData);
        setReference(surveyTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}surveyManagement/updateSurveyStatus/${id}`;
    var data = new FormData();
    data.append('userJson', e);
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
        getAllSurveys();
        setShowAlert(true);
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
    if (parseInt(values.mcq) < 2) {
      setError('Minimum 2 questions for each type must be in a Survey');
      setFlag(true);
    } else if (parseInt(values.mmcq) < 2) {
      setError('Minimum 2 questions for each type must be in a Survey');
      setFlag(true);
    } else if (parseInt(values.dropdown) < 2) {
      setError('Minimum 2 questions for each type must be in a Survey');
      setFlag(true);
    } else if (parseInt(values.mcq) >= 2) {
      const found = surveyData.some(row => row.name.toLowerCase() === values.surveyName.toLowerCase());
      if (found) {
        setError('Survey Name already exists');
        setFlag(true);
      } else {
        const survey = {
          name: values.surveyName,
          questions: values.mcq,
          mmcqQuestion: values.mmcq,
          dropdownQuestions: values.dropdown,
          country: countryTemp,
        };
        setItem('survey', survey);
        history.push('/admin/addQuestion');
        handleCancel2();
      }
    }
  };
  const onClose = () => {
    setVisible(false);
  };
  const showDrawer = id => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}surveyManagement/surveyAssignments/${id}`;
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
        const users = result.assignedUsers;
        const groups = result.assignedGroups;
        setAssignedUsers(users);
        setAssignedGroups(groups);
      })
      .catch(function(error) {
        console.log(error);
      });
    setVisible(true);
  };

  const surveyTableColumns = [
    {
      title: 'Survey Name',
      dataIndex: 'name',
      key: 'name',
      width: 150,
    },
    {
      title: 'Total Questions',
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
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
      align: 'center',
    },
    {
      title: 'Question Sorting',
      dataIndex: 'sort',
      key: 'sort',
      width: 200,
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
      title: 'Assign/Modify',
      dataIndex: 'assign',
      key: 'assign',
      align: 'center',
      width: 150,
    },
    {
      title: 'View',
      dataIndex: 'viewAssign',
      key: 'viewAssign',
      align: 'center',
      width: 70,
    },
  ];

  const sorting = id => {
    history.push(`/admin/sort/${id}`);
  };

  const editSurvey = id => {
    history.push(`/admin/editQuestion/${id}`);
  };

  const filterFunction = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setSurveyData(filteredData);
  };

  const handleCancel2 = () => {
    setShowModal2(false);
  };

  const handleCancel = () => {
    setShowModal(false);
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

  const addSurvey = survey => {
    setShowModal2(true);
  };
  const assignSurvey = id => {
    history.push(`/admin/assign/${id}`);
  };

  const editAssignSurvey = id => {
    history.push(`/admin/editassign/${id}`);
  };

  useEffect(() => {
    if (name === '' || mcq === '' || mmcq === '' || dropdown === '') {
      setLoading(true);
    } else {
      setLoading(false);
    }
  }, [name, mcq, mmcq, dropdown]);

  const specialCharacter2 = event => {
    setFlag(false);
    var regex = new RegExp('^[0-9]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  useEffect(() => {
    if (selectedUsers.length > 0 || selectedGroups.length > 0) {
      setLoading2(false);
    } else {
      setLoading2(true);
    }
  }, [selectedUsers, selectedGroups]);

  const selectedFilterAdmin = id => {
    const user = admins.find(i=>i.id== id);

      if(user.country == "ALL COUNTRIES"){
        getAllSurveys();
      }else{
        getAllSurveysInCountry(user.country);
      }
  }

  const selectedAdmin = id => {
    const user = admins.find(i=>i.id== id);
    console.log(user);
    setCountryTemp(user.country);
    console.log(countryTemp);
      // if(user.country == "ALL COUNTRIES"){
      //   getAllSurveys();
      // }else{
      //   getAllSurveysInCountry(user.country);
      // }
  }


  return (
    <>
      <PageHeader title="Survey Management Dashboard" />
      <Main>
        <Row gutter={25}>
          <Col sm={24} xs={24}>
          {user?.role ===   '"SuperAdmin"'? <Col>
            <Select placeholder="Choose Admin"  onChange={selectedFilterAdmin} >            
                {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
            </Select>
            </Col>:null}
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
                    <Button className="btn-export" type="primary" size="medium" onClick={addSurvey}>
                      Add Survey
                    </Button>
                  </div>
                  <div>
                    <Input
                      placeholder="Search Survey Name"
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
                      columns={surveyTableColumns}
                      scroll={{ y: 340, x: 600 }}
                      dataSource={surveyData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: surveyData.length,
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
        title="Add Survey"
        wrapClassName="sDash_export-wrap"
        visible={showModal2}
        footer={null}
        onCancel={handleCancel2}
      >
        <Form name="AddSurvey" form={form} onFinish={handleSubmit} layout="vertical">
        {user?.role ===   '"SuperAdmin"'?
        <Form.Item
        name="Choose Admin"
        label="Choose Admin"
        rules={[{ message: 'Please choose an Admin', required: true }]}
      >
         <Select placeholder="Choose Admin"  onChange={selectedAdmin} >            
                 {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
            </Select>

      </Form.Item>
        
        
        // <Col>
        //     <Select placeholder="Choose Admin"  onChange={e => setCountryTemp(e.target.value)} >            
        //         {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
        //     </Select>
        //     </Col>
            :null}
          <Form.Item
            name="surveyName"
            label="Survey Name"
            rules={[{ message: 'Please enter survey name', required: true }]}
          >
            <Input
              placeholder="Survey Name"
              onKeyPress={specialCharacter}
              maxlength="40"
              onChange={e => setName(e.target.value)}
            />
          </Form.Item>
          <Form.Item
            name="mcq"
            label="Total MCQ Questions"
            // rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input
              placeholder="MCQ Questions"
              type="text"
              maxlength="2"
              onKeyPress={specialCharacter2}
              onChange={e => setMcq(e.target.value)}
            />
          </Form.Item>
          <Form.Item
            name="mmcq"
            label="Total MMCQ Questions"
            //rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input placeholder="MMCQ Questions" type="number" onChange={e => setMmcq(e.target.value)} />
          </Form.Item>
          <Form.Item
            name="dropdown"
            label="Total Dropdown Questions"
            //rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input placeholder="Dropdown Questions" type="number" onChange={e => setDropdown(e.target.value)} />
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
      <Drawer
        width={440}
        title="Assigned Users/Group"
        placement="right"
        closable={false}
        onClose={onClose}
        visible={visible}
      >
        {assignedUsers.length > 0 ? (
          <UserTableStyleWrapper>
            <p>Assigned To Users</p>
            <TableWrapper className="table-responsive">
              <Table id="myTable" scroll={{ y: 240 }} dataSource={assignedUsers} pagination={false}>
                <Column
                  title="Name"
                  dataIndex="userName"
                  key="userName"
                  render={(text, record) => (
                    <>
                      <div className="user-info">
                        <figure>
                          <img
                            style={{ width: '40px', cursor: 'pointer' }}
                            src={
                              record.userImageKey === null
                                ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                : record.userImageKey
                            }
                            alt="userImage"
                          />
                        </figure>
                        <figcaption>
                          <Heading className="user-name" as="h6">
                            {record.userName}
                          </Heading>
                        </figcaption>
                      </div>
                    </>
                  )}
                />
                <Column title="Email" dataIndex="userEmail" key="userEmail" />
              </Table>
            </TableWrapper>
          </UserTableStyleWrapper>
        ) : (
          ''
        )}
        {assignedGroups.length > 0 ? (
          <>
            {assignedGroups.map(groups => {
              return (
                <Collapse accordion style={{ marginTop: '10px' }}>
                  <Panel header={groups.groupName}>
                    <UserTableStyleWrapper>
                      <TableWrapper className="table-responsive">
                        <Table id="myTable" scroll={{ y: 240 }} dataSource={groups.assignedUsers} pagination={false}>
                          <Column
                            title="Name"
                            dataIndex="userName"
                            key="userName"
                            render={(text, record) => (
                              <>
                                <div className="user-info">
                                  <figure>
                                    <img
                                      style={{ width: '40px', cursor: 'pointer' }}
                                      src={
                                        record.userImageKey === null
                                          ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                          : record.userImageKey
                                      }
                                      alt="userImage"
                                    />
                                  </figure>
                                  <figcaption>
                                    <Heading className="user-name" as="h6">
                                      {record.userName}
                                    </Heading>
                                  </figcaption>
                                </div>
                              </>
                            )}
                          />
                          <Column title="Email" dataIndex="userEmail" key="userEmail" />
                        </Table>
                      </TableWrapper>
                    </UserTableStyleWrapper>
                  </Panel>
                </Collapse>
              );
            })}
          </>
        ) : (
          ''
        )}
      </Drawer>
    </>
  );
};

export default Dashboard;

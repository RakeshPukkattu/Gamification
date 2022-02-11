import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Select, Drawer, Collapse, Alert } from 'antd';
import { PageHeader } from '../../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../../styled';
import { UserTableStyleWrapper } from '../../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../../components/cards/frame/cards-frame';
import { Button } from '../../../../components/buttons/buttons';
import { useHistory, useLocation } from 'react-router-dom';
import Heading from '../../../../components/heading/heading';
import axios from 'axios';
import { getItem } from '../../../../utility/localStorageControl';
import {useSelector} from 'react-redux';

const Dashboard = () => {
  const { Column } = Table;
  const location = useLocation();
  const history = useHistory();
  const [selectFilter, setSelectFilter] = useState('');
  const [show, setShow] = useState(false);
  const [sessionData, setSessionData] = useState([]);
  const [singleSession, setSingleSession] = useState(null);
  const [reference, setReference] = useState([]);
  const [visible, setVisible] = useState(false);
  const { Panel } = Collapse;
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [sessionStatus, setSessionStatus] = useState('');
  const user = useSelector(state=> state?.auth?.user);
  const [admins, setAdmins] = useState([]);
  const [country,setCountry] = useState([]);


  useEffect(() => {
           
    if(user.role == '"ADMIN"'){
      console.log(user.role  + "first");
      getCurrentUserCountry();
    }else if(user.role == '"SuperAdmin"'){
      getAllSessions();
    }
 
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
        const countryTemp = result.country;
        console.log(countryTemp + "now");
        setCountry(countryTemp);
        getAllSessionsInCountry(countryTemp);
      })
      .catch(function(error) {
        console.log(error);
      });
    }

    useEffect(() => {
      getAllAdmins();
    }, []);
  
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
    if (location.state !== null && location.state !== undefined) {
      setShowAlert(true);
      setAlertText(location.state.detail);
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, []);

  const showDrawer = session => {
    setSingleSession(session);
    let sessionStatus = '';
    var dateFirst = new Date();
    var dateSecond = new Date(session.endTime);
    var timeDiff = Math.abs(dateSecond.getTime() - dateFirst.getTime());
    var diffHours = Math.ceil(timeDiff / (1000 * 3600));
    if (diffHours > 4) {
      sessionStatus = 'Not Initiated';
    } else if (diffHours <= 4 && diffHours >= 0) {
      sessionStatus = 'In Progress';
    } else {
      sessionStatus = 'Completed';
    }
    setSessionStatus(sessionStatus);
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  // useEffect(() => {
  //   getAllSessions();
  // }, []);

  const getAllSessionsInCountry = (countryTemp) => {
    console.log(countryTemp);
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}gameSessionUserManagement/gameUserSession/sessionInCountry/${countryTemp}`;
    console.log(URL);
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
        const sessions = result.sessions.reverse();
        console.log(sessions);
        let sessionsTableData = [];
        sessions.map(session => {
          const {
            sessionId,
            sessionName,
            startTime,
            endTime,
            sessionEnableDisable,
            sessionCurrentStatus,
            getUsersInSession,
          } = session;
          let sessionStatus = '';
          var dateFirst = new Date();
          var dateSecond = new Date(endTime);
          var timeDiff = Math.abs(dateSecond.getTime() - dateFirst.getTime());
          var diffHours = Math.ceil(timeDiff / (1000 * 3600));
          console.log(diffHours);
          if (diffHours > 4) {
            sessionStatus = 'Not Initiated';
          } else if (diffHours <= 4 && diffHours >= 0) {
            sessionStatus = 'In Progress';
          } else {
            sessionStatus = 'Completed';
          }
          return sessionsTableData.push({
            key: sessionId,
            name: sessionName,
            startDateTime: startTime,
            endDateTime: endTime,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => updateStatus(e, sessionId, sessionName)}
                      defaultChecked={sessionEnableDisable === true ? true : false}
                      style={{ marginLeft: '30px' }}
                      disabled={sessionCurrentStatus !== 'not_Initiated' ? true : false}
                    />
                  </p>
                </div>
              </>
            ),
            status: sessionStatus,
            modify: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  disabled={
                    sessionEnableDisable === true ? (sessionCurrentStatus !== 'not_Initiated' ? true : false) : true
                  }
                  onClick={() => {
                    history.push(`/admin/editusersession/${sessionId}`);
                  }}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(session)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        });
        setSessionData(sessionsTableData);
        setReference(sessionsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getAllSessions = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}gameSessionUserManagement/gameUserSessions`;
    console.log(URL);
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
        const sessions = result.sessions.reverse();
        console.log(sessions);
        let sessionsTableData = [];
        sessions.map(session => {
          const {
            sessionId,
            sessionName,
            startTime,
            endTime,
            sessionEnableDisable,
            sessionCurrentStatus,
            getUsersInSession,
          } = session;
          let sessionStatus = '';
          var dateFirst = new Date();
          var dateSecond = new Date(endTime);
          var timeDiff = Math.abs(dateSecond.getTime() - dateFirst.getTime());
          var diffHours = Math.ceil(timeDiff / (1000 * 3600));
          console.log(diffHours);
          if (diffHours > 4) {
            sessionStatus = 'Not Initiated';
          } else if (diffHours <= 4 && diffHours >= 0) {
            sessionStatus = 'In Progress';
          } else {
            sessionStatus = 'Completed';
          }
          return sessionsTableData.push({
            key: sessionId,
            name: sessionName,
            startDateTime: startTime,
            endDateTime: endTime,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => updateStatus(e, sessionId, sessionName)}
                      defaultChecked={sessionEnableDisable === true ? true : false}
                      style={{ marginLeft: '30px' }}
                      disabled={sessionCurrentStatus !== 'not_Initiated' ? true : false}
                    />
                  </p>
                </div>
              </>
            ),
            status: sessionStatus,
            modify: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  disabled={
                    sessionEnableDisable === true ? (sessionCurrentStatus !== 'not_Initiated' ? true : false) : true
                  }
                  onClick={() => {
                    history.push(`/admin/editusersession/${sessionId}`);
                  }}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(session)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        });
        setSessionData(sessionsTableData);
        setReference(sessionsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const sessionsTableData = [];

  const sessionsTableColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => {
        return a.name.toString().localeCompare(b.name.toString());
      },
    },
    {
      title: 'Start Date and Time',
      dataIndex: 'startDateTime',
      key: 'startDateTime',
      align: 'center',
    },
    {
      title: 'End Date and Time',
      dataIndex: 'endDateTime',
      key: 'endDateTime',
      align: 'center',
    },
    {
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
      align: 'center',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
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

  const selectedFilterAdmin = id => {
    const user = admins.find(i=>i.id== id);

    console.log(user.country);
      if(user.country == "ALL COUNTRIES"){
        getAllSessions();
      }else{
       getAllSessionsInCountry(user.country);
      }
  }


  const selectedFilter = e => {
    setShow(true);
    setSelectFilter(e);
  };

  const filterFunction = e => {
    var selectedFilter = selectFilter;
    var x = 1;
    if (selectedFilter === 'name') {
      x = 1;
    }
    if (selectedFilter === 'status') {
      x = 5;
    }
    if (x === 1) {
      const currValue = e.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
      setSessionData(filteredData);
    }
    if (x === 5) {
      const currValue = e.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.status.toLowerCase().includes(currValue));
      setSessionData(filteredData);
    }
  };

  const addSession = () => {
    history.push('/admin/addusersession');
  };

  const usersTableColumns = [
    {
      title: 'Name',
      dataIndex: 'userName',
      key: 'userName',
    },
    {
      title: 'Email',
      dataIndex: 'userEmail',
      key: 'userEmail',
    },
  ];

  const dataTable = [];

  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}gameSessionUserManagement/updateEnableDisable/${id}`;
    const userValues = {
      sessionEnableDisable: e,
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
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
        getAllSessions();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <>
      <PageHeader title="User Session Management Dashboard" />
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
                    marginLeft: '-80px',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type="success" showIcon /> : ''}
                </div>
                <div className="sDash_export-box" style={{ marginBottom: '20px' }}>
                  <div>
                    <Button className="btn-export" type="primary" size="medium" onClick={addSession}>
                      Create Session
                    </Button>
                  </div>
                  <div>
                  {user?.role ===   '"SuperAdmin"'? <Col>
                  <Select 
                  placeholder="Choose Admin"  onChange={selectedFilterAdmin} >            
                   {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
                  </Select>
                  </Col>:null}
                    <Select
                      // defaultValue="name"
                      placeholder="Search By"
                      style={{ marginRight: '10px' }}
                      onChange={selectedFilter}
                    >
                      <Option value="name">Name</Option>
                      <Option value="status">Status</Option>
                    </Select>
                    <Input
                      placeholder="Search Here"
                      style={{ width: '50%', height: '80%' }}
                      id="myInput"
                      onKeyUp={filterFunction}
                    />
                  </div>
                </div>
                <UserTableStyleWrapper>
                  <TableWrapper className="table-responsive">
                    <Table
                      id="myTable"
                      scroll={{ y: 340, x: true }}
                      columns={sessionsTableColumns}
                      dataSource={sessionData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: sessionData.length,
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
      {singleSession === null ? (
        ''
      ) : (
        <Drawer
          width={440}
          title={singleSession.sessionName}
          placement="right"
          closable={false}
          onClose={onClose}
          visible={visible}
        >
          <p>Status: {sessionStatus}</p>
          <UserTableStyleWrapper>
            <TableWrapper className="table-responsive">
              <Table id="myTable" scroll={{ y: 240 }} dataSource={singleSession.getUsersInSession} pagination={false}>
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
                              record.avatharKey === null
                                ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                : record.avatharKey
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
        </Drawer>
      )}
    </>
  );
};

export default Dashboard;

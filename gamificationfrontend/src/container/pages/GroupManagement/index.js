import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Select, Drawer, Alert, Spin } from 'antd';
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
import {useSelector} from 'react-redux';

const Dashboard = () => {
  const { Column } = Table;
  const location = useLocation();
  const history = useHistory();
  const [selectFilter, setSelectFilter] = useState('');
  const [show, setShow] = useState(false);
  const [showdelete, setShowDelete] = useState(true);
  const [groupsData, setGroupsData] = useState([]);
  const [reference, setReference] = useState([]);
  const [singleGroup, setSingleGroup] = useState(null);
  const [allUsers, setAllUsers] = useState([]);
  const [userReference, setUserReference] = useState([]);
  const [visible, setVisible] = useState(false);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('success');
  const [loading2, setLoading2] = useState(false);

  const user = useSelector(state=> state?.auth?.user);
  const [admins, setAdmins] = useState([]);

  useEffect(() => {
           
    if(user.role == '"ADMIN"'){
      console.log(user.role  + "first");
      getCurrentUserCountry();
    }else if(user.role == '"SuperAdmin"'){
      getAllGroups();
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
        getAllGroupsInCountry(countryTemp);
      })
      .catch(function(error) {
        console.log(error);
      });
    }

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

  const showDrawer = group => {
    setSingleGroup(group);
    console.log(group);
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
  });
  const groupsTableData = [];

  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      if (selectedRowKeys.length > 0) {
        setShowDelete(false);
      }
      if (selectedRowKeys.length === 0) {
        setShowDelete(true);
      }
      setState({ ...state, selectedRowKeys, selectedRows });
    },
    getCheckboxProps: record => {
      return {
        disabled: record.enableDisable.props.children.props.children.props.children.props.defaultChecked === false,
      };
    },
  };

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

  const getAllGroupsInCountry = (countryTemp) => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}groupManagement/groupsInCountry/${countryTemp}`;
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
        const groups = result.groups.reverse();
        let groupsTableData = [];
        console.log(groups);
        groups.map(group => {
          const { groupId, groupName, groupStatus, groupMembers, groupInAnySession } = group;
          setUserReference(groupMembers);
          return groupsTableData.push({
            key: groupId,
            name: groupName,
            groupStatus: groupInAnySession === true ? 'In Use' : 'Not in Use',
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => updateStatus(e, groupId, groupName)}
                      defaultChecked={groupStatus === true ? true : false}
                      style={{ marginLeft: '30px' }}
                      disabled={groupInAnySession}
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
                    history.push(`/admin/editgroup/${groupId}`);
                  }}
                  disabled={groupStatus === true ? groupInAnySession : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            clone: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    history.push(`/admin/clonegroup/${groupId}`);
                  }}
                >
                  <FeatherIcon icon="copy" size={16} />
                </Button>
              </>
            ),
            details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(group)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        });
        setGroupsData(groupsTableData);
        setReference(groupsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getAllGroups = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}groupManagement/groups`;
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
        const groups = result.groups.reverse();
        let groupsTableData = [];
        console.log(groups);
        groups.map(group => {
          const { groupId, groupName, groupStatus, groupMembers, groupInAnySession } = group;
          setUserReference(groupMembers);
          return groupsTableData.push({
            key: groupId,
            name: groupName,
            groupStatus: groupInAnySession === true ? 'In Use' : 'Not in Use',
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => updateStatus(e, groupId, groupName)}
                      defaultChecked={groupStatus === true ? true : false}
                      style={{ marginLeft: '30px' }}
                      disabled={groupInAnySession}
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
                    history.push(`/admin/editgroup/${groupId}`);
                  }}
                  disabled={groupStatus === true ? groupInAnySession : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            clone: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    history.push(`/admin/clonegroup/${groupId}`);
                  }}
                >
                  <FeatherIcon icon="copy" size={16} />
                </Button>
              </>
            ),
            details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(group)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        });
        setGroupsData(groupsTableData);
        setReference(groupsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}groupManagement/updateStatus/${id}`;
    const userValues = {
      groupStatus: e,
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
        getAllGroups();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const usersTableData = [];

  useEffect(() => {
    if (userReference !== undefined) {
      userReference.map(user => {
        const { id, name, imageKey, email, status } = user;
        if (status === true) {
          usersTableData.push({
            key: id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      imageKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : imageKey
                    }
                    alt="userImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {name}
                  </Heading>
                </figcaption>
              </div>
            ),
            email: email,
          });
        }
      });
      setAllUsers(usersTableData);
    }
  }, [userReference]);

  const groupsTableColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => {
        return a.name.toString().localeCompare(b.name.toString());
      },
    },
    {
      title: 'Group Status',
      dataIndex: 'groupStatus',
      key: 'groupStatus',
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
      width: 90,
      align: 'center',
    },
    {
      title: 'Clone',
      dataIndex: 'clone',
      key: 'clone',
      width: 90,
      align: 'center',
    },
    {
      title: 'Details',
      dataIndex: 'details',
      key: 'details',
      width: 90,
      align: 'center',
    },
  ];

  const selectedFilter = e => {
    setShow(true);
    setSelectFilter(e);
  };

  const selectedFilterAdmin = id => {
    const user = admins.find(i=>i.id== id);

    console.log(user.country);
      if(user.country == "ALL COUNTRIES"){
        getAllGroups();
      }else{
       getAllGroupsInCountry(user.country);
      }
  }

  const filterFunction = e => {
    var selectedFilter = selectFilter;
    var x = 1;
    if (selectedFilter === 'name') {
      x = 1;
    }
    if (selectedFilter === 'groupStatus') {
      x = 2;
    }
    if (x === 1) {
      const currValue = e.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
      setGroupsData(filteredData);
    }
    if (x === 2) {
      const currValue = e.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.groupStatus.toLowerCase().includes(currValue));
      setGroupsData(filteredData);
    }
  };

  const addGroup = () => {
    history.push('/admin/addgroup');
  };

  const usersTableColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
  ];

  const dataTable = [];

  const deleteGroups = () => {
    setLoading2(true);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let groupIds = [];
    state.selectedRowKeys.map(id => {
      groupIds.push(id.toString());
    });
    const groupValues = {
      groupMemberIds: groupIds,
    };
    var data = new FormData();
    data.append('groupIds', JSON.stringify(groupValues));
    const URL = `${api}groupManagement/deleteGroups`;
    var config = {
      method: 'delete',
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
        setAlertText(`Group Successfully Deleted`);
        setAlertType('success');
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        setLoading2(false);
        setShowDelete(true);
        getAllGroups();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <>
      <PageHeader title="Group Management Dashboard" />
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
                  {showAlert ? <Alert message={alertText} type="success" showIcon /> : ''}
                </div>
                <div className="sDash_export-box" style={{ marginBottom: '20px' }}>
                  <div>
                    <Button className="btn-export" type="primary" size="medium" onClick={addGroup}>
                      Create Group
                    </Button>
                    {/* <Button
                      className="btn-export"
                      style={{ marginLeft: '10px' }}
                      type={loading2 ? '' : 'danger'}
                      size="medium"
                      onClick={deleteGroups}
                      disabled={showdelete}
                    >
                      {loading2 ? <Spin size="medium" /> : 'Delete Groups'}
                    </Button> */}
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
                      style={{ marginLeft: '10px' }}
                      onChange={selectedFilter}
                    >
                      <Option value="name">Name</Option>
                      <Option value="groupStatus">Group Status</Option>
                    </Select>
                    <Input
                      placeholder="Search Here"
                      style={{ width: '57%', height: '41%' }}
                      id="myInput"
                      onKeyUp={filterFunction}
                    />
                  </div>
                </div>

                <UserTableStyleWrapper>
                  <TableWrapper className="table-responsive">
                    <Table
                      id="myTable"
                      rowSelection={rowSelection}
                      columns={groupsTableColumns}
                      scroll={{ y: 340, x: true }}
                      dataSource={groupsData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: groupsData.length,
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
      {singleGroup === null ? (
        ''
      ) : (
        <Drawer
          width={440}
          title={singleGroup.groupName}
          placement="right"
          closable={false}
          onClose={onClose}
          visible={visible}
        >
          <UserTableStyleWrapper>
            <TableWrapper className="table-responsive">
              <Table id="myTable" scroll={{ y: 240 }} dataSource={singleGroup.members} >
                <Column
                  title="Name"
                  dataIndex="name"
                  key="name"
                  render={(text, record) => (
                    <>
                      <div className="user-info">
                        <figure>
                          <img
                            style={{ width: '40px', cursor: 'pointer' }}
                            src={
                              record.imageKey === null
                                ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                                : record.imageKey
                            }
                            alt="userImage"
                          />
                        </figure>
                        <figcaption>
                          <Heading className="user-name" as="h6">
                            {record.name}
                          </Heading>
                        </figcaption>
                      </div>
                    </>
                  )}
                />
                <Column title="Email" dataIndex="email" key="email" />
              </Table>
            </TableWrapper>
          </UserTableStyleWrapper>
        </Drawer>
      )}
    </>
  );
};

export default Dashboard;

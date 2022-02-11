import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch,Select, Input, Spin, Form, Alert } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory } from 'react-router-dom';
import _ from 'lodash';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';
import {useSelector} from 'react-redux';

const Dashboard = () => {
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [disable, setDisable] = useState(true);
  const [disable2, setDisable2] = useState(true);
  const [groupName, setGroupName] = useState('');
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [reference, setReference] = useState([]);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('');
  const [reference2, setReference2] = useState([]);
  const [users, setUsers] = useState([]);
  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
    selectedRowKeys1: 0,
    selectedRows1: [],
  });
  const user = useSelector(state=> state?.auth?.user);
  const [admins, setAdmins] = useState([]);
  const [country, setCountry] = useState([]);

  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      setDisable(false);
      setDisable2(true);
      if (selectedRowKeys.length === 0) {
        setDisable(true);
      }
      setState({ ...state, selectedRowKeys, selectedRows });
    },
  };

  const rowSelection2 = {
    onChange: (selectedRowKeys1, selectedRows1) => {
      setDisable(true);
      setDisable2(false);
      if (selectedRowKeys1.length === 0) {
        setDisable2(true);
      }
      setState({ ...state, selectedRowKeys1, selectedRows1 });
    },
  };

  const handleSubmit = values => {
    setLoading2(true);
    const api = process.env.REACT_APP_BACKEND_API;
    console.log(selectedUsers);
    let userId = [];
    selectedUsers.map(user => {
      userId.push(user.key.toString());
    });
    const groupValues = {
      groupName: values.groupName,
      groupMemberIds: userId,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(groupValues));
    data.append('country',country);
    const URL = `${api}groupManagement/creategroup`;
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
        setLoading2(false);
        const result = response.data;
        console.log(result);
        if (result.code.includes('406')) {
          setShowAlert(true);
          setAlertText('Group with same name already exists!');
          setAlertType('danger');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/group',
            state: { detail: `${values.groupName} Added Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };
  const handleChange = e => {
    setGroupName(e.target.value);
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

  // useEffect(() => {
  //   getAllUsers();
  // }, []);

  useEffect(() => {
           
    if(user.role == '"ADMIN"'){
      console.log(user.role  + "first");
      getCurrentUserCountry();  
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
      getAllUsersInCountry(countryTemp);
    })
    .catch(function(error) {
      console.log(error);
    });
}

const getAllUsersInCountry = (countryTemp) => {
  const api = process.env.REACT_APP_BACKEND_API;
  const URL = `${api}userManagement/usersInCountry/${countryTemp}`;
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
      const rev = result.users.reverse();
      setUsers(rev);
      rev.map(user => {
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
      setReference(usersTableData);
      setAllUsers(usersTableData);
    })
    .catch(function(error) {
      console.log(error);
    });
};

  const getAllUsers = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/users`;
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
        const rev = result.users;
        rev.map(user => {
          const { id, name, imageKey, email, status, user_In_Group } = user;
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
        setReference(usersTableData);
        setAllUsers(usersTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const selectedFilterAdmin = id => {
    const user = admins.find(i=>i.id== id);
      setCountry(user.country);
      if(user.country == "ALL COUNTRIES"){
        getAllUsers();
      }else{
        getAllUsersInCountry(user.country);
      }
  }
  const usersTableData = [];

  const usersTableData2 = [];

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

  const addSelected = () => {
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys.map(key => {
      users.map(user => {
        if (user.id === key) {
          usersTableData2.push({
            key: user.id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      user.imageKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : user.imageKey
                    }
                    alt="userImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {user.name}
                  </Heading>
                </figcaption>
              </div>
            ),
            email: user.email,
          });
        }
      });
    });
    let merged = usersTableData2.concat(selectedUsers);
    setSelectedUsers(merged);
    setReference2(merged);
    const sorted = _.differenceBy(allUsers, usersTableData2, 'key');
    setAllUsers(sorted);
    setReference(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const removeSelected = () => {
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys1.map(key => {
      users.map(user => {
        if (user.id === key) {
          usersTableData.push({
            key: user.id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      user.imageKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : user.imageKey
                    }
                    alt="userImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {user.name}
                  </Heading>
                </figcaption>
              </div>
            ),
            email: user.email,
          });
        }
      });
    });
    let merged = usersTableData.concat(allUsers);
    setAllUsers(merged);
    setReference(merged);
    const sorted = _.differenceBy(selectedUsers, usersTableData, 'key');
    setSelectedUsers(sorted);
    setReference2(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const filterFunction = (tableId, inputField) => {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById(inputField);
    filter = input.value.toUpperCase();
    table = document.getElementById(tableId);
    tr = table.getElementsByTagName('tr');
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName('td')[1];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = '';
        } else {
          tr[i].style.display = 'none';
        }
      }
    }
  };

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const searchTable = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry =>
      entry.name.props.children[1].props.children.props.children.toLowerCase().includes(currValue),
    );
    setAllUsers(filteredData);
  };

  const searchTable2 = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference2.filter(entry =>
      entry.name.props.children[1].props.children.props.children.toLowerCase().includes(currValue),
    );
    setSelectedUsers(filteredData);
  };

  useEffect(() => {
    if (selectedUsers.length > 1 && groupName.length > 0) {
      setLoading(false);
    } else {
      setLoading(true);
    }
  }, [selectedUsers, groupName]);

  return (
    <>
      <PageHeader title="Create Group" />
      <Main>
        <Row gutter={25}>
          <Col sm={24} xs={24}>
          {user?.role ===   '"SuperAdmin"'? <Col>
                  <Select 
                  placeholder="Choose Admin"  onChange={selectedFilterAdmin} >            
                   {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
                  </Select>
                  </Col>:null}
            <ExportStyleWrap>
              <Cards headless>
                <div
                  style={{
                    width: '100%',
                    marginLeft: '100px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    position: 'absolute',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type="error" showIcon /> : ''}
                </div>
                <Form name="adduser" form={form} onFinish={handleSubmit} layout="vertical">
                  <Form.Item
                    name="groupName"
                    rules={[{ message: 'Please enter group name', required: true }]}
                    label="Group Name"
                  >
                    <Input
                      placeholder="Group Name"
                      onChange={handleChange}
                      onKeyPress={specialCharacter}
                      maxlength="40"
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Row gutter={25}>
                    <Col sm={10} xs={24}>
                      <UserTableStyleWrapper>
                        <p>Select Users</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            style={{ height: '300px' }}
                            id="allUser"
                            rowSelection={rowSelection}
                            scroll={{ y: 240 }}
                            columns={usersTableColumns}
                            dataSource={allUsers}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search User"
                          style={{ width: '50%', float: 'right', marginTop: '10px' }}
                          id="myInput"
                          // onKeyUp={() => {
                          //   filterFunction('allUser', 'myInput');
                          // }}
                          onKeyUp={e => {
                            searchTable(e);
                          }}
                        />
                      </UserTableStyleWrapper>
                    </Col>
                    <Col sm={4} xs={24}>
                      <div className="text-center" style={{ textAlign: 'center', padding: '110px 0px' }}>
                        <Button
                          type={disable ? '' : 'primary'}
                          size="small"
                          style={{ width: '115px' }}
                          onClick={addSelected}
                          disabled={disable}
                        >
                          Add &#10097;&#10097;
                        </Button>
                        <Button
                          type="secondary"
                          size="small"
                          style={{ marginTop: '10px' }}
                          onClick={removeSelected}
                          disabled={disable2}
                        >
                          &#10096;&#10096; Remove
                        </Button>
                      </div>
                    </Col>
                    <Col sm={10} xs={24}>
                      <UserTableStyleWrapper>
                        <p>Users Added</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            id="myTable"
                            style={{ height: '300px' }}
                            scroll={{ y: 240 }}
                            rowSelection={rowSelection2}
                            columns={usersTableColumns}
                            dataSource={selectedUsers}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search User"
                          style={{ width: '50%', height: '80%', float: 'right', marginTop: '10px' }}
                          id="myInput2"
                          // onKeyUp={() => {
                          //   filterFunction('myTable', 'myInput2');
                          // }}
                          onKeyUp={e => {
                            searchTable2(e);
                          }}
                        />
                      </UserTableStyleWrapper>
                    </Col>
                  </Row>
                  <div className="sDash-button-grp">
                    <Button
                      className="btn-signin"
                      htmlType="submit"
                      type={loading ? '' : 'primary'}
                      size="large"
                      disabled={loading}
                    >
                      {loading2 ? <Spin size="medium" /> : 'Create'}
                    </Button>
                  </div>
                </Form>
                <div>
                  <p className="danger text-center" style={{ color: 'red', marginTop: '10px' }}>
                    {flag2 ? error : ''}
                  </p>
                </div>
              </Cards>
            </ExportStyleWrap>
          </Col>
        </Row>
      </Main>
    </>
  );
};

export default Dashboard;

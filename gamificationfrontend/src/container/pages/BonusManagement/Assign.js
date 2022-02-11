import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Spin, Form, Alert, Radio } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory, useParams } from 'react-router-dom';
import _ from 'lodash';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';

const Assign = () => {
  const { id } = useParams();
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
  const [groups, setGroups] = useState([]);
  const [selectedGroups, setSelectedGroups] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
  const [reference2, setReference2] = useState([]);
  const [users, setUsers] = useState([]);
  const [assign, setAssign] = useState('');
  const [showButton, setShowButton] = useState(false);
  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
    selectedRowKeys1: 0,
    selectedRows1: [],
  });

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
    let user = [];
    let groups = [];
    selectedUsers.map(users => {
      user.push(users.key.toString());
    });
    selectedGroups.map(group => {
      groups.push(group.key.toString());
    });
    setLoading2(true);
    const user2 = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const assignValues = {
      userIDs: user,
      groupIDs: groups,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(assignValues));
    const URL = `${api}surveyManagement/assignSurvey/${id}`;
    var config = {
      method: 'post',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user2.accessToken}`,
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
            pathname: '/admin/survey',
            state: { detail: `Survey Assigned Successfully` },
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
    getAllUsers();
    getAllGroups();
  }, []);

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

  const rowSelection3 = {
    onChange: (selectedRowKeys, selectedRows) => {
      setDisable(false);
      setDisable2(true);
      if (selectedRowKeys.length === 0) {
        setDisable(true);
      }
      setState({ ...state, selectedRowKeys, selectedRows });
    },
  };

  const rowSelection4 = {
    onChange: (selectedRowKeys1, selectedRows1) => {
      setDisable(true);
      setDisable2(false);
      if (selectedRowKeys1.length === 0) {
        setDisable2(true);
      }
      setState({ ...state, selectedRowKeys1, selectedRows1 });
    },
  };

  const getAllGroups = () => {
    const user = getItem('user');
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
        setGroups(groups);
        console.log(groups);
        let groupsTableData = [];
        groups.map(group => {
          const { groupId, groupName, groupInAnySession } = group;
          if (group.groupStatus === true) {
            return groupsTableData.push({
              key: groupId,
              name: groupName,
            });
          }
        });
        setReference(groupsTableData);
        setAllGroups(groupsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const groupsTableData2 = [];
  const groupsTableData = [];

  const groupsTableColumns = [
    {
      title: 'Group Name',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const addSelected2 = () => {
    console.log(state.selectedRowKeys);
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys.map(key => {
      groups.map(group => {
        console.log(group);
        if (group.groupId === key) {
          groupsTableData2.push({
            key: group.groupId,
            name: group.groupName,
          });
        }
      });
    });
    let merged = groupsTableData2.concat(selectedGroups);
    setSelectedGroups(merged);
    setReference2(merged);
    const sorted = _.differenceBy(allGroups, groupsTableData2, 'key');
    setAllGroups(sorted);
    setReference(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const removeSelected2 = () => {
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys1.map(key => {
      groups.map(group => {
        if (group.groupId === key) {
          groupsTableData.push({
            key: group.groupId,
            name: group.groupName,
          });
        }
      });
    });
    let merged = groupsTableData.concat(allGroups);
    setAllGroups(merged);
    setReference(merged);
    const sorted = _.differenceBy(selectedGroups, groupsTableData, 'key');
    setSelectedGroups(sorted);
    setReference2(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const searchTable3 = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setAllGroups(filteredData);
  };

  const searchTable4 = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference2.filter(entry => entry.name.toLowerCase().includes(currValue));
    setSelectedGroups(filteredData);
  };

  useEffect(() => {
    if (selectedUsers.length > 1 || selectedGroups.length > 0) {
      setLoading(false);
    } else {
      setLoading(true);
    }
  }, [selectedUsers, selectedGroups]);

  const selectAssign = e => {
    setAssign(e.target.value);
    setShowButton(true);
  };

  return (
    <>
      <PageHeader title="Assign Bonus Questions" />
      <Main>
        <Row gutter={25}>
          <Col sm={24} xs={24}>
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
                  <Radio.Group onChange={selectAssign} style={{ marginBottom: '20px' }}>
                    <Radio value="users">Individual Users</Radio>
                    <Radio value="groups">Groups</Radio>
                  </Radio.Group>
                  {assign === 'users' ? (
                    <Row gutter={25}>
                      <Col sm={10} xs={24}>
                        <UserTableStyleWrapper>
                          <p>Select Users</p>
                          <TableWrapper className="table-responsive">
                            <Table
                              style={{ height: '300px' }}
                              id="allUser"
                              rowSelection={rowSelection}
                              scroll={{ y: 240, x: true }}
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
                              scroll={{ y: 240, x: true }}
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
                  ) : (
                    ''
                  )}
                  {assign === 'groups' ? (
                    <Row gutter={25}>
                      <Col sm={10} xs={24}>
                        <UserTableStyleWrapper>
                          <p>Select Groups</p>
                          <TableWrapper className="table-responsive">
                            <Table
                              style={{ height: '200px' }}
                              id="allGroup"
                              scroll={{ y: 180 }}
                              rowSelection={rowSelection3}
                              columns={groupsTableColumns}
                              dataSource={allGroups}
                              pagination={false}
                            />
                          </TableWrapper>
                          <Input
                            placeholder="Search Group"
                            style={{ width: '50%', float: 'right', marginTop: '50px' }}
                            id="myInput"
                            onKeyUp={e => {
                              searchTable3(e);
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
                            onClick={addSelected2}
                            disabled={disable}
                          >
                            Add &#10097;&#10097;
                          </Button>
                          <Button
                            type="secondary"
                            size="small"
                            style={{ width: '115px' }}
                            style={{ marginTop: '10px' }}
                            onClick={removeSelected2}
                            disabled={disable2}
                          >
                            &#10096;&#10096; Remove
                          </Button>
                        </div>
                      </Col>
                      <Col sm={10} xs={24}>
                        <UserTableStyleWrapper>
                          <p>Selected Groups</p>
                          <TableWrapper className="table-responsive">
                            <Table
                              id="myTable"
                              style={{ height: '200px' }}
                              scroll={{ y: 180 }}
                              rowSelection={rowSelection4}
                              columns={groupsTableColumns}
                              dataSource={selectedGroups}
                              pagination={false}
                            />
                          </TableWrapper>
                          <Input
                            placeholder="Search Group"
                            style={{ width: '50%', float: 'right', marginTop: '50px' }}
                            id="myInput2"
                            onKeyUp={e => {
                              searchTable4(e);
                            }}
                          />
                        </UserTableStyleWrapper>
                      </Col>
                    </Row>
                  ) : (
                    ''
                  )}

                  <div className="sDash-button-grp">
                    {showButton ? (
                      <Button
                        className="btn-signin"
                        htmlType="submit"
                        type={loading ? '' : 'primary'}
                        size="large"
                        disabled={loading}
                      >
                        {loading2 ? <Spin size="medium" /> : 'Assign'}
                      </Button>
                    ) : (
                      ''
                    )}
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

export default Assign;

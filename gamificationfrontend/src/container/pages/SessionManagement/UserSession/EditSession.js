import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Input, Form, Spin, Alert, Modal } from 'antd';
import { PageHeader } from '../../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../../styled';
import { UserTableStyleWrapper } from '../../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../../components/cards/frame/cards-frame';
import { Button } from '../../../../components/buttons/buttons';
import Heading from '../../../../components/heading/heading';
import { useHistory, useParams } from 'react-router-dom';
import _ from 'lodash';
import axios from 'axios';
import { getItem } from '../../../../utility/localStorageControl';
import {useSelector} from 'react-redux';

const EditSession = () => {
  const { id } = useParams();
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [disable, setDisable] = useState(true);
  const [disable2, setDisable2] = useState(true);
  const [reference, setReference] = useState([]);
  const [reference2, setReference2] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [name, setName] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [minDate, setMinDate] = useState('');
  const [endminDate, setendMinDate] = useState('');
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [users, setUsers] = useState([]);
  const [flag, setFlag] = useState(false);
  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
    selectedRowKeys1: 0,
    selectedRows1: [],
  });
  const user = useSelector(state=> state?.auth?.user);
  const [country, setCountry] = useState([]);

  const handleOk = () => {
    setIsModalVisible(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

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
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let userId = [];
    selectedUsers.map(user => {
      userId.push(user.key.toString());
    });
    const sessionValues = {
      sessionName: values.sessionName,
      startDateandTime: values.startDateTime,
      endDateandTime: values.endDateTime,
      userIds: userId,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(sessionValues));
    data.append('country', country);
    const URL = `${api}gameSessionUserManagement/modifyGameUserSession/${id}`;
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
        setLoading2(false);
        const result = response.data;
        console.log(result);
        if (result.code.includes('406')) {
          setShowAlert(true);
          setAlertText('Session with same name already exists!');
          setAlertType('danger');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else if (result.code.includes('400')) {
          setIsModalVisible(true);
        } else {
          history.push({
            pathname: '/admin/sessions/user',
            state: { detail: `${values.sessionName} Updated Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

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
        getAllUsers(countryTemp);
      })
      .catch(function(error) {
        console.log(error);
      });
  }

  const handleChange = e => {
    setName(e.target.value);
  };
  const handleChange2 = e => {
    setStartDate(e.target.value);
    let arr = e.target.value.split('T');
    let ss = arr[1].split(':');
    let dd = parseInt(addZero(ss[0])) + 4;
    console.log(dd);
    let time = addZero(dd).toString() + ':' + addZero(ss[1]);
    let dateTime = arr[0] + 'T' + time;
    console.log(dateTime);
    //console.log(arr);
    setendMinDate(dateTime);
  };
  const handleChange3 = e => {
    setEndDate(e.target.value);
  };

  function addZero(i) {
    if (i < 10) {
      i = '0' + i;
    }
    return i;
  }

  useEffect(() => {
    var now = new Date();
    let date = now.toISOString().substring(0, 10);
    let hour = addZero(now.getHours());
    let minute = addZero(now.getMinutes());
    let time = hour + ':' + minute;
    let dateAndTime = date.concat(`T${time}`);
    setMinDate(dateAndTime);
    //getAllUsers();
    getOneSession();
    if(user.role == '"ADMIN"'){
      console.log(user.role  + "first");
      getCurrentUserCountry();  
    }
  }, []);

  const getAllUsers = (country) => {
    const api = process.env.REACT_APP_BACKEND_API;
    console.log(country);
    const URL = `${api}userManagement/usersInCountry/${country}`;
    console.log(URL);

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
        console.log(rev);
        setUsers(rev);
        rev.map(user => {
          const { id, name, imageKey, email, status, user_In_Any_Session } = user;
          if (status === true || user_In_Any_Session !== true) {
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
        console.log(usersTableData);
        setReference(usersTableData);
        setAllUsers(usersTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getOneSession = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}gameSessionUserManagement/gameUserSession/${id}`;
    console.log(id);
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
        console.log(result);
        form.setFieldsValue({
          sessionName: result.sessionName,
          startDateTime: result.startTime,
          endDateTime: result.endTime,
        });
        setName(result.sessionName);
        getAllUsers(result.countryName);
        setCountry(result.countryName);
        setStartDate(result.startTime);
        setEndDate(result.endTime);
        setMinDate(result.startTime);
        setendMinDate(result.startTime);
        console.log(result.getUsersInSession);
        result.getUsersInSession.map(user => {
          usersTableData2.push({
            key: user.id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      user.avatharKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : user.avatharKey
                    }
                    alt="userImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {user.userName}
                  </Heading>
                </figcaption>
              </div>
            ),
            email: user.userEmail,
          });
        });
        setSelectedUsers(usersTableData2);
        setReference2(usersTableData2);
        setFlag(true);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  useEffect(() => {
    const sorted = _.differenceBy(allUsers, selectedUsers, 'key');
    setAllUsers(sorted);
    setReference(sorted);
  }, [flag]);

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
    if (selectedUsers.length > 1 && name.length > 0 && startDate.length > 0 && endDate.length > 0) {
      setLoading(false);
    } else {
      setLoading(true);
    }
  }, [selectedUsers, name, startDate, endDate]);

  return (
    <>
      <PageHeader title="Update Session" />
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
                <Form name="addgroup" form={form} onFinish={handleSubmit} layout="vertical">
                  <Form.Item
                    name="sessionName"
                    rules={[{ message: 'Please enter session name', required: true }]}
                    label="Session Name"
                  >
                    <Input
                      placeholder="Session Name"
                      onKeyPress={specialCharacter}
                      maxlength="40"
                      onChange={handleChange}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Form.Item
                    name="startDateTime"
                    rules={[{ message: 'Please enter start date time', required: true }]}
                    label="Start Date Time"
                  >
                    <Input
                      placeholder="Start Date Time"
                      onChange={handleChange2}
                      type="datetime-local"
                      min={minDate}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Form.Item
                    name="endDateTime"
                    rules={[{ message: 'Please enter start date time', required: true }]}
                    label="End Date Time"
                  >
                    <Input
                      placeholder="End Date Time"
                      onChange={handleChange3}
                      type="datetime-local"
                      min={endminDate}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Row gutter={25}>
                    <Col sm={10} xs={24}>
                      <UserTableStyleWrapper>
                        <p>Select Users</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            style={{ height: '200px' }}
                            id="allGroup"
                            scroll={{ y: 180 }}
                            rowSelection={rowSelection}
                            columns={usersTableColumns}
                            dataSource={allUsers.filter(i=>!selectedUsers.some(x=>x.key==i.key))}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search Group"
                          style={{ width: '50%', float: 'right', marginTop: '50px' }}
                          id="myInput"
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
                          style={{ width: '115px' }}
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
                        <p>Selected Users</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            id="myTable"
                            style={{ height: '200px' }}
                            scroll={{ y: 180 }}
                            rowSelection={rowSelection2}
                            columns={usersTableColumns}
                            dataSource={selectedUsers}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search Group"
                          style={{ width: '50%', float: 'right', marginTop: '50px' }}
                          id="myInput2"
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
                      type={loading ? '' : 'warning'}
                      size="large"
                      disabled={loading}
                    >
                      {loading2 ? <Spin size="medium" /> : 'Update Session'}
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
      <Modal title="Error In Creating" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
        <p>Selected Group Members Already already exists in other Session! :)</p>
      </Modal>
    </>
  );
};

export default EditSession;

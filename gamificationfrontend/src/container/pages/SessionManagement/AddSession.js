import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Input, Select, Form, Spin, Alert, Modal } from 'antd';
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

const AddSession = () => {
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [disable, setDisable] = useState(true);
  const [disable2, setDisable2] = useState(true);
  const [reference, setReference] = useState([]);
  const [reference2, setReference2] = useState([]);
  const [selectedGroups, setSelectedGroups] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
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
  const [groups, setGroups] = useState([]);
  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
    selectedRowKeys1: 0,
    selectedRows1: [],
  });
  const user = useSelector(state=> state?.auth?.user);
  const [admins, setAdmins] = useState([]);
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
    let groupId = [];
    selectedGroups.map(group => {
      groupId.push(group.key.toString());
    });
    const sessionValues = {
      sessionName: values.sessionName,
      startDateandTime: values.startDateTime,
      endDateandTime: values.endDateTime,
      groupIds: groupId,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(sessionValues));
    data.append('country',country);
    console.log(country);
    const URL = `${api}gameSessionManagement/createGameSession`;
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
          setAlertText('Session with same name already exists!');
          setAlertType('danger');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else if (result.code.includes('400')) {
          setIsModalVisible(true);
        } else {
          history.push({
            pathname: '/admin/session',
            state: { detail: `${values.sessionName} Added Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };
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
    var now = new Date();
    let date = now.toISOString().substring(0, 10);
    let hour = addZero(now.getHours());
    let minute = addZero(now.getMinutes());
    let time = hour + ':' + minute;
    let dateAndTime = date.concat(`T${time}`);
    setMinDate(dateAndTime);
   // getAllGroups();
   if(user.role == '"ADMIN"'){
    console.log(user.role  + "first");
    getCurrentUserCountry();  
  }else{
    getAllGroups();
  }
  }, []);

//   useEffect(() => {
           
//     if(user.role == '"ADMIN"'){
//       console.log(user.role  + "first");
//       getCurrentUserCountry();  
//     }
    
// }, []);

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
      setCountry(countryTemp);
      console.log(countryTemp + "now");
      getAllGroupsInCountry(countryTemp);
    })
    .catch(function(error) {
      console.log(error);
    });
}

const getAllGroupsInCountry = (countryTemp) => {
  console.log(countryTemp);
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
      setGroups(groups);
      console.log(groups);
      let groupsTableData = [];
      groups.map(group => {
        const { groupId, groupName, groupInAnySession } = group;
        if (group.groupStatus === true && groupInAnySession !== true) {
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

const selectedFilterAdmin = id => {
  const user = admins.find(i=>i.id== id);
  setCountry(user.country);
  if(user.country == "ALL COUNTRIES"){
    getAllGroups();
  }else{
    getAllGroupsInCountry(user.country);
  }
}

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
          if (group.groupStatus === true && groupInAnySession !== true) {
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

  const addSelected = () => {
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

  const removeSelected = () => {
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
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setAllGroups(filteredData);
  };

  const searchTable2 = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference2.filter(entry => entry.name.toLowerCase().includes(currValue));
    setSelectedGroups(filteredData);
  };

  useEffect(() => {
    if (selectedGroups.length > 0 && name.length > 0 && startDate.length > 0 && endDate.length > 0) {
      setLoading(false);
    } else {
      setLoading(true);
    }
  }, [selectedGroups, name, startDate, endDate]);

  return (
    <>
      <PageHeader title="Create Session" />
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
                        <p>Select Groups</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            style={{ height: '200px' }}
                            id="allGroup"
                            scroll={{ y: 180 }}
                            rowSelection={rowSelection}
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
                        <p>Selected Groups</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            id="myTable"
                            style={{ height: '200px' }}
                            scroll={{ y: 180 }}
                            rowSelection={rowSelection2}
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
                      {loading2 ? <Spin size="medium" /> : 'Create Session'}
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

export default AddSession;

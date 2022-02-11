import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Select,Drawer, Modal, Spin, Alert, Form } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { NavLink, useHistory } from 'react-router-dom';
import AddUser from './AddUser';
import axios from 'axios';
// import { getItem } from '../../../utility/localStorageControl';
import { Link } from 'react-router-dom';
import {useSelector} from 'react-redux'



const Dashboard = () => {
  const history = useHistory();
  const [selectFilter, setSelectFilter] = useState('');
  const [show, setShow] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showModal2, setShowModal2] = useState(false);
  const [allUsers, setAllUsers] = useState([]);
  const [reference, setReference] = useState([]);
  const [singleUser, setSingleUser] = useState(null);
  const [showModal3, setShowModal3] = useState(false);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('success');
  const [showExcel, setShowExcel] = useState(false);
  const [check, setCheck] = useState('');
  const [visible, setVisible] = useState(false);
  const [usersFromCountry, setUsersFromCountry] = useState([]);
  const [countries, setCountries] = useState([]);
  const [selectedCountry, setSelectedCountry] = useState();
  const [admins, setAdmins] = useState([]);
  const [countryTemp, setCountryTemp] = useState([]);

const user = useSelector(state=> state?.auth?.user);
// console.log(user);

  useEffect(() => {
    getAllAdmins();
  }, []);

  useEffect(() => {
           
            if(user.role == '"ADMIN"'){
              console.log(user.role  + "first");
              getCurrentUserCountry();
              //getAllUsersInCountry();
            }else if(user.role == '"SuperAdmin"'){
              getAllUsers();
            }
            //new end
  }, []);

  // useEffect(() => {
  //   getCurrentUserCountry();
  // }, []);

  const showDrawer = user => {
    setSingleUser(user);
    setVisible(true);
  };

  const onClose = () => {
    setVisible(false);
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
        setCountryTemp(result.country);
        const countryTemp = result.country;
        console.log(countryTemp + "now");
        getAllUsersInCountry(countryTemp);
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

  const getAllUsers = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/users`;
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
        const rev = result.users.reverse();
        console.log(rev);
        let usersTableData = [];
        rev.map(user => {
          const { id, name, imageKey, email, country, roles, status, user_In_Group } = user;
          setCheck(status);
          return usersTableData.push({
            key: id,
            //name: name,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px', cursor: 'pointer' }}
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
            modify: (
              <>
                <Button
                  className="btn-icon"
                  shape="circle"
                  onClick={() => editUser(user)}
                  disabled={status === true ? false : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            country: country,
            role: (
              <>
                {roles.map((rol, index) => {
                  if (index < roles.length - 1) {
                    return <p style={{ display: 'inline-block', marginLeft: '5px' }}>{rol},</p>;
                  }
                  return <p style={{ display: 'inline-block', marginLeft: '5px' }}>{rol}</p>;
                })}
              </>
            ),details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(user)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      checked={status === true ? true : false}
                      onChange={e => updateStatus(e, email, name, status, user_In_Group)}
                      style={{ marginLeft: '30px' }}
                    />
                  </p>
                </div>
              </>
            ),
          });
        });
        setReference(usersTableData);
        setAllUsers(usersTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getAllUsersInCountry = (countryTemp) => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/usersInCountry/${countryTemp}`;
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
        const rev = result.users.reverse();
        console.log(rev);
        let usersTableData = [];
        rev.map(user => {
          const { id, name, imageKey, email, country, roles, status, user_In_Group } = user;
          setCheck(status);
          return usersTableData.push({
            key: id,
            //name: name,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px', cursor: 'pointer' }}
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
            modify: (
              <>
                <Button
                  className="btn-icon"
                  shape="circle"
                  onClick={() => editUser(user)}
                  disabled={status === true ? false : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            country: country,
            role: (
              <>
                {roles.map((rol, index) => {
                  if (index < roles.length - 1) {
                    return <p style={{ display: 'inline-block', marginLeft: '5px' }}>{rol},</p>;
                  }
                  return <p style={{ display: 'inline-block', marginLeft: '5px' }}>{rol}</p>;
                })}
              </>
            ),details: (
              <>
                <Button className="btn-icon" to="#" shape="circle" onClick={() => showDrawer(user)}>
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      checked={status === true ? true : false}
                      onChange={e => updateStatus(e, email, name, status, user_In_Group)}
                      style={{ marginLeft: '30px' }}
                    />
                  </p>
                </div>
              </>
            ),
          });
        });
        setReference(usersTableData);
        setAllUsers(usersTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateStatus = (e, email, name, userStatus, user_In_Group) => {
    if (user_In_Group === true) {
      setShowAlert(true);
      setAlertText(`${name} already present in a group`);
      setAlertType('error');
      setTimeout(() => {
        setShowAlert(false);
        setAlertType('success');
      }, 3000);
    } else {
      const api = process.env.REACT_APP_BACKEND_API;
      const URL = `${api}userManagement/updateStatus/${email}`;
      const userValues = {
        userStatus: e,
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
          setShowAlert(true);
          setAlertText(`${name} status successfully Updated`);
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
          const result = response.data;
          if(user.role == '"ADMIN"'){
            getCurrentUserCountry();
          }else{
           // getAllUsers();
            getAllUsersInCountry(countryTemp.countryName);
          }
          
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  };

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

  const usersTableColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a, b) => {
        return a.name.props.children[1].props.children.props.children
          .toString()
          .localeCompare(b.name.props.children[1].props.children.props.children.toString());
      },
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
      sorter: (a, b) => {
        return a.email.toString().localeCompare(b.email.toString());
      },
    },
    {
      title: 'Modify',
      dataIndex: 'modify',
      key: 'modify',
      width: 130,
      align: 'center',
    },
    {
      title: 'Country',
      dataIndex: 'country',
      key: 'country',
      width: 130,
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
    },
    {
      title: 'Details',
      dataIndex: 'details',
      key: 'details',
      width: 90,
      align: 'center',
    },
    {
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
    },
  ];

  const selectedFilterAdmin = id => {
    const user = admins.find(i=>i.id== id);

      if(user.country == "ALL COUNTRIES"){
        getAllUsers();
      }else{
        getAllUsersInCountry(user.country);
      }
  }

  const selectedFilter = e => {
    setShow(true);
    setSelectFilter(e);
  };

  const filterFunction = e => {
    var selectedFilter = selectFilter;
    var x = 0;
    if (selectedFilter === 'name') {
      x = 0;
    } else if (selectFilter === 'role') {
      x = 4;
    } else if (selectFilter === 'email') {
      x = 1;
    } else if (selectFilter === 'country') {
      x = 3;
    }
    if (x === 0) {
      const currValue = e?.target.value.toLowerCase();
      const filteredData = reference.filter(entry =>
        entry.name.props.children[1].props.children.props.children.toLowerCase().includes(currValue),
      );
      setAllUsers(filteredData);
    }
    if (x === 1) {
      const currValue = e?.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.email.toLowerCase().includes(currValue));
      setAllUsers(filteredData);
    }
    if (x === 4) {
      const currValue = e?.target.value.toLowerCase();
      const filteredData = reference.filter(entry =>
        entry.role.props.children[0].props.children.toLowerCase().includes(currValue),
      );
      setAllUsers(filteredData);
    }
    if (x === 3) {
      const currValue = e?.target.value.toLowerCase();
      const filteredData = reference.filter(entry => entry.countries.toLowerCase().includes(currValue));
      setAllUsers(filteredData);
    }
  };

  const addUser = () => {
    setSingleUser(null);
    setShowModal(true);
  };
  const addExcel = () => {
    //setFlag(false);
    let role = user.role.replace(/['"]+/g, '');
    if (role.toString().toLowerCase() === 'superadmin') {
      setShowExcel(true);
    } else {
      setShowExcel(false);
    }
    setShowModal2(true);
  };
  const handleCancel = () => {
    // getAllUsers();
    if(user.role == '"ADMIN"'){
      getCurrentUserCountry();
    }else if(user.role == '"SuperAdmin"'){
      getAllUsersInCountry(countryTemp.country);
    }
    setShowModal2(false);
    setShowModal(false);
  };

  const editUser = user => {
    if(user.role == '"ADMIN"'){
      getCurrentUserCountry();
    }else if(user.role == '"SuperAdmin"'){
      getAllUsersInCountry(countryTemp.country);
    } 
    
    setSingleUser(user);
    setShowModal(true);
  };

  const addEditUser = val => {
    setShowAlert(true);
    setAlertText(val);
    setTimeout(() => {
      setShowAlert(false);
    }, 3000);
  };

  const [form] = Form.useForm();
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [flag3, setFlag3] = useState(true);
  const [loading, setLoading] = useState(false);
  const [file, setFile] = useState(null);

  const handleSubmit = values => {
    setLoading(true);
    const api = process.env.REACT_APP_BACKEND_API;
    let role = user.role.replace(/['"]+/g, '');
    const URL =
      role === 'SuperAdmin' ? `${api}userManagement/superAdminBulkUpload` : `${api}userManagement/adminBulkUpload`;
    var data = new FormData();
    data.append('file', file);
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
        console.log(response.data);
        const myArr = response.data.success.split(':');
        const myArr2 = response.data.total.split(':');
        setError(`${myArr[1]} User Added Out of ${myArr2[1]}`);
        setFlag(response.data.errorLogGenereted);
        // if (ss[1] !== undefined && ss[1].includes('No')) {
        //   setFlag3(false);
        // } else {
        //   setFlag3(true);
        // }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleChange = event => {
    const fileToUpload = event.target.files[0];
    setFile(fileToUpload);
  };

  const download = () => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/download/errorLog.xlsx`;
    var config = {
      method: 'get',
      url: URL,
      responseType: 'blob',
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'error.xlsx'); //or any other extension
        document.body.appendChild(link);
        link.click();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <>
      <PageHeader title="User Management Dashboard" />
      <Main>
        <Row gutter={25}>
          <Col sm={24} xs={24}>
            <ExportStyleWrap>
              <Cards headless>
                <Row gutter={25} style={{ marginBottom: '10px' }}>
                  <Col sm={16} xs={24}>
                    <Button className="btn-export" type="primary" size="large" onClick={addUser}>
                      Add User
                    </Button>
                    <AddUser
                      title={singleUser === null ? 'Add User' : 'Update User'}
                      wrapClassName="sDash_export-wrap"
                      visible={showModal}
                      footer={null}
                      onCancel={handleCancel}
                      onAddEditUser={addEditUser}
                      singleUser={singleUser}
                      getAllUsers={getAllUsers}
                      getCurrentUserCountry={getCurrentUserCountry}
                    />
                    <Button
                      className="btn-export"
                      type="primary"
                      style={{ marginLeft: '20px' }}
                      size="large"
                      onClick={addExcel}
                    >
                      Bulk User Upload
                    </Button>
                  </Col>
                 {user?.role ===   '"SuperAdmin"'? <Col>
                  <Select 
                  placeholder="Choose Admin"  onChange={selectedFilterAdmin} >            
                   {admins.map(user => (<Option value={user.id}>{user.name}</Option>))}
                  </Select>
                  </Col>:null}
                  <Col sm={8} xs={24} className="margin">
                    <Select
                      // defaultValue="name"
                      placeholder="Search By"
                      style={{ marginRight: '10px' }}
                      onChange={selectedFilter}
                    >
                      <Option value="name">Name</Option>
                      <Option value="role">Role</Option>
                      <Option value="email">Email</Option>
                      <Option value="country">Country</Option>
                    </Select>
                    <Input
                      placeholder="Search Here"
                      style={{ width: '50%', height: '80%' }}
                      id="myInput"
                      onKeyUp={filterFunction}
                    />
                  </Col>
                </Row>
                <div
                  style={{
                    marginBottom: '10px',
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type={alertType} showIcon /> : ''}
                </div>
                <UserTableStyleWrapper>
                  <TableWrapper className="table-responsive">
                    <Table
                      id="myTable"
                      columns={usersTableColumns}
                      dataSource={allUsers}
                      scroll={{ y: 340, x: true }}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: allUsers.length,
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
        title="Upload Excel "
        wrapClassName="sDash_export-wrap"
        visible={showModal2}
        footer={null}
        onCancel={handleCancel}
      >
        {flag ? (
          <>
            <div>
              <p className="danger text-center" style={{ color: 'green' }}>
                {error}
              </p>
            </div>
            {flag3 ? (
              <div className="text-center">
                <Button className="btn-signin" type="danger" size="large" onClick={download}>
                  Download Error Log
                </Button>
              </div>
            ) : (
              ''
            )}
          </>
        ) : (
          <>
            <div className="text-center">
              {showExcel ? (
                <Button className="btn-signin" type="primary" size="large" style={{ marginBottom: '20px' }}>
                  <Link to="/User_list_Superadmin.xlsx" className="btn-signin" target="_blank" download>
                    Download Sample Excel
                  </Link>
                </Button>
              ) : (
                <Button className="btn-signin" type="primary" size="large" style={{ marginBottom: '20px' }}>
                  <Link to="/User_list_Admin.xlsx" className="btn-signin" target="_blank" download>
                    Download Sample Excel
                  </Link>
                </Button>
              )}
            </div>
            <Form name="addexcel" form={form} onFinish={handleSubmit} layout="vertical">
              <Form.Item name="file" rules={[{ required: true }]} label="Upload Excel">
                <Input placeholder="Choose File" type="file" accept=".xlsx, .xls" onChange={handleChange} />
              </Form.Item>
              <p className="upload-tip">Supports files in .xlsx, .xls format</p>
              <div className="sDash-button-grp">
                <Button className="btn-signin" htmlType="submit" type="primary" size="large" disabled={loading}>
                  {loading ? <Spin size="medium" /> : 'Upload'}
                </Button>
              </div>
            </Form>
          </>
        )}
      </Modal>
      {singleUser === null ? (
        ''
      ) : (
        <Drawer
          width={440}
          title={singleUser.name}
          placement="right"
          closable={false}
          onClose={onClose}
          visible={visible}
        >     
          <div>Id : {singleUser.id}</div>     
          <div>Region : {singleUser.region}</div>
          <div>State : {singleUser.state}</div>
          <div>City : {singleUser.city}</div>
          <div>Latitude : {singleUser.cityLatitude}</div>
          <div>Longitude : {singleUser.cityLongitude}</div>

        </Drawer>
      )}
    </>
  );
};

export default Dashboard;

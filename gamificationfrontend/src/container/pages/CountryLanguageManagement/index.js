import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Form, Collapse, Modal, Alert } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import AddCountry from './AddCountry';
import { getItem } from '../../../utility/localStorageControl';

const Dashboard = () => {
  const history = useHistory();
  const [selectFilter, setSelectFilter] = useState('');
  const [countryLanguagesData, setCountryLanguagesData] = useState([]);
  const [reference, setReference] = useState([]);
  const countryLanguagesTableData = [];
  const [showModal, setShowModal] = useState(false);
  const [showModal2, setShowModal2] = useState(false);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [languageId, setLanguageId] = useState(null);
  const [singleCountry, setSingleCountry] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [languages, setLanguages] = useState(null);
  const [form] = Form.useForm();
  const handleSubmit = values => {};

  let dataTable = [];

  useEffect(() => {
    getAllCountries();
  }, []);

  const getAllCountries = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
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
        let countryLanguagesTableData = [];
        countries.map(countryLanguage => {
          const { countryID, countryName, countryStatus, languages } = countryLanguage;

          return countryLanguagesTableData.push({
            key: countryID,
            name: countryName,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      onChange={e => updateCountryStatus(e, countryID, countryName)}
                      defaultChecked={countryStatus}
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
                  disabled={countryStatus === true ? false : true}
                  onClick={() => editCountry(countryLanguage)}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
            children: [
              {
                key: <>{languages.map(language => language.languageId)}</>,
                name: (
                  <>
                    {languages.map((language, index) => {
                      if (index < languages.length - 1) {
                        return <div style={{ marginLeft: '40px' }}>{language.languageName}</div>;
                      }
                      return <div style={{ marginLeft: '40px' }}>{language.languageName}</div>;
                    })}
                    {/* {languages.map(language => (
                  <div>{language.name}</div>
                ))} */}
                  </>
                ),
                enableDisable: (
                  <>
                    {languages.map(language => (
                      <div className="notification-list-single">
                        <p>
                          <Switch
                            disabled={countryStatus === true ? false : true}
                            onChange={e => updateLanguageStatus(e, language.languageId, language.languageName)}
                            defaultChecked={language.languageStatus === true ? true : false}
                            style={{ marginLeft: '30px' }}
                          />
                        </p>
                      </div>
                    ))}
                  </>
                ),
                modify: (
                  <>
                    {languages.map(language => (
                      <div style={{ marginTop: '5px' }}>
                        <Button
                          className="btn-icon"
                          to="#"
                          shape="circle"
                          disabled={countryStatus === true ? (language.languageStatus === true ? false : true) : true}
                          onClick={() => {
                            editLanguage(language);
                            setLanguages(languages);
                          }}
                        >
                          <FeatherIcon icon="edit" size={16} />
                        </Button>
                      </div>
                    ))}
                  </>
                ),
              },
            ],
          });
        });
        setCountryLanguagesData(countryLanguagesTableData);
        setReference(countryLanguagesTableData);
        // setGroupsData(groupsTableData);
        // setReference(groupsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const editCountry = country => {
    setSingleCountry(country);
    setShowModal(true);
  };

  const updateCountryStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}countrylanguageManagement/updateCountryStatus/${id}`;
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
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
        getAllCountries();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateLanguageStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}countrylanguageManagement/updateLanguageStatus/${id}`;
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
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
        getAllCountries();
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const countryLanguagesTableColumns = [
    {
      title: 'Country Name',
      dataIndex: 'name',
      key: 'name',
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
  ];

  // const selectedFilter = e => {
  //   setShow(true);
  //   setSelectFilter(e);
  // };

  const filterFunction = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setCountryLanguagesData(filteredData);
  };

  const addCountryLanguage = () => {
    setSingleCountry(null);
    setShowModal(true);
  };

  const handleCancel = () => {
    //getAllUsers();
    //setShowModal2(false);
    setShowModal(false);
  };

  const handleCancel2 = () => {
    setShowModal2(false);
  };

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const editLanguage = language => {
    setLanguageId(language.languageId);
    form.setFieldsValue({
      languageName: language.languageName,
    });
    setShowModal2(true);
  };

  const updateLanguage = values => {
    const found = languages.some(row => row.languageName.toLowerCase() === values.languageName.toLowerCase());
    if (found) {
      setFlag(true);
      setError('Language Already Exist!');
      setTimeout(() => {
        setFlag(false);
      }, 2000);
    } else {
      const user = getItem('user');
      const api = process.env.REACT_APP_BACKEND_API;
      const languageValues = {
        language: values.languageName,
      };
      var data = new FormData();
      data.append('userJson', JSON.stringify(languageValues));
      const URL = `${api}countrylanguageManagement/modifyLanguage/${languageId}`;
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
          setShowModal2(false);
          const result = response.data;
          setShowAlert(true);
          setAlertText(`${values.languageName} status successfully Updated`);
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
          getAllCountries();
        })
        .catch(function(error) {
          console.log(error);
        });
    }
  };

  const addEditCountry = val => {
    setShowAlert(true);
    setAlertText(val);
    setTimeout(() => {
      setShowAlert(false);
    }, 3000);
    getAllCountries();
  };

  return (
    <>
      <PageHeader title="Country Language Management Dashboard" />
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
                    <Button className="btn-export" type="primary" size="medium" onClick={addCountryLanguage}>
                      Add Country / Language
                    </Button>
                  </div>
                  <AddCountry
                    title={singleCountry === null ? 'Add Country/Language' : 'Edit Country/Language'}
                    wrapClassName="sDash_export-wrap"
                    visible={showModal}
                    footer={null}
                    onCancel={handleCancel}
                    onAddEditCountry={addEditCountry}
                    singleCountry={singleCountry}
                  />
                  <div>
                    {/* <Select
                      // defaultValue="name"
                      placeholder="Search By"
                      style={{ marginRight: '10px' }}
                      onChange={selectedFilter}
                    >
                      <Option value="name">Name</Option>
                      <Option value="countryLanguageStatus">CountryLanguage Status</Option>
                    </Select> */}
                    <Input
                      placeholder="Search Country Name"
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
                      columns={countryLanguagesTableColumns}
                      scroll={{ y: 340, x: true }}
                      dataSource={countryLanguagesData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: countryLanguagesData.length,
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
        title="Edit Language"
        wrapClassName="sDash_export-wrap"
        visible={showModal2}
        footer={null}
        onCancel={handleCancel2}
      >
        <Form name="EditLanguage" form={form} onFinish={updateLanguage} layout="vertical">
          <Form.Item
            name="languageName"
            label="Language Name"
            rules={[{ message: 'Please enter language name', required: true }]}
          >
            <Input placeholder="Language Name" onKeyPress={specialCharacter} maxlength="40" />
          </Form.Item>

          <div className="sDash-button-grp">
            <Button className="btn-signin" htmlType="submit" type="secondary" size="large">
              Update
            </Button>
          </div>
          <div>
            <p className="danger text-center" style={{ color: 'red' }}>
              {flag ? error : ''}
            </p>
          </div>
        </Form>
      </Modal>
    </>
  );
};

export default Dashboard;

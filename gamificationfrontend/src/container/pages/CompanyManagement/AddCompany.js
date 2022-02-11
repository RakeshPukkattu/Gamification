import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Spin, Form, Alert, Select } from 'antd';
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
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';

const AddCompany = () => {
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [disable, setDisable] = useState(true);
  const [disable2, setDisable2] = useState(true);
  const [groupName, setGroupName] = useState('');
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('');
  const [email, setEmail] = useState('');
  const [email2, setEmail2] = useState('');
  const [minDate, setMinDate] = useState('');
  const [endminDate, setendMinDate] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [selectedCountry, setSelectedCountry] = useState('');
  const [countries, setCountries] = useState([]);
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [pincode, setPincode] = useState('');
  const [primaryContact, setPrimaryContact] = useState('');
  const [secondaryContact, setSecondaryContact] = useState('');
  const [state, setState] = useState('');
  const [states, setStates] = useState([]);
  const [duration, setDuration] = useState('');
  const [sameContact, setSameContact] = useState(false);
  const [flag3, setFlag3] = useState(false);

  useEffect(() => {
    var config = {
      method: 'get',
      url: 'https://countriesnow.space/api/v0.1/countries/states',
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        console.log(result);
        setCountries(result.data);
      })
      .catch(function(error) {
        console.log(error);
      });
  }, []);

  const handleSubmit = values => {
    console.log(values);
    setLoading2(true);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const countryValues = values;
    var data = new FormData();
    data.append('userJson', JSON.stringify(countryValues));
    const URL = `${api}companyManagement/registerCompany`;
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
        if (result.code.includes('406')) {
          setShowAlert(true);
          setAlertText('Company with same name already exists!');
          setAlertType('danger');
          window.scrollTo(0, 0);
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/company',
            state: { detail: `${values.companyName} Added Successfully` },
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

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const handleEmail = e => {
    setEmail(e.target.value);
  };

  const handleEmail2 = e => {
    setEmail2(e.target.value);
  };

  function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  const checkEmail = e => {
    if (validateEmail(email)) {
      setFlag2(false);
    } else {
      setFlag2(true);
      setError('Email is not Valid');
    }
  };

  const checkEmail2 = e => {
    if (validateEmail(email2)) {
      setFlag2(false);
      if (validateEmail(email)) {
        //setLoading(false);
      }
    } else {
      setFlag2(true);
      setError('Email is not Valid');
    }
  };

  useEffect(() => {
    var now = new Date();
    let date = now.toISOString().substring(0, 10);
    setMinDate(date);
  }, []);

  const handleChange2 = e => {
    setStartDate(e.target.value);
    setendMinDate(e.target.value);
  };
  const handleChange3 = e => {
    setEndDate(e.target.value);
  };

  const handleChangeCountry = e => {
    //setState('');
    setSelectedCountry(e);
    setState('');
    countries.map(country => {
      if (country.name == e) {
        setStates(country.states);
      }
    });
  };

  const handleChangeState = e => {
    setState(e);
  };

  useEffect(() => {
    if (
      name === '' ||
      address === '' ||
      pincode === '' ||
      selectedCountry === '' ||
      primaryContact === '' ||
      secondaryContact === '' ||
      state === '' ||
      startDate === '' ||
      duration === '' ||
      email === ''
    ) {
      setLoading(true);
    } else {
      if (validateEmail(email)) {
        setLoading(false);
      }
      if (email == email2) {
        setLoading(true);
      }
      if (primaryContact == secondaryContact) {
        setLoading(true);
      }
    }
  }, [
    name,
    address,
    pincode,
    selectedCountry,
    primaryContact,
    secondaryContact,
    state,
    startDate,
    duration,
    email,
    email2,
  ]);

  const handleDuration = e => {
    setDuration(e);
  };

  useEffect(() => {
    if (primaryContact === secondaryContact && primaryContact !== '') {
      setSameContact(true);
      setError("Both Number can't be same");
    } else {
      setSameContact(false);
    }
  }, [primaryContact, secondaryContact]);

  useEffect(() => {
    if (email === email2 && email !== '') {
      setFlag3(true);
      setError("Both Email can't be same");
    } else {
      setFlag3(false);
    }
  }, [email, email2]);

  return (
    <>
      <PageHeader title="Create Company" />
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
                <Form name="addcompnay" form={form} onFinish={handleSubmit} layout="vertical">
                  <Form.Item
                    name="companyName"
                    rules={[{ message: 'Please enter company name', required: true }]}
                    label="Company Name"
                  >
                    <Input
                      placeholder="Company Name"
                      onChange={e => {
                        setName(e.target.value);
                        setFlag2(false);
                      }}
                      onKeyPress={specialCharacter}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Form.Item
                    name="address"
                    rules={[{ message: 'Please enter address', required: true }]}
                    label="Address"
                  >
                    <Input.TextArea
                      placeholder="Address"
                      onChange={e => {
                        setAddress(e.target.value);
                        setFlag2(false);
                      }}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>

                  <Form.Item
                    name="country"
                    rules={[{ message: 'Please select country', required: true }]}
                    label="Country"
                  >
                    <Select placeholder="Select Country" onChange={handleChangeCountry} style={{ width: '40%' }}>
                      {countries.map(country => (
                        <Option disabled={selectedCountry.includes('All') ? true : false} value={country.name}>
                          {country.name}
                        </Option>
                      ))}
                    </Select>
                  </Form.Item>
                  <Form.Item name="state" rules={[{ message: 'Please select state', required: true }]} label="State">
                    <Select placeholder="Select State" onChange={handleChangeState} style={{ width: '40%' }}>
                      {states.map(state => (
                        <Option value={state.name}>{state.name}</Option>
                      ))}
                    </Select>
                  </Form.Item>
                  <Form.Item
                    name="pincode"
                    rules={[{ message: 'Please enter pin code', required: true }]}
                    label="Pin Code"
                  >
                    <Input
                      placeholder="Pin Code"
                      onChange={e => {
                        setPincode(e.target.value);
                        setFlag2(false);
                      }}
                      maxlength="40"
                      type="number"
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  {/* <PhoneInput value="+918920781812" style={{ display: 'none' }} /> */}
                  <Form.Item name="primaryContact" label="Primary Contact No.">
                    <PhoneInput
                      inputStyle={{
                        width: '455px',
                        height: '45px',
                        fontSize: '14px',
                        paddingLeft: '48px',
                        borderRadius: '4px',
                      }}
                      country={'in'}
                      value={primaryContact}
                      onChange={phone => {
                        setPrimaryContact(phone);
                        setFlag2(false);
                      }}
                    />
                  </Form.Item>
                  <Form.Item label="Secondary Contact No." name="secondaryContact">
                    <PhoneInput
                      inputStyle={{
                        width: '455px',
                        height: '45px',
                        fontSize: '14px',
                        paddingLeft: '48px',
                        borderRadius: '4px',
                      }}
                      country={'in'}
                      value={secondaryContact}
                      onChange={phone => {
                        setSecondaryContact(phone);
                        setFlag2(false);
                      }}
                    />
                  </Form.Item>
                  <p className="danger" style={{ color: 'red', marginTop: '10px' }}>
                    {sameContact ? ` Error: ${error}` : ''}
                  </p>
                  {/* <Form.Item name="state" rules={[{ message: 'Please enter state', required: true }]} label="State">
                    <Input
                      placeholder="State"
                      onChange={e => {
                        setState(e.target.value);
                        setFlag2(false);
                      }}
                      maxlength="40"
                      style={{ width: '40%' }}
                    />
                  </Form.Item> */}
                  <Form.Item
                    name="startDate"
                    rules={[{ message: 'Please enter start date', required: true }]}
                    label="Start Date"
                  >
                    <Input
                      placeholder="Start Date"
                      onChange={handleChange2}
                      type="date"
                      min={minDate}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Form.Item
                    name="duration"
                    rules={[{ message: 'Please select duration', required: true }]}
                    label="Duration"
                  >
                    <Select placeholder="Select Duration" onChange={handleDuration} style={{ width: '40%' }}>
                      <Option value="ONEMONTH">1 month</Option>
                      <Option value="SIXMONTHS">6 months</Option>
                      <Option value="ONEYEAR">1 year</Option>
                      <Option value="TWOYEARS">2 years</Option>
                      <Option value="THREEYEARS">3 years</Option>
                      <Option value="FIVEYEARS">5 years</Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="primaryEmail" rules={[{ required: true }]} label="Primary Email ID">
                    <Input
                      placeholder="Primary Email"
                      required
                      onChange={handleEmail}
                      onBlur={checkEmail}
                      style={{ width: '40%' }}
                      id="email"
                    />
                  </Form.Item>
                  <Form.Item name="secondaryEmail" label="Secondary Email ID">
                    <Input
                      placeholder="Secondary Email"
                      onChange={handleEmail2}
                      onBlur={checkEmail2}
                      style={{ width: '40%' }}
                      id="email"
                    />
                  </Form.Item>
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
                  <p className="danger" style={{ color: 'red', marginTop: '10px' }}>
                    {flag3 ? ` Error: ${error}` : ''}
                  </p>
                  <p className="danger" style={{ color: 'red', marginTop: '10px' }}>
                    {flag2 ? ` Error: ${error}` : ''}
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

export default AddCompany;

import React, { useState, useEffect } from 'react';
import { NavLink, useHistory } from 'react-router-dom';
import { Form, Input, Button, Collapse, Spin, Checkbox, Row, Col, Avatar, Radio, Upload, Select } from 'antd';
import ImgCrop from 'antd-img-crop';

import { AuthWrapper, Aside, Content } from './style';
import Heading from '../../../../components/heading/heading';
import 'antd/es/modal/style';
import 'antd/es/slider/style';
import axios from 'axios';

import actions from '../../../../redux/authentication/actions';
import { setItem } from '../../../../utility/localStorageControl';
import { useDispatch } from 'react-redux';

const SignIn = () => {
  const { autoLogin: autoLoginAction, setUserAuthToken } = actions;
  const dispatch = useDispatch();
  const history = useHistory();
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [flag, setFlag] = useState(false);
  const [flag2, setFlag2] = useState(false);
  const [flag3, setFlag3] = useState(false);
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [loading3, setLoading3] = useState(true);
  const [loading4, setLoading4] = useState(true);
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [avatarImages, setAvatarImages] = useState([]);
  const [fileImage, setFileImage] = useState(null);
  const [urlImage, setUrlImage] = useState(null);
  const { Panel } = Collapse;
  const [show, setShow] = useState(true);
  const [email, setEmail] = useState('');
  const [serverRoles, setServerRoles] = useState([]);

  const [form] = Form.useForm();

  const handleSubmit = values => {
    setLoading2(true);
    setLoading3(true);
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}gamification/auth/signin`;
    const userValues = {
      email: values.username,
      password: values.password,
      deviceInfo: {
        deviceId: 'aaaa-aaaa-aaaa-aaaaaa',
        deviceType: 'BROWSER_CHROME',
      },
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
    data.append('role', JSON.stringify(values.selectRole));
    fileImage === null ? null : data.append('file', fileImage);
    urlImage === null ? null : data.append('key', urlImage);
    var config = {
      method: 'post',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      data: data,
    };
    axios(config)
      .then(function(response) {
        setLoading2(false);
        const user = response.data;
        console.log(user);
        setItem('user', user);
        dispatch(autoLogin(user));
      })
      .catch(function(error) {
        console.log(error);
        setFlag3(true);
        setError('Username or Password is wrong');
        setLoading2(false);
        setLoading3(false);
      });
  };

  const autoLogin = user => {
    return async dispatch => {
      dispatch(autoLoginAction(user));
      setItem('access_token', user.accessToken);
      setItem('user', user);
      return dispatch(setUserAuthToken(user.accessToken));
    };
  };

  const handleChange = e => {
    if (e.target.value === '') {
      setLoading3(true);
    } else {
      setLoading3(false);
    }
    setFlag3(false);
  };

  useEffect(() => {
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/avathars`;
    var config = {
      method: 'get',
      url: URL,
      // headers: {
      //   Authorization: `Bearer ${user.accessToken}`,
      // },
    };

    axios(config)
      .then(function(response) {
        const result = response.data;
        setAvatarImages(result);
      })
      .catch(function(error) {
        console.log(error);
      });
  }, []);

  const [fileList, setFileList] = useState([]);

  const beforeUpload = file => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      setFlag(true);
      setError('You can only upload JPG/PNG file!');
      return;
    }
    const isLt2M = file.size / 1024 / 1024 < 0.5;
    if (!isLt2M) {
      setFlag(true);
      setError('Image must smaller than 500KB!');
      return;
    }
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.addEventListener('load', event => {
      const _loadedImageUrl = event.target.result;
      setChoosenAvatar(_loadedImageUrl);
      setShow(false);
    });
    return isJpgOrPng && isLt2M;
  };

  const [choosenAvatar, setChoosenAvatar] = useState('');

  const handleChange2 = e => {
    setFileImage(null);
    setUrlImage(e.target.value);
    setShow(false);
    setChoosenAvatar(e.target.value);
  };

  const onChange2 = ({ fileList: newFileList }) => {
    const isLt2M = newFileList[0].size / 1024 / 1024 < 0.5;
    const isJpgOrPng = newFileList[0].type === 'image/jpeg' || newFileList[0].type === 'image/png';
    if (isLt2M === false) {
      setFlag(true);
      setError('Image must smaller than 500KB!');
      return;
    } else if (isJpgOrPng == false) {
      setFlag(true);
      setError('You can only upload JPG/PNG file!');
    } else {
      setFlag2(false);
      setFlag(false);
      setUrlImage(null);
      setFileImage(newFileList[0].originFileObj);
      setFileList(newFileList);
    }
  };

  function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  const handleEmail = e => {
    setLoading(true);
    setFlag2(false);
    setFlag3(false);
    setEmail(e.target.value);
  };

  const checkEmail = e => {
    setShowForgotPassword(false);
    if (validateEmail(email)) {
      const domain = email.split('@').pop();
      setLoading(false);
      setLoading4(false);
      setFlag2(false);
      const api = process.env.REACT_APP_BACKEND_API;
      const URL = `${api}userManagement/getCredentials/${email}`;
      var config = {
        method: 'get',
        url: URL,
        headers: {
          'Access-Control-Allow-Origin': '*',
          //authkey: '1e9627df-ae68-48c1-882c-484a1747cabe',
        },
      };

      axios(config)
        .then(function(response) {
          //setLoading(false);
          const result = response.data;
          console.log(result);
          if (result.password !== null) {
            setShowForgotPassword(true);
          }
          if (result.emailId === null) {
            setLoading(true);
            setLoading4(true);
            setLoading3(true);
            setShowForgotPassword(false);
            setFlag3(true);
            setError("Email doesn't exist in database!");
          }
          if (result.imageKey === null) {
            console.log('Hello');
          } else {
            setChoosenAvatar(result.imageKey);
            setShow(false);
          }
          if (result.roles.length !== 0) {
            setServerRoles(result.roles);
            form.setFieldsValue({
              selectRole: result.roles[0],
            });
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    } else {
      setShowForgotPassword(false);
      setLoading(true);
      setLoading4(true);
      setFlag2(true);
      setLoading3(true);
      setError('Email is not Valid');
    }
  };

  const beforeCrop = file => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      setFlag(true);
      setError('You can only upload JPG/PNG file!');
    }
    const isLt2M = file.size / 1024 / 1024 < 0.5;
    if (!isLt2M) {
      setFlag(true);
      setError('Image must smaller than 500KB!');
    }
    return isJpgOrPng && isLt2M;
  };

  const createPassword = () => {
    setLoading4(true);
    setFlag3(true);
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}forgot_password?email=${email}`;
    var config = {
      method: 'post',
      url: URL,
    };
    axios(config)
      .then(function(response) {
        console.log(response.data);
        setSuccess(
          'Password Creation Link has been sent to your registered email, Please Follow the instructions given on mail',
        );
        setTimeout(() => {
          setLoading4(false);
          setFlag3(false);
        }, 3000);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const forgotPasswordMessage = () => {
    setLoading4(true);
    setFlag3(true);
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}forgot_password?email=${email}`;
    var config = {
      method: 'post',
      url: URL,
    };
    axios(config)
      .then(function(response) {
        setSuccess(
          'Password Reset Link has been sent to your registered email, Please Follow the instructions given on mail',
        );
        setTimeout(() => {
          setLoading4(false);
          setFlag3(false);
        }, 3000);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <Row>
      <Col xxl={16} xl={15} lg={12} md={16} xs={24}>
        <AuthWrapper>
          <p className="auth-notice">
            Don&rsquo;t have an account? <NavLink to="/register">Sign up now</NavLink>
          </p>
          <div className="auth-contents">
            <Form name="login" form={form} onFinish={handleSubmit} layout="vertical">
              <Heading as="h3">Sign in</Heading>
              {show ? (
                <Form.Item name="avatar">
                  <Collapse accordion>
                    <Panel header="Choose Your Avatar or Select an Image" key="1">
                      <Form.Item name="avatar">
                        <Radio.Group size="large" onChange={handleChange2}>
                          {avatarImages.map(img => (
                            <Radio.Button
                              key={img.id}
                              value={img.avatharKey}
                              style={{ marginLeft: '10px', marginTop: '10px' }}
                            >
                              <Avatar src={img.avatharKey} />
                            </Radio.Button>
                          ))}
                        </Radio.Group>
                      </Form.Item>
                      <div>
                        <p>Upload your Image</p>
                        <ImgCrop rotate beforeCrop={beforeCrop}>
                          <Upload
                            //action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                            listType="picture-card"
                            fileList={fileList}
                            onChange={onChange2}
                            beforeUpload={beforeUpload}
                          >
                            {fileList.length < 1 && 'Choose Image'}
                          </Upload>
                        </ImgCrop>
                      </div>
                    </Panel>
                    <div>
                      <p className="danger text-center" style={{ color: 'red' }}>
                        {flag ? error : ''}
                      </p>
                    </div>
                  </Collapse>
                </Form.Item>
              ) : (
                ''
              )}
              {choosenAvatar === '' ? (
                ''
              ) : (
                <Form.Item name="avatar" label="Your Avatar Image">
                  <img
                    src={choosenAvatar}
                    alt="avatar"
                    style={{ width: '60px', height: '60px', borderRadius: '50%' }}
                  />
                  <p
                    className="danger"
                    style={{ color: 'green', cursor: 'pointer' }}
                    onClick={() => {
                      setShow(true);
                      setChoosenAvatar('');
                    }}
                  >
                    Change Avatar
                  </p>
                </Form.Item>
              )}
              <Form.Item
                name="username"
                rules={[{ required: true }]}
                label="Email Address"
                style={{ marginTop: '-15px' }}
              >
                <Input
                  placeholder="Email"
                  required
                  onChange={handleEmail}
                  onBlur={checkEmail}
                  id="email"
                  onPaste={e => {
                    e.preventDefault();
                    return false;
                  }}
                  onCopy={e => {
                    e.preventDefault();
                    return false;
                  }}
                />
              </Form.Item>
              <p className="danger" style={{ color: 'red', marginTop: '-12px' }}>
                {flag2 ? error : ''}
              </p>
              <Form.Item
                name="password"
                rules={[{ message: 'Please input your Password!', required: true }]}
                label="Password"
              >
                <Input.Password
                  placeholder="Password"
                  required
                  onChange={handleChange}
                  disabled={loading}
                  onPaste={e => {
                    e.preventDefault();
                    return false;
                  }}
                  onCopy={e => {
                    e.preventDefault();
                    return false;
                  }}
                />
              </Form.Item>
              <Form.Item
                name="selectRole"
                label="Select Role"
                rules={[{ message: 'Please select one role', required: true }]}
              >
                <Select placeholder="Select Role" disabled={loading}>
                  {serverRoles.map(role => (
                    <Select.Option value={role} key={role}>
                      {role}
                    </Select.Option>
                  ))}

                  {/* <Select.Option value="Admin">Admin</Select.Option>
                  <Select.Option value="Learner">Learner</Select.Option> */}
                </Select>
              </Form.Item>
              <div className="auth-form-action">
                <Checkbox disabled={loading}>Keep me logged in</Checkbox>
                {showForgotPassword ? (
                  <Button
                    className="btn-signin"
                    disabled={loading4}
                    onClick={forgotPasswordMessage}
                    style={{ background: 'transparent', border: 'none', marginTop: '-5px' }}
                  >
                    Forgot password?
                  </Button>
                ) : (
                  <Button
                    className="btn-signin"
                    onClick={createPassword}
                    disabled={loading4}
                    style={{ background: 'transparent', border: 'none', marginTop: '-5px' }}
                  >
                    Create password
                  </Button>
                )}
              </div>
              <Form.Item>
                <Button
                  className="btn-signin"
                  htmlType="submit"
                  type={loading3 ? '' : 'primary'}
                  size="large"
                  disabled={loading3}
                >
                  {loading2 ? <Spin size="medium" /> : 'Sign In'}
                </Button>
              </Form.Item>
            </Form>
          </div>
          <div>
            <p className="danger text-center" style={{ color: 'red' }}>
              {flag3 ? error : ''}
            </p>
            <p className="danger text-center" style={{ color: 'green' }}>
              {flag3 ? success : ''}
            </p>
          </div>
        </AuthWrapper>
      </Col>
      <Col xxl={8} xl={9} lg={12} md={8} xs={24}>
        <Aside>
          <div className="auth-side-content">
            <img src={require('../../../../static/img/auth/topShape.png')} alt="" className="topShape" />
            <img src={require('../../../../static/img/auth/bottomShape.png')} alt="" className="bottomShape" />
            <Content>
              <Heading as="h1">
                <img style={{ height: '50px' }} src={require('../../../../static/img/logo.png')} alt="" />
              </Heading>
              <br />
              <Heading as="h1">Manage all your organisation things here!</Heading>
              <img
                className="auth-content-figure"
                src={require('../../../../static/img/auth/cover.png')}
                alt="cover"
                style={{ width: '100%' }}
              />
            </Content>
          </div>
        </Aside>
      </Col>
    </Row>
  );
};

export default SignIn;

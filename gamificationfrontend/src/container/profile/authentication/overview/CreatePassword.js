import React, { useState, useEffect } from 'react';
import { NavLink, useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import { Form, Input, Button, Modal, Row, Col } from 'antd';
import { AuthWrapper, Aside, Content } from './style';
import Heading from '../../../../components/heading/heading';

const CreatePassword = () => {
  const [form] = Form.useForm();
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);

  const [password, setPassword] = useState('');
  const [retypePassword, setRetypePassword] = useState('');
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(true);

  const handleSubmit = values => {
    if (checkPasswordRegex(password)) {
      setFlag(false);
      if (password === retypePassword) {
        if (checkPasswordRegex(password)) {
          setShowModal(true);
        } else {
          setFlag(true);
          setError('Your Password is not matching the below criteria.');
        }
      } else {
        setFlag(true);
        setError('Both Passwords are not Matching');
      }
    } else {
      setFlag(true);
      setError('Your Password is not matching the below criteria.');
    }
  };

  function checkPasswordRegex(str) {
    var re = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    return re.test(str);
  }

  const handlePassword = e => {
    setFlag(false);
    setPassword(e.target.value);
  };

  const handleRetypePassword = e => {
    setFlag(false);
    setRetypePassword(e.target.value);
  };

  const checkPassword = () => {
    setLoading(false);
  };

  const handleCancel = () => {
    setShowModal(false);
  };

  useEffect(() => {
    if (password.length > 0 && retypePassword.length > 0) {
      setLoading2(false);
    } else {
      setLoading2(true);
    }
  }, [password, retypePassword]);

  return (
    <Row>
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

      <Col xxl={16} xl={15} lg={12} md={16} xs={24}>
        <AuthWrapper>
          <div className="auth-contents">
            <Form name="forgotPass" form={form} onSubmitCapture={handleSubmit} layout="vertical">
              <Heading as="h3">Create Password?</Heading>
              <p className="forgot-text">Enter the Password which you want to create.</p>
              <Form.Item
                label="Your New Password"
                name="password"
                rules={[{ required: true, message: 'Please input your password!' }]}
              >
                <Input.Password
                  placeholder="Your New Password"
                  onChange={handlePassword}
                  onBlur={checkPassword}
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
                label="Re-Type Password"
                name="dd"
                rules={[{ required: true, message: 'Please input your above password!' }]}
              >
                <Input.Password
                  placeholder="Re-Type Password"
                  onChange={handleRetypePassword}
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
              <Form.Item>
                <Button
                  className="btn-reset"
                  htmlType="submit"
                  type={loading2 ? '' : 'primary'}
                  size="large"
                  disabled={loading2}
                >
                  Submit
                </Button>
              </Form.Item>
              <p className="danger text-center" style={{ color: 'red' }}>
                {flag ? error : ''}
              </p>
              <div>
                <p>
                  Hint: <br /> Password should contain at least 8 characters. <br /> Password should have a Special
                  character (eg. @ # $ %). Password should have an uppercase and a lowercase character and a digit.
                </p>
              </div>
            </Form>
          </div>
        </AuthWrapper>
      </Col>
      <Modal title="Password Successfully Created" visible={showModal} footer={null} onCancel={handleCancel}>
        <NavLink to="/">Go Back to Login</NavLink>
      </Modal>
    </Row>
  );
};

export default CreatePassword;

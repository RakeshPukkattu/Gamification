import React, { useState, useEffect } from 'react';
import { Avatar, Tooltip, Modal, Upload, Radio, Alert } from 'antd';
import { Link, useHistory, useRouteMatch } from 'react-router-dom';
import FeatherIcon from 'feather-icons-react';
import { InfoWraper } from './auth-info-style';
import ImgCrop from 'antd-img-crop';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { logOut } from '../../../redux/authentication/actionCreator';
import { getItem, removeItem } from '../../../utility/localStorageControl';

const AuthInfo = () => {
  const dispatch = useDispatch();
  const history = useHistory();
  const { path } = useRouteMatch();
  const [show, setShow] = useState(false);
  const [showModal3, setShowModal3] = useState(false);
  const [image, setImage] = useState(null);
  const [showAlert, setShowAlert] = useState(false);
  const [text, setText] = useState({
    icon: 'info',
    text: 'Uploading Image',
  });

  const handleCancel2 = () => {
    setShowModal3(false);
  };
  const [fileList, setFileList] = useState([]);
  const [flag, setFlag] = useState(false);
  const [error, setError] = useState('');
  const [avatarImages, setAvatarImages] = useState([]);

  const { logoutDone } = useSelector(state => {
    return {
      logoutDone: state.auth.logoutDone,
    };
  });
  React.useEffect(() => {
    if (logoutDone === true) {
      setTimeout(() => {
        history.push('/signin');
      }, 100);
    }
  }, [logoutDone]);

  const SignOut = () => {
    const user = getItem('user');
    console.log(user.accessToken);
    const api = process.env.REACT_APP_BACKEND_API;
    const values = {
      deviceInfo: {
        deviceId: 'aaaa-aaaa-aaaa',
        deviceType: 'BROWSER_CHROME',
      },
      token: `${user.accessToken}`,
    };
    const URL = `${api}gamification/auth/signout`;
    var config = {
      method: 'put',
      url: URL,
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
        'Content-Type': 'application/json',
      },
      data: values,
    };
    axios(config)
      .then(function(response) {
        removeItem('access_token');
        console.log(response);
        setTimeout(() => {
          location.reload();
        }, 20);
      })
      .catch(function(error) {
        setTimeout(() => {
          location.reload();
        }, 20);
        console.log(error);
      });
    //dispatch(logOut(location));
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

  useEffect(() => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/getUser/${user.email}`;
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
        setImage(result.imageKey);
      })
      .catch(function(error) {
        console.log(error);
      });
  }, []);

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
      //setChoosenAvatar(_loadedImageUrl);
      setShow(false);
    });
    return isJpgOrPng && isLt2M;
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
      setShowAlert(true);
      setFlag(false);
      const user = getItem('user');
      const api = process.env.REACT_APP_BACKEND_API;
      const URL = `${api}userManagement/userAvatharImage/${user.email}`;
      var data = new FormData();
      data.append('file', newFileList[0].originFileObj);
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
          const result = response.data;
          console.log(result);
          if (result.code.includes('200')) {
            setText({ icon: 'success', text: 'Successfully Uploaded Image' });
            setTimeout(() => {
              setShowAlert(false);
              handleCancel2();
            }, 3000);
          }
        })
        .catch(function(error) {
          console.log(error);
        });
      setFileList(newFileList);
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

  const handleChange2 = e => {
    setShowAlert(true);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}userManagement/userAvatharImage/${user.email}`;
    var data = new FormData();
    data.append('key', e.target.value);
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
        const result = response.data;
        console.log(result);
        setText({ icon: 'success', text: 'Successfully Uploaded Image' });
        setTimeout(() => {
          setImage(e.target.value);
          setShowAlert(false);
          handleCancel2();
        }, 3000);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const showEditImage = () => {
    setShowModal3(true);
  };

  return (
    <InfoWraper>
      <div className="nav-author">
        <Link to="#" className="head-example">
          <Avatar
            src={image === null ? 'https://cdn0.iconfinder.com/data/icons/user-pictures/100/matureman1-512.png' : image}
            onClick={() => {
              showEditImage();
            }}
          />
        </Link>
        {/* <Popover placement="bottomRight" content={userContent} action="click">
          <Link to="#" className="head-example">
            <Avatar src="https://cdn0.iconfinder.com/data/icons/user-pictures/100/matureman1-512.png" />
          </Link>
        </Popover> */}
      </div>
      <div className="notification">
        <Tooltip placement="top" title="LogOut">
          <Link to="#" className="head-example" style={{ marginTop: '5px' }} onClick={SignOut}>
            <FeatherIcon icon="log-out" size={22} />
          </Link>
        </Tooltip>
      </div>
      <Modal title="Change the Avatar Image" visible={showModal3} footer={null} onCancel={handleCancel2}>
        {showAlert ? (
          <Alert message={text.text} type={text.icon} showIcon />
        ) : (
          <>
            <p style={{ marginBottom: '-5px' }}>Choose Your Avatar</p>
            <Radio.Group size="large" onChange={handleChange2}>
              {avatarImages.map(img => (
                <Radio.Button key={img.id} value={img.avatharKey} style={{ marginLeft: '10px', marginTop: '10px' }}>
                  <Avatar src={img.avatharKey} />
                </Radio.Button>
              ))}
            </Radio.Group>
            <div>
              <p style={{ marginBottom: '-5px', marginTop: '20px' }}>Upload User's Avatar</p>
              <ImgCrop rotate beforeCrop={beforeCrop}>
                <Upload listType="picture-card" fileList={fileList} onChange={onChange2} beforeUpload={beforeUpload}>
                  {fileList.length < 1 && 'Choose Image'}
                </Upload>
              </ImgCrop>
            </div>
            <div>
              <p className="danger text-center" style={{ color: 'red' }}>
                {flag ? error : ''}
              </p>
            </div>
          </>
        )}
      </Modal>
    </InfoWraper>
  );
};

export default AuthInfo;

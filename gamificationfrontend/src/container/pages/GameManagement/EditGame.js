import React, { useState, useEffect } from 'react';
import { Row, Col, Input, Spin, Form, Alert, Select, Upload } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import { useHistory, useParams } from 'react-router-dom';
import _ from 'lodash';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';
import CreatableSelect from 'react-select/creatable';
import CreatableInputOnly from '../CountryLanguageManagement/AddLanguage';

const components = {
  DropdownIndicator: null,
};

const createOption = label => ({
  label,
  value: label,
});

const EditGame = () => {
  const { id } = useParams();
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('');
  const [name, setName] = useState('');
  const [file, setFile] = useState('');
  const [file2, setFile2] = useState('');
  const [file3, setFile3] = useState('');
  const [game, setGame] = useState(null);
  const [image, setImage] = useState('');
  const [image2, setImage2] = useState('');
  const [show, setShow] = useState(true);
  const [show2, setShow2] = useState(true);

  const [state, setState] = useState({
    inputValue: '',
    value: [],
  });

  useEffect(() => {
    getAllGames();
  }, []);

  useEffect(() => {
    console.log(game);
    if (game !== null) {
      form.setFieldsValue({
        gameName: game.gameName,
        assessment: game.assessment === true ? 'Yes' : 'No',
      });
      setImage(game.thumbNailKey);
      setImage2(game.imageKey);
      let keywords = [];
      const kk = game.keyWords.split(',');
      kk.map(key => {
        keywords.push({
          label: key,
          value: key,
        });
      });
      setState({
        ...state,
        inputValue: '',
        value: keywords,
      });
    }
  }, [game]);

  const getAllGames = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}Games/allGames`;
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
        const rev = result.games.reverse();
        rev.map(game => {
          if (id == game.id) {
            setGame(game);
          }
        });
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleSubmit = values => {
    let keywords = [];
    state.value.map(lan => {
      keywords.push(lan.value);
    });
    console.log(fileList.length, fileList2.length);
    setLoading2(true);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const gameValues = {
      gameName: values.gameName,
      keywords: keywords,
      assessment: values.assessment === 'Yes' ? true : false,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(gameValues));
    file3 === '' ? null : data.append('file', file3);
    fileList.length === 0 ? null : data.append('thumbNail', fileList[0].originFileObj);
    fileList2.length === 0 ? null : data.append('image', fileList2[0].originFileObj);
    const URL = `${api}Games/modifyGame/${id}`;
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
          setAlertText('game with same name already exists!');
          window.scrollTo(0, 0);
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/game',
            state: { detail: `${values.gameName} Updated Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const handleChange5 = value => {
    setState({ ...state, value });
  };
  const handleInputChange = inputValue => {
    setState({ ...state, inputValue });
  };
  const handleKeyDown = event => {
    const { inputValue } = state;
    if (!inputValue) return;
    switch (event.key) {
      case 'Enter':
      case 'Tab':
        if (state.value.length === 0 || state.value.length === 11) {
          setState({
            ...state,
            inputValue: '',
            value: [...state.value, createOption(inputValue)],
          });
        } else {
          const found = state.value.some(lan => lan.value.toLowerCase() === inputValue.toLowerCase());
          console.log(found);
          if (found) {
            console.log('Already Present');
          } else {
            setState({
              ...state,
              inputValue: '',
              value: [...state.value, createOption(inputValue)],
            });
          }
        }
        event.preventDefault();
    }
  };

  useEffect(() => {
    if (name === '' || file === '' || file2 === '' || state.value.length === 0) {
      setLoading(true);
    } else {
      setLoading(false);
    }
  }, [name, file, file2, state]);

  const [fileList, setFileList] = useState([]);
  const [fileList2, setFileList2] = useState([]);

  const onChange2 = ({ fileList: newFileList }) => {
    const isLt2M = newFileList[0].size / 1024 / 1024 < 0.5;
    const isJpgOrPng = newFileList[0].type === 'image/jpeg' || newFileList[0].type === 'image/png';
    if (isLt2M === false) {
      setShowAlert(true);
      setError('Image must smaller than 500KB!');
      return;
    } else if (isJpgOrPng == false) {
      setShowAlert(true);
      setError('You can only upload JPG/PNG file!');
    } else {
      console.log('success');
      // setShowAlert(true);
      // setFlag(false);
      // const user = getItem('user');
      // const api = process.env.REACT_APP_BACKEND_API;
      // const URL = `${api}userManagement/userAvatharImage/${user.email}`;
      // var data = new FormData();
      // data.append('file', newFileList[0].originFileObj);
      // var config = {
      //   method: 'put',
      //   url: URL,
      //   headers: {
      //     'Content-Type': 'multipart/form-data',
      //     Authorization: `Bearer ${user.accessToken}`,
      //   },
      //   data: data,
      // };

      // axios(config)
      //   .then(function(response) {
      //     const result = response.data;
      //     console.log(result);
      //     if (result.code.includes('200')) {
      //       setText({ icon: 'success', text: 'Successfully Uploaded Image' });
      //       setTimeout(() => {
      //         setShowAlert(false);
      //         handleCancel2();
      //       }, 3000);
      //     }
      //   })
      //   .catch(function(error) {
      //     console.log(error);
      //   });
      setFile('fvb');
      setFileList(newFileList);
    }
  };

  const onChange3 = ({ fileList: newFileList }) => {
    const isLt2M = newFileList[0].size / 1024 / 1024 < 0.5;
    const isJpgOrPng = newFileList[0].type === 'image/jpeg' || newFileList[0].type === 'image/png';
    if (isLt2M === false) {
      setShowAlert(true);
      setError('Image must smaller than 500KB!');
      return;
    } else if (isJpgOrPng == false) {
      setShowAlert(true);
      setError('You can only upload JPG/PNG file!');
    } else {
      console.log('success');
      // setShowAlert(true);
      // setFlag(false);
      // const user = getItem('user');
      // const api = process.env.REACT_APP_BACKEND_API;
      // const URL = `${api}userManagement/userAvatharImage/${user.email}`;
      // var data = new FormData();
      // data.append('file', newFileList[0].originFileObj);
      // var config = {
      //   method: 'put',
      //   url: URL,
      //   headers: {
      //     'Content-Type': 'multipart/form-data',
      //     Authorization: `Bearer ${user.accessToken}`,
      //   },
      //   data: data,
      // };

      // axios(config)
      //   .then(function(response) {
      //     const result = response.data;
      //     console.log(result);
      //     if (result.code.includes('200')) {
      //       setText({ icon: 'success', text: 'Successfully Uploaded Image' });
      //       setTimeout(() => {
      //         setShowAlert(false);
      //         handleCancel2();
      //       }, 3000);
      //     }
      //   })
      //   .catch(function(error) {
      //     console.log(error);
      //   });
      setFile2('fvb');
      setFileList2(newFileList);
    }
  };

  const beforeUpload = file => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      setShowAlert(true);
      setError('You can only upload JPG/PNG file!');
      return;
    }
    const isLt2M = file.size / 1024 / 1024 < 0.5;
    if (!isLt2M) {
      setShowAlert(true);
      setError('Image must smaller than 500KB!');
      return;
    }
    return isJpgOrPng && isLt2M;
  };

  const changeImage = () => {
    setShow(false);
  };

  const changeImage2 = () => {
    setShow2(false);
  };

  return (
    <>
      <PageHeader title="Edit Game" />
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
                  {showAlert ? <Alert message={error} type="error" showIcon /> : ''}
                </div>
                <Form name="addgame" form={form} onFinish={handleSubmit} layout="vertical">
                  <Form.Item
                    name="gameName"
                    rules={[{ message: 'Please enter game name', required: true }]}
                    label="Game Title"
                  >
                    <Input
                      placeholder="Game Name"
                      onChange={e => {
                        setName(e.target.value);
                        setFlag2(false);
                      }}
                      onKeyPress={specialCharacter}
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <Form.Item name="keywords" label="Keywords">
                    <div style={{ width: '40%' }}>
                      <CreatableSelect
                        components={components}
                        inputValue={state.inputValue}
                        isClearable
                        isMulti
                        menuIsOpen={false}
                        onChange={handleChange5}
                        onInputChange={handleInputChange}
                        onKeyDown={handleKeyDown}
                        placeholder="Type Language and press enter..."
                        value={state.value}
                      />
                      <p style={{ fontSize: '13px', marginTop: '5px', color: 'gray' }}>
                        Press Enter ot Tab after writing the language name
                      </p>
                      <div style={{ display: 'none' }}>
                        <CreatableInputOnly />
                      </div>
                    </div>
                  </Form.Item>
                  <Form.Item name="smallImage" label="Small Thumbnail" style={{ marginTop: '-40px' }}>
                    {show ? (
                      <>
                        <img src={image} alt="smallImage" width={150} />
                        <Button
                          className="btn-signin"
                          type="warning"
                          size="small"
                          style={{ marginLeft: '10px' }}
                          onClick={changeImage}
                        >
                          Change
                        </Button>
                      </>
                    ) : (
                      <Upload
                        listType="picture-card"
                        fileList={fileList}
                        onChange={onChange2}
                        beforeUpload={beforeUpload}
                      >
                        {fileList.length < 1 && 'Choose Image'}
                      </Upload>
                    )}
                  </Form.Item>
                  <p style={{ fontSize: '13px', marginTop: '5px', color: 'gray', marginTop: '-15px' }}>
                    Maximum size limit for the image is 500KB
                  </p>
                  <Form.Item name="bigImage" label="Big Thumbnail">
                    {show2 ? (
                      <>
                        <img src={image2} alt="smallImage" width={150} />
                        <Button
                          className="btn-signin"
                          type="warning"
                          size="small"
                          style={{ marginLeft: '10px' }}
                          onClick={changeImage2}
                        >
                          Change
                        </Button>
                      </>
                    ) : (
                      <Upload
                        listType="picture-card"
                        fileList={fileList2}
                        onChange={onChange3}
                        beforeUpload={beforeUpload}
                      >
                        {fileList2.length < 1 && 'Choose Image'}
                      </Upload>
                    )}
                  </Form.Item>
                  <p style={{ fontSize: '13px', marginTop: '5px', color: 'gray', marginTop: '-15px' }}>
                    Maximum size limit for the image is 500KB
                  </p>
                  <Form.Item
                    name="assessment"
                    label="Select Assessment"
                    rules={[{ message: 'Please select one assessment', required: true }]}
                  >
                    <Select placeholder="Select Assessment" showArrow={true} style={{ width: '40%' }}>
                      <Option value="Yes">Yes</Option>
                      <Option value="No">No</Option>
                    </Select>
                  </Form.Item>
                  <Form.Item name="zip" label="Zip File">
                    <Input
                      placeholder="zip file"
                      onChange={e => {
                        setFile3(e.target.files[0]);
                        setFlag2(false);
                      }}
                      accept=".zip,.rar,.7zip"
                      type="file"
                      style={{ width: '40%' }}
                    />
                  </Form.Item>
                  <p style={{ fontSize: '13px', marginTop: '5px', color: 'gray', marginTop: '-15px' }}>
                    Maximum size limit for the zip file is 10MB and if you don't want to change file, leave it empty
                  </p>
                  <div className="sDash-button-grp">
                    <Button
                      className="btn-signin"
                      htmlType="submit"
                      type="primary"
                      size="large"
                      //disabled={loading}
                    >
                      {loading2 ? <Spin size="medium" /> : 'Update'}
                    </Button>
                  </div>
                </Form>
                {/* <div>
                  <p className="danger" style={{ color: 'red', marginTop: '10px' }}>
                    {flag2 ? ` Error: ${error}` : ''}
                  </p>
                </div> */}
              </Cards>
            </ExportStyleWrap>
          </Col>
        </Row>
      </Main>
    </>
  );
};

export default EditGame;

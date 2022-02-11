import React, { useState, useEffect } from 'react';
import { Row, Col, Form, Input, Modal, Radio, Select, Table, Alert } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Link } from 'react-router-dom';
import FeatherIcon from 'feather-icons-react';
import { WizardWrapper, Wizard, WizardBlock, ProductTable, OrderSummary } from '../SurveyManagement/Style';
import { Steps } from '../../../components/steps/steps2';
import Heading from '../../../components/heading/heading';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import { Main, BasicFormWrapper, TableWrapper } from '../../styled';
import { useHistory, useParams } from 'react-router-dom';
import { getItem } from '../../../utility/localStorageControl';
import axios from 'axios';
import { UserTableStyleWrapper } from '../style';

const AddQuestion = () => {
  const { id } = useParams();
  const history = useHistory();
  const [form] = Form.useForm();
  const [industrytype, setIndustryType] = useState('');
  const [gameType, setGameType] = useState('');
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [flag3, setFlag3] = useState(true);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isModalVisible2, setIsModalVisible2] = useState(false);
  const [disable, setDisable] = useState(false);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [adminText, setAdminText] = useState('');
  const [adminComponents, setAdminComponents] = useState([]);
  const [uiThemes, setUIThemes] = useState([]);
  const [adminGames, setAdminGames] = useState([]);
  const [industries, setIndustries] = useState([]);

  useEffect(() => {
    getAllIndustries();
  }, []);

  const getAllIndustries = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}industryManagement/industries`;
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
        console.log(response.data);
        const result = response.data.industries;
        setIndustries(result);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const [state, setState] = useState({
    status: 'process',
    isFinished: false,
    current: 0,
  });

  const { status, isFinished, current } = state;

  const handleIndustryType = e => {
    setFlag3(false);
    setIndustryType(e);
  };
  const handleGameType = e => {
    setGameType(e);
  };
  const next = () => {
    if (flag3 === true) {
      if (state2.selectedRows.length !== 0) {
        const surveyFound = state2.selectedRows.some(row => row.name === 'Survey Management');
        const sessionFound = state2.selectedRows.some(row => row.name === 'Session Management');
        const groupFound = state2.selectedRows.some(row => row.name === 'Group Management');
        const userFound = state2.selectedRows.some(row => row.name === 'User Management');
        const loginFound = state2.selectedRows.some(row => row.name === 'Login Management');
        const countryFound = state2.selectedRows.some(row => row.name === 'Country Management');
        if (surveyFound || sessionFound) {
          let group = state2.selectedRows.some(row => row.name === 'Group Management');
          let user = state2.selectedRows.some(row => row.name === 'User Management');
          let login = state2.selectedRows.some(row => row.name === 'Login Management');
          if (group !== true || user !== true || login !== true) {
            setAdminText('Please add login, user and group managment also.');
            setIsModalVisible2(true);
          }
        }
        if (groupFound) {
          let user = state2.selectedRows.some(row => row.name === 'User Management');
          let login = state2.selectedRows.some(row => row.name === 'Login Management');
          if (user !== true || login !== true) {
            setAdminText('Please add login and user managment also.');
            setIsModalVisible2(true);
          }
        }
        if (userFound) {
          let login = state2.selectedRows.some(row => row.name === 'Login Management');
          if (login !== true) {
            setAdminText('Please add login managment also.');
            setIsModalVisible2(true);
          }
        }
        if (loginFound) {
          let user = state2.selectedRows.some(row => row.name === 'User Management');
          if (user !== true) {
            setAdminText('Please add User managment also.');
            setIsModalVisible2(true);
          }
        }
        if (countryFound) {
          let language = state2.selectedRows.some(row => row.name === 'Language Management');
          if (language !== true) {
            setAdminText('Please add Language managment also.');
            setIsModalVisible2(true);
          }
        }
      } else {
        setShowAlert(true);
        setAlertText(`Please select one theme and component`);
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
      }

      //setIsModalVisible2(true);
    } else {
      setState({
        ...state,
        status: 'process',
        current: current + 1,
      });
    }
  };

  const prev = () => {
    setState({
      ...state,
      status: 'process',
      current: current - 1,
    });
  };

  const done = () => {
    setIsModalVisible(true);
  };

  const handleOk = () => {
    setState({
      ...state,
      isFinished: true,
      current: 0,
      visible: true,
    });
    setIsModalVisible(false);
    console.log(state2.selectedRows, state3.selectedRows, state4.selectedRows);
    let companys = [];
    let games = [];
    let themes = [];
    let industryId = [];
    industries.map(industry => {
      if (industry.industryName === industrytype) {
        industryId.push(industry.industryID);
      }
    });
    state2.selectedRows.map(state => {
      companys.push(state.key);
    });
    state3.selectedRows.map(state => {
      games.push(state.key);
    });
    state4.selectedRows.map(state => {
      themes.push(state.key);
    });
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const componentValues = { componentId: companys, gameId: games, themeId: themes[0], industryId: industryId };
    var data = new FormData();
    data.append('userJson', JSON.stringify(componentValues));
    const URL = `${api}assignCompanyComponents/assignComponents/${id}`;
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
        const result = response.data;
        if (result.code.includes('406')) {
          setShowAlert(true);
          setAlertText('Group with same name already exists!');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/company',
            state: { detail: `Components Assigned Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleOk2 = () => {
    setIsModalVisible2(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setIsModalVisible2(false);
  };

  useEffect(() => {
    if (state.status === 'finish') {
      setDisable(true);
    } else {
      setDisable(false);
    }
  }, [state]);

  const [state2, setState2] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
  });

  const [state3, setState3] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
  });

  const [state4, setState4] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
  });

  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      setState2({ ...state2, selectedRowKeys, selectedRows });
    },
    selectedRowKeys: state2.selectedRowKeys,
  };
  const rowSelection2 = {
    onChange: (selectedRowKeys, selectedRows) => {
      setState3({ ...state3, selectedRowKeys, selectedRows });
    },
    selectedRowKeys: state3.selectedRowKeys,
  };

  const rowSelection3 = {
    onChange: (selectedRowKeys, selectedRows) => {
      setState4({ ...state4, selectedRowKeys, selectedRows });
    },
    selectedRowKeys: state4.selectedRowKeys,
  };

  const componentsTableColumns = [
    {
      title: 'Components',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const gamesTableColumns = [
    {
      title: 'Games',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const themesTableColumns = [
    {
      title: 'Themes',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  let dataTable = [];

  const getAdminComponents = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}adminComponentsManagement/allAdminComponents`;
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
        const components = result.components;
        components.map(component => {
          const { id, title } = component;
          return dataTable.push({
            key: id,
            name: title,
          });
        });
        setAdminComponents(dataTable);
      })
      .catch(function(error) {
        console.log(error);
      });
  };
  const getThemeUIComponents = () => {
    industries.map(industry => {
      if (industry.industryName === industrytype) {
        industry.themes.map(theme => {
          const { id, themeName, thumbNailKey } = theme;
          return dataTable5.push({
            key: id,
            name: (
              <div className="user-info">
                <figure>
                  <img style={{ width: '40px' }} src={thumbNailKey} alt="theme" />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {themeName}
                  </Heading>
                </figcaption>
              </div>
            ),
          });
        });
        setUIThemes(dataTable5);
      }
    });
  };

  const getGamesComponents = () => {
    industries.map(industry => {
      if (industry.industryName === industrytype) {
        industry.games.map(game => {
          const { id, gameName, thumbNailKey } = game;
          return dataTable2.push({
            key: id,
            name: (
              <div className="user-info">
                <figure>
                  <img style={{ width: '40px' }} src={thumbNailKey} alt="game" />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {gameName}
                  </Heading>
                </figcaption>
              </div>
            ),
          });
        });
        setAdminGames(dataTable2);
      }
    });
  };

  useEffect(() => {
    getAdminComponents();
  }, []);
  useEffect(() => {
    getThemeUIComponents();
    getGamesComponents();
  }, [state]);

  let dataTable2 = [];
  let dataTable5 = [];

  const componentsTableColumns2 = [
    {
      title: 'Selected Components',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const gamesTableColumns2 = [
    {
      title: 'Selected Games',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const themesTableColumns2 = [
    {
      title: 'Selected Theme',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  let dataTable3 = [];
  let dataTable4 = [];
  let dataTable6 = [];

  if (state2.selectedRows.length > 0) {
    state2.selectedRows.map(state => {
      return dataTable3.push({
        key: state.id,
        name: state.name,
      });
    });
  }

  if (state3.selectedRows.length > 0) {
    state3.selectedRows.map(state => {
      return dataTable4.push({
        key: state.id,
        name: (
          <div className="user-info">
            <figure>
              <img style={{ width: '40px' }} src={state.name.props.children[0].props.children.props.src} alt="game" />
            </figure>
            <figcaption>
              <Heading className="user-name" as="h6">
                {state.name.props.children[1].props.children.props.children}
              </Heading>
            </figcaption>
          </div>
        ),
      });
    });
  }

  if (state4.selectedRows.length > 0) {
    state4.selectedRows.map(state => {
      return dataTable6.push({
        key: state.id,
        name: (
          <div className="user-info">
            <figure>
              <img style={{ width: '40px' }} src={state.name.props.children[0].props.children.props.src} alt="theme" />
            </figure>
            <figcaption>
              <Heading className="user-name" as="h6">
                {state.name.props.children[1].props.children.props.children}
              </Heading>
            </figcaption>
          </div>
        ),
      });
    });
  }

  useEffect(() => {
    if (state4.selectedRows.length === 0) {
      setFlag3(false);
    } else {
      setFlag3(false);
    }
  }, [state4]);

  useEffect(() => {
    if (state2.selectedRows.length === 0) {
      setFlag3(true);
    } else {
      const surveyFound = state2.selectedRows.some(row => row.name === 'Survey Management');
      const sessionFound = state2.selectedRows.some(row => row.name === 'Session Management');
      const groupFound = state2.selectedRows.some(row => row.name === 'Group Management');
      const userFound = state2.selectedRows.some(row => row.name === 'User Management');
      const loginFound = state2.selectedRows.some(row => row.name === 'Login Management');
      const countryFound = state2.selectedRows.some(row => row.name === 'Country Management');
      if (surveyFound || sessionFound) {
        let group = state2.selectedRows.some(row => row.name === 'Group Management');
        let user = state2.selectedRows.some(row => row.name === 'User Management');
        let login = state2.selectedRows.some(row => row.name === 'Login Management');
        if (group !== true || user !== true || login !== true) {
          setFlag3(true);
        } else {
          setFlag3(false);
        }
      }
      if (groupFound) {
        let user = state2.selectedRows.some(row => row.name === 'User Management');
        let login = state2.selectedRows.some(row => row.name === 'Login Management');
        if (user !== true || login !== true) {
          setFlag3(true);
        } else {
          setFlag3(false);
        }
      }
      if (userFound) {
        let login = state2.selectedRows.some(row => row.name === 'Login Management');
        if (login !== true) {
          setFlag3(true);
        } else {
          setFlag3(false);
        }
      }
      if (loginFound) {
        let user = state2.selectedRows.some(row => row.name === 'User Management');
        if (user !== true) {
          setFlag3(true);
        } else {
          setFlag3(false);
        }
      }
      if (countryFound) {
        let language = state2.selectedRows.some(row => row.name === 'Language Management');
        if (language !== true) {
          setFlag3(true);
        } else {
          setFlag3(false);
        }
      }
    }
  }, [state2]);

  useEffect(() => {
    if (state3.selectedRows.length === 0) {
      setFlag3(true);
    } else {
      setFlag3(false);
    }
  }, [state3]);
  return (
    <>
      <PageHeader title="Assign Components" />
      <Main>
        <div className="wizard-side-border">
          <WizardBlock>
            <Cards headless>
              <div
                style={{
                  marginTop: '-20px',
                  width: '100%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  position: 'absolute',
                }}
              >
                {showAlert ? <Alert message={alertText} type="error" showIcon /> : ''}
              </div>
              <WizardWrapper className="bordered-wizard">
                <Steps
                  isswitch
                  isvertical
                  current={0}
                  status={status}
                  steps={[
                    {
                      title: 'Select Industry Type',
                      content: (
                        <BasicFormWrapper className="basic-form-inner" style={{ marginTop: '-100px' }}>
                          <div className="">
                            <Row justify="center" style={{ margin: 'auto' }}>
                              <Col sm={22} xs={24}>
                                <div className="shipping-form">
                                  <Heading as="h4">1. Select Industry Type</Heading>
                                </div>
                              </Col>
                            </Row>
                            <Form form={form} name="account" style={{ marginBottom: '20px' }}>
                              <Form.Item name="dcd" label="Choose Industry Type">
                                <Select
                                  style={{ width: '100%' }}
                                  onChange={handleIndustryType}
                                  placeholder="Select Industry"
                                >
                                  {industries.map(industry => {
                                    return <Option value={industry.industryName}>{industry.industryName}</Option>;
                                  })}
                                </Select>
                              </Form.Item>
                            </Form>
                          </div>
                        </BasicFormWrapper>
                      ),
                    },
                    {
                      title: 'Select Theme',
                      content: (
                        <BasicFormWrapper className="basic-form-inner" style={{ marginTop: '-100px' }}>
                          <div className="">
                            <Row justify="left" style={{ margin: 'auto' }}>
                              <Col sm={22} xs={24}>
                                <div className="shipping-form">
                                  <Heading as="h4" style={{ marginBottom: '10px' }}>
                                    2. Select Theme
                                  </Heading>
                                </div>
                              </Col>
                            </Row>
                            <UserTableStyleWrapper>
                              <TableWrapper className="table-responsive">
                                <Table
                                  id="myTable"
                                  columns={themesTableColumns}
                                  //rowSelection={rowSelection3}
                                  rowSelection={{
                                    type: 'radio',
                                    ...rowSelection3,
                                  }}
                                  scroll={{ y: 240 }}
                                  dataSource={uiThemes}
                                  pagination={false}
                                ></Table>
                              </TableWrapper>
                            </UserTableStyleWrapper>
                          </div>
                        </BasicFormWrapper>
                      ),
                    },
                    {
                      title: 'Select Admin Components',
                      content: (
                        <BasicFormWrapper className="basic-form-inner" style={{ marginTop: '-100px' }}>
                          <div className="">
                            <Row justify="center" style={{ margin: 'auto' }}>
                              <Col sm={22} xs={24}>
                                <div className="shipping-form">
                                  <Heading as="h4" style={{ marginBottom: '10px' }}>
                                    3. Select Admin Components
                                  </Heading>
                                </div>
                              </Col>
                            </Row>
                            <UserTableStyleWrapper>
                              <TableWrapper className="table-responsive">
                                <Table
                                  id="myTable"
                                  columns={componentsTableColumns}
                                  rowSelection={rowSelection}
                                  scroll={{ y: 240 }}
                                  dataSource={adminComponents}
                                  pagination={false}
                                ></Table>
                              </TableWrapper>
                            </UserTableStyleWrapper>
                          </div>
                        </BasicFormWrapper>
                      ),
                    },
                    {
                      title: 'Select Games',
                      content: (
                        <BasicFormWrapper className="basic-form-inner" style={{ marginTop: '-100px' }}>
                          <div className="">
                            <Row justify="center" style={{ margin: 'auto' }}>
                              <Col sm={22} xs={24}>
                                <div className="shipping-form">
                                  <Heading as="h4">4. Select Games</Heading>
                                </div>
                              </Col>
                            </Row>
                            <UserTableStyleWrapper>
                              <TableWrapper className="table-responsive">
                                <Table
                                  id="myTable"
                                  columns={gamesTableColumns}
                                  rowSelection={rowSelection2}
                                  scroll={{ y: 240 }}
                                  dataSource={adminGames}
                                  pagination={false}
                                ></Table>
                              </TableWrapper>
                            </UserTableStyleWrapper>
                          </div>
                        </BasicFormWrapper>
                      ),
                    },
                    {
                      title: 'Review',
                      content: (
                        <BasicFormWrapper className="" style={{ marginTop: '-100px' }}>
                          <div className="">
                            <Row justify="center">
                              <Col sm={22} xs={24}>
                                <div className="payment-method-form">
                                  <Heading as="h4">5. Verify the Components, Games and Theme</Heading>
                                </div>
                              </Col>
                            </Row>
                            <Row gutter={25}>
                              <Col sm={8}>
                                <UserTableStyleWrapper>
                                  <TableWrapper className="table-responsive">
                                    <Table
                                      id="myTable"
                                      columns={themesTableColumns2}
                                      scroll={{ y: 240 }}
                                      dataSource={dataTable6}
                                      pagination={false}
                                    ></Table>
                                  </TableWrapper>
                                </UserTableStyleWrapper>
                              </Col>
                              <Col sm={8}>
                                <UserTableStyleWrapper>
                                  <TableWrapper className="table-responsive">
                                    <Table
                                      id="myTable"
                                      columns={componentsTableColumns2}
                                      scroll={{ y: 240 }}
                                      dataSource={dataTable3}
                                      pagination={false}
                                    ></Table>
                                  </TableWrapper>
                                </UserTableStyleWrapper>
                                {/* <p style={{ color: 'black', marginTop: '20px' }}>Select Games</p> */}
                              </Col>
                              <Col sm={8}>
                                <UserTableStyleWrapper>
                                  <TableWrapper className="table-responsive">
                                    <Table
                                      id="myTable"
                                      columns={gamesTableColumns2}
                                      scroll={{ y: 240 }}
                                      dataSource={dataTable4}
                                      pagination={false}
                                    ></Table>
                                  </TableWrapper>
                                </UserTableStyleWrapper>
                              </Col>
                            </Row>
                          </div>
                        </BasicFormWrapper>
                      ),
                    },
                  ]}
                  onNext={next}
                  onPrev={prev}
                  onDone={done}
                  flag={flag3}
                  isfinished={isFinished}
                />
              </WizardWrapper>
              <div>
                <p className="danger text-center" style={{ color: 'red' }}>
                  {flag2 ? error : ''}
                </p>
              </div>
            </Cards>
          </WizardBlock>
        </div>
      </Main>
      <Modal title="Assign Components" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
        <p>Are you sure to assign the components to the company :)</p>
      </Modal>
      <Modal
        title="Error in Selecting Admin Components"
        visible={isModalVisible2}
        onOk={handleOk2}
        onCancel={handleCancel}
      >
        <p>{adminText}</p>
      </Modal>
    </>
  );
};

export default AddQuestion;

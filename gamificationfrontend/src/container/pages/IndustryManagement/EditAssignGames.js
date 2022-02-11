import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Spin, Form, Alert, Image } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory, useParams } from 'react-router-dom';
import _ from 'lodash';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';

const EditAssignGames = () => {
  const { id } = useParams();
  const history = useHistory();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(false);
  const [disable, setDisable] = useState(true);
  const [disable2, setDisable2] = useState(true);
  const [groupName, setGroupName] = useState('');
  const [selectedGames, setSelectedGames] = useState([]);
  const [allGames, setAllGames] = useState([]);
  const [reference, setReference] = useState([]);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState('');
  const [groups, setGroups] = useState([]);
  const [selectedGroups, setSelectedGroups] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
  const [reference2, setReference2] = useState([]);
  const [games, setGames] = useState([]);
  const [flag, setFlag] = useState('');
  const [indutryDetails, setIndustryDetails] = useState([]);
  const [image, setImage] = useState('');
  const [visible, setVisible] = useState(false);
  const [state, setState] = useState({
    selectedRowKeys: 0,
    selectedRows: [],
    selectedRowKeys1: 0,
    selectedRows1: [],
  });

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
    let game = [];
    selectedGames.map(games => {
      game.push(games.key.toString());
    });
    let theme = [];
    indutryDetails.themes.map(t => {
      theme.push(t.id.toString());
    });
    setLoading2(true);
    const user2 = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const assignValues = {
      games: game,
      themes: theme,
    };
    var data = new FormData();
    data.append('userJson', JSON.stringify(assignValues));
    const URL = `${api}industryManagement/modifyIndustry/${id}`;
    var config = {
      method: 'put',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user2.accessToken}`,
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
          setAlertText('Games with same name already exists!');
          setAlertType('danger');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/industry',
            state: { detail: `Assigned Games Updates Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  useEffect(() => {
    getAllGames();
    getAllCompanies();
  }, []);

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
        console.log(rev);
        setGames(rev);
        rev.map(game => {
          const { id, gameName, thumbNailKey, assessment } = game;
          if (assessment === true) {
            gamesTableData.push({
              key: id,
              name: (
                <div className="user-info">
                  <figure>
                    <img
                      style={{ width: '40px' }}
                      src={
                        thumbNailKey === null
                          ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                          : thumbNailKey
                      }
                      // src={
                      //   'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                      // }
                      alt="gameImage"
                    />
                  </figure>
                  <figcaption>
                    <Heading className="user-name" as="h6">
                      {gameName}
                    </Heading>
                  </figcaption>
                </div>
              ),
              thumbnail: (
                <>
                  <Button
                    className="btn-icon"
                    to="#"
                    shape="circle"
                    onClick={() => {
                      setVisible(true);
                      setImage(thumbNailKey);
                    }}
                  >
                    <FeatherIcon icon="eye" size={16} />
                  </Button>
                </>
              ),
            });
          }
        });
        setReference(gamesTableData);
        setAllGames(gamesTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const getAllCompanies = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}industryManagement/industries`;
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
        let table = [];

        result.industries.map(industry => {
          if (industry.industryID == id) {
            setIndustryDetails(industry);
            industry.games.map(game => {
              table.push({
                key: game.id,
                name: (
                  <div className="user-info">
                    <figure>
                      <img
                        style={{ width: '40px' }}
                        src={
                          game.thumbNailKey === null
                            ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                            : game.thumbNailKey
                        }
                        alt="userImage"
                      />
                    </figure>
                    <figcaption>
                      <Heading className="user-name" as="h6">
                        {game.gameName}
                      </Heading>
                    </figcaption>
                  </div>
                ),
                thumbnail: (
                  <>
                    <Button
                      className="btn-icon"
                      to="#"
                      shape="circle"
                      onClick={() => {
                        setVisible(true);
                        setImage(game.thumbNailKey);
                      }}
                    >
                      <FeatherIcon icon="eye" size={16} />
                    </Button>
                  </>
                ),
              });
            });
          }
        });
        setSelectedGames(table);
        setReference2(table);
        setFlag(true);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  useEffect(() => {
    const sorted = _.differenceBy(allGames, selectedGames, 'key');
    setAllGames(sorted);
    setReference(sorted);
  }, [flag]);

  const gamesTableData = [];

  const gamesTableData2 = [];

  const gamesTableColumns = [
    {
      title: 'Games',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Thumbnail',
      dataIndex: 'thumbnail',
      key: 'thumbnail',
    },
  ];

  const addSelected = () => {
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys.map(key => {
      games.map(game => {
        if (game.id === key) {
          gamesTableData2.push({
            key: game.id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      game.thumbNailKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : game.thumbNailKey
                    }
                    alt="gameImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {game.gameName}
                  </Heading>
                </figcaption>
              </div>
            ),
            thumbnail: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    setVisible(true);
                    setImage(game.thumbNailKey);
                  }}
                >
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        }
      });
    });
    let merged = gamesTableData2.concat(selectedGames);
    setSelectedGames(merged);
    setReference2(merged);
    const sorted = _.differenceBy(allGames, gamesTableData2, 'key');
    setAllGames(sorted);
    setReference(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const removeSelected = () => {
    setDisable(true);
    setDisable2(true);
    state.selectedRowKeys1.map(key => {
      games.map(game => {
        if (game.id === key) {
          gamesTableData.push({
            key: game.id,
            name: (
              <div className="user-info">
                <figure>
                  <img
                    style={{ width: '40px' }}
                    src={
                      game.thumbNailKey === null
                        ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                        : game.thumbNailKey
                    }
                    alt="gameImage"
                  />
                </figure>
                <figcaption>
                  <Heading className="user-name" as="h6">
                    {game.gameName}
                  </Heading>
                </figcaption>
              </div>
            ),
            thumbnail: (
              <>
                <Button
                  className="btn-icon"
                  to="#"
                  shape="circle"
                  onClick={() => {
                    setVisible(true);
                    setImage(game.thumbNailKey);
                  }}
                >
                  <FeatherIcon icon="eye" size={16} />
                </Button>
              </>
            ),
          });
        }
      });
    });
    let merged = gamesTableData.concat(allGames);
    setAllGames(merged);
    setReference(merged);
    const sorted = _.differenceBy(selectedGames, gamesTableData, 'key');
    setSelectedGames(sorted);
    setReference2(sorted);
    setState({ selectedRowKeys: 0, selectedRows: [], selectedRowKeys1: 0, selectedRows1: [] });
  };

  const searchTable = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry =>
      entry.name.props.children[1].props.children.props.children.toLowerCase().includes(currValue),
    );
    setAllGames(filteredData);
  };

  const searchTable2 = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference2.filter(entry =>
      entry.name.props.children[1].props.children.props.children.toLowerCase().includes(currValue),
    );
    setSelectedGames(filteredData);
  };

  useEffect(() => {
    if (selectedGames.length > 0) {
      setLoading(false);
    } else {
      setLoading(true);
    }
  }, [selectedGames]);

  return (
    <>
      <PageHeader title="Edit Assign Games" />
      <Main>
        <div style={{ display: 'none' }}>
          <Image.PreviewGroup preview={{ visible, onVisibleChange: vis => setVisible(vis) }}>
            <Image src={image} />
          </Image.PreviewGroup>
        </div>
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
                <Form name="adduser" form={form} onFinish={handleSubmit} layout="vertical">
                  <Row gutter={25}>
                    <Col sm={10} xs={24}>
                      <UserTableStyleWrapper>
                        <p>Select Games</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            style={{ height: '300px' }}
                            id="allUser"
                            rowSelection={rowSelection}
                            scroll={{ y: 240, x: true }}
                            columns={gamesTableColumns}
                            dataSource={allGames}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search Games"
                          style={{ width: '50%', float: 'right', marginTop: '10px' }}
                          id="myInput"
                          // onKeyUp={() => {
                          //   filterFunction('allUser', 'myInput');
                          // }}
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
                        <p>Selected Games</p>
                        <TableWrapper className="table-responsive">
                          <Table
                            id="myTable"
                            style={{ height: '300px' }}
                            scroll={{ y: 240, x: true }}
                            rowSelection={rowSelection2}
                            columns={gamesTableColumns}
                            dataSource={selectedGames}
                            pagination={false}
                          />
                        </TableWrapper>
                        <Input
                          placeholder="Search Games"
                          style={{ width: '50%', height: '80%', float: 'right', marginTop: '10px' }}
                          id="myInput2"
                          // onKeyUp={() => {
                          //   filterFunction('myTable', 'myInput2');
                          // }}
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
                      {loading2 ? <Spin size="medium" /> : 'Update Assign Games'}
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
    </>
  );
};

export default EditAssignGames;

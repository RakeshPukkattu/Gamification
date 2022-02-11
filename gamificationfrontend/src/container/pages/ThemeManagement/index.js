import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Switch, Input, Form, Modal, Drawer, Collapse, Radio, Select, Alert, Image } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Main, ExportStyleWrap, TableWrapper } from '../../styled';
import { UserTableStyleWrapper } from '../style';
import FeatherIcon from 'feather-icons-react';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import Heading from '../../../components/heading/heading';
import { useHistory, useLocation } from 'react-router-dom';
import axios from 'axios';
import { setItem, getItem } from '../../../utility/localStorageControl';

const Dashboard = () => {
  const location = useLocation();
  const { Panel } = Collapse;
  const { Column } = Table;
  const history = useHistory();
  const [themeData, setThemeData] = useState([]);
  const [reference, setReference] = useState([]);
  const [showModal2, setShowModal2] = useState(false);
  const [showModal3, setShowModal3] = useState(false);
  const [id, setId] = useState('');
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(true);
  const [form] = Form.useForm();
  const [name, setName] = useState('');
  const [assign, setAssign] = useState('');
  const [visible2, setVisible2] = useState(false);
  const [mcq, setMcq] = useState('');
  const [mmcq, setMmcq] = useState(null);
  const [allUsers, setAllUsers] = useState([]);
  const [allGroups, setAllGroups] = useState([]);
  const [games, setGames] = useState([]);
  const [themes, setThemes] = useState([]);
  const [alertText, setAlertText] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [assignedUsers, setAssignedUsers] = useState([]);
  const [assignedGroups, setAssignedGroups] = useState([]);
  const [alertType, setAlertType] = useState('success');
  const [visible, setVisible] = useState(false);
  const [image, setImage] = useState('');

  useEffect(() => {
    if (location.state !== null && location.state !== undefined) {
      setShowAlert(true);
      setAlertText(location.state.detail);
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, []);

  useEffect(() => {
    getAllThemes();
  }, []);

  const getAllThemes = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}Themes/allThemes`;
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
        const rev = result.themes.reverse();
        console.log(rev);
        let themeTableData = [];
        rev.map(theme => {
          const { id, themeName, thumbNailKey, assessment, status } = theme;
          return themeTableData.push({
            key: id,
            name: themeName,
            view: (
              <>
                <div className="user-info">
                  <figure>
                    <img
                      onClick={() => {
                        setVisible(true);
                        setImage(thumbNailKey);
                      }}
                      style={{ width: '40px', cursor: 'pointer' }}
                      src={
                        thumbNailKey === null
                          ? 'https://media.istockphoto.com/vectors/avatar-5-vector-id1131164548?k=6&m=1131164548&s=612x612&w=0&h=3-7WOnmaUlfAmYIkDVHxcOZhgfl0AeMPOgbd3xgi48c='
                          : thumbNailKey
                      }
                      alt="userImage"
                    />
                  </figure>
                </div>
              </>
            ),
            //name: themeName,
            enableDisable: (
              <>
                <div className="notification-list-single">
                  <p>
                    <Switch
                      // onChange={e => {
                      //   if (isAssigned) {
                      //     setShowAlert(true);
                      //     setAlertText(`${themeName} has been Assigned`);
                      //     setAlertType('error');
                      //     setTimeout(() => {
                      //       setShowAlert(false);
                      //       setAlertType('success');
                      //     }, 3000);
                      //   } else {
                      //     updateStatus(e, themeID, themeName);
                      //   }
                      // }}
                      onChange={e => {
                        updateStatus(e, id, themeName);
                      }}
                      checked={status}
                      style={{ marginLeft: '30px' }}
                      //disabled={isAssigned}
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
                  onClick={() => {
                    editTheme(id);
                  }}
                  //disabled={enableDisableStatus === true ? (isAssigned === false ? false : true) : true}
                >
                  <FeatherIcon icon="edit" size={16} />
                </Button>
              </>
            ),
          });
        });
        setThemeData(themeTableData);
        setReference(themeTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const updateStatus = (e, id, name) => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}Themes/modifyThemeStatus/${id}`;
    // const userValues = {
    //   status: e,
    // };
    var data = new FormData();
    data.append('status', e);
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
        getAllThemes();
        setShowAlert(true);
        setAlertType('success');
        setAlertText(`${name} status successfully Updated`);
        setTimeout(() => {
          setShowAlert(false);
        }, 3000);
        const result = response.data;
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const themeTableColumns = [
    {
      title: 'Theme Name',
      dataIndex: 'name',
      key: 'name',
      width: 150,
    },
    {
      title: 'Thumbnail',
      dataIndex: 'view',
      key: 'view',
      width: 200,
    },
    {
      title: 'Enable/Disable',
      dataIndex: 'enableDisable',
      key: 'enableDisable',
      width: 150,
      align: 'center',
    },
    {
      title: 'Modify',
      dataIndex: 'modify',
      key: 'modify',
      width: 150,
      align: 'center',
    },
  ];

  const filterFunction = e => {
    const currValue = e.target.value.toLowerCase();
    const filteredData = reference.filter(entry => entry.name.toLowerCase().includes(currValue));
    setThemeData(filteredData);
  };

  const editTheme = id => {
    history.push(`/admin/edittheme/${id}`);
  };

  const addTheme = () => {
    history.push(`/admin/addtheme`);
  };

  return (
    <>
      <PageHeader title="Theme Management Dashboard" />
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
                    marginBottom: '10px',
                    width: '100%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    position: 'absolute',
                    marginLeft: '20px',
                  }}
                >
                  {showAlert ? <Alert message={alertText} type={alertType} showIcon /> : ''}
                </div>
                <div className="sDash_export-box" style={{ marginBottom: '20px' }}>
                  <div>
                    <Button
                      className="btn-export"
                      type="success"
                      size="medium"
                      onClick={addTheme}
                      style={{ marginLeft: '10px' }}
                    >
                      Add Theme
                    </Button>
                  </div>
                  <div>
                    <Input
                      placeholder="Search Theme Name"
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
                      columns={themeTableColumns}
                      scroll={{ y: 340, x: 600 }}
                      dataSource={themeData}
                      pagination={{
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '30', '50'],
                        defaultPageSize: 10,
                        total: themeData.length,
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
    </>
  );
};

export default Dashboard;

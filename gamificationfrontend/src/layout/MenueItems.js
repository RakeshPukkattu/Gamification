import React, { useEffect, useState } from 'react';
import { Menu } from 'antd';
import { NavLink, useRouteMatch } from 'react-router-dom';
import FeatherIcon from 'feather-icons-react';
import propTypes from 'prop-types';
import MainCard from '../components/cards/sampleCard/MainCard';
import { getItem } from '../utility/localStorageControl';

const { SubMenu } = Menu;

const MenuItems = ({ toggleCollapsed, topMenu, events }) => {
  const { path } = useRouteMatch();
  const [role, setRole] = useState(true);
  const [role2, setRole2] = useState(true);

  const pathName = window.location.pathname;
  const pathArray = pathName.split(path);
  const mainPath = pathArray[1];
  const mainPathSplit = mainPath.split('/');

  const [openKeys, setOpenKeys] = React.useState(
    !topMenu ? [`${mainPathSplit.length > 2 ? mainPathSplit[1] : 'dashboard'}`] : [],
  );

  const onOpenChange = keys => {
    setOpenKeys(keys[keys.length - 1] !== 'recharts' ? [keys.length && keys[keys.length - 1]] : keys);
  };

  const onClick = item => {
    if (item.keyPath.length === 1) setOpenKeys([]);
  };

  useEffect(() => {
    const user = getItem('user');
    let role = user.role.replace(/['"]+/g, '');
    console.log('vdfkn', role);
    if (role.toString().toLowerCase() === 'learner') {
      setRole(false);
    }
    if (role.toString() === 'SuperAdmin') {
      setRole2(false);
    }
  }, []);

  return (
    <Menu
      onOpenChange={onOpenChange}
      onClick={onClick}
      mode={!topMenu || window.innerWidth <= 991 ? 'inline' : 'horizontal'}
      defaultSelectedKeys={
        !topMenu
          ? [
              `${
                mainPathSplit.length === 1 ? 'home' : mainPathSplit.length === 2 ? mainPathSplit[1] : mainPathSplit[2]
              }`,
            ]
          : []
      }
      defaultOpenKeys={!topMenu ? [`${mainPathSplit.length > 2 ? mainPathSplit[1] : 'dashboard'}`] : []}
      overflowedIndicator={<FeatherIcon icon="more-vertical" />}
      openKeys={openKeys}
    >
      {role ? (
        <>
          <Menu.Item
            key="home"
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}`}>
                  <FeatherIcon icon="home" />
                </NavLink>
              )
            }
          >
            <NavLink onClick={toggleCollapsed} to={`${path}`}>
              Dashboard
            </NavLink>
          </Menu.Item>
          {role2 ? (
            ''
          ) : (
            <Menu.Item
              icon={
                !topMenu && (
                  <NavLink className="menuItem-iocn" to={`${path}/company`}>
                    <FeatherIcon icon="globe" />
                  </NavLink>
                )
              }
              key="company"
            >
              <NavLink onClick={toggleCollapsed} to={`${path}/company`}>
                Company Management
              </NavLink>
            </Menu.Item>
          )}
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/user`}>
                  <FeatherIcon icon="user" />
                </NavLink>
              )
            }
            key="User Management"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/user`}>
              User Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/group`}>
                  <FeatherIcon icon="users" />
                </NavLink>
              )
            }
            key="group"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/group`}>
              Group Management
            </NavLink>
          </Menu.Item>
          <SubMenu key="session" icon={!topMenu && <FeatherIcon icon="sliders" />} title="Session Management">
            <Menu.Item key="user">
              <NavLink onClick={toggleCollapsed} to={`${path}/sessions/user`}>
                For Users
              </NavLink>
            </Menu.Item>
            <Menu.Item key="groups">
              <NavLink onClick={toggleCollapsed} to={`${path}/session`}>
                For Groups
              </NavLink>
            </Menu.Item>
          </SubMenu>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/country`}>
                  <FeatherIcon icon="aperture" />
                </NavLink>
              )
            }
            key="country"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/country`}>
              Country Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/survey`}>
                  <FeatherIcon icon="activity" />
                </NavLink>
              )
            }
            key="survey"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/survey`}>
              Survey Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/industry`}>
                  <FeatherIcon icon="briefcase" />
                </NavLink>
              )
            }
            key="industry"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/industry`}>
              Industry Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/game`}>
                  <FeatherIcon icon="airplay" />
                </NavLink>
              )
            }
            key="game"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/game`}>
              Game Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/theme`}>
                  <FeatherIcon icon="award" />
                </NavLink>
              )
            }
            key="theme"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/theme`}>
              Theme Management
            </NavLink>
          </Menu.Item>

          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/bonus`}>
                  <FeatherIcon icon="archive" />
                </NavLink>
              )
            }
            key="bonus"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/bonus`}>
              Bonus Management
            </NavLink>
          </Menu.Item>
          <Menu.Item
            icon={
              !topMenu && (
                <NavLink className="menuItem-iocn" to={`${path}/achievement`}>
                  <FeatherIcon icon="book-open" />
                </NavLink>
              )
            }
            key="achievement"
          >
            <NavLink onClick={toggleCollapsed} to={`${path}/achievement`}>
              Achievements
            </NavLink>
          </Menu.Item>
        </>
      ) : (
        ''
      )}
    </Menu>
  );
};

MenuItems.propTypes = {
  topMenu: propTypes.bool,
  toggleCollapsed: propTypes.func,
  events: propTypes.object,
};

export default MenuItems;

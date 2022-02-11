import React, { useEffect, useState } from 'react';
import { Row, Col, Skeleton } from 'antd';
import { PageHeader } from '../../components/page-headers/page-headers';
import { Main } from '../styled';

import MainCard from '../../components/cards/sampleCard/MainCard';
import { getItem, removeItem } from '../../utility/localStorageControl';

const UserDashboard = () => {
  window.onpopstate = function(event) {
    // "event" object seems to contain value only when the back button is clicked
    // and if the pop state event fires due to clicks on a button
    // or a link it comes up as "undefined"

    if (event) {
      //alert('Hello');
    } else {
      //alert('vkfnv');
      // Continue user action through link or button
    }
  };

  // if (window.performance) {
  //   if (performance.navigation.type == 1) {
  //     alert('This page is reloaded');
  //   } else {
  //     alert('This page is not reloaded');
  //   }
  // }

  window.onbeforeunload = function(e) {
    console.log('Logging out');
    var e = e || window.event;
    removeItem('user');
    // var msg = 'Do you really want to leave this page?';

    // // For IE and Firefox
    // if (e) {
    //   e.returnValue = msg;
    // }

    // // For Safari / chrome
    // return msg;
  };

  const [role, setRole] = useState(true);
  const [role2, setRole2] = useState(true);

  useEffect(() => {
    const user = getItem('user');
    let role = user.role.replace(/['"]+/g, '');
    if (role.toString().toLowerCase() === 'learner') {
      setRole(false);
    }
    if (role.toString() === 'SuperAdmin') {
      setRole2(false);
    }
  }, []);

  const content = [
    {
      id: 1,
      title: 'User Management',
      content: 'Manage Your Users',
      img: 'static/img/icon/1.svg',
      className: 'primary',
      link: '/admin/user',
    },
    {
      id: 2,
      title: 'Group Management',
      content: 'Manage Your Groups',
      img: 'static/img/icon/2.svg',
      className: 'secondary',
      link: '/admin/group',
    },
    {
      id: 3,
      title: 'Session Management',
      content: 'Manage Your Sessions',
      img: 'static/img/icon/4.svg',
      className: 'success',
      link: '/admin/session',
    },
    {
      id: 4,
      title: 'Country Management',
      content: 'Manage Your Countries and Languages',
      img: 'static/img/icon/3.svg',
      className: 'warning',
      link: '/admin/country',
    },
    {
      id: 5,
      title: 'Survey Management',
      content: 'Manage Your Survey',
      img: 'static/img/icon/1.svg',
      className: 'primary',
      link: '/admin/survey',
    },
    {
      id: 6,
      title: 'Industry Management',
      content: 'Manage Your Survey',
      img: 'static/img/icon/2.svg',
      className: 'secondary',
      link: '/admin/industry',
    },
    {
      id: 7,
      title: 'Game Management',
      content: 'Manage Your Games',
      img: 'static/img/icon/1.svg',
      className: 'success',
      link: '/admin/game',
    },
    {
      id: 8,
      title: 'Theme Management',
      content: 'Manage Your Themes',
      img: 'static/img/icon/2.svg',
      className: 'primary',
      link: '/admin/theme',
    },
    {
      id: 9,
      title: 'Bonus Management',
      content: 'Manage Your Bonus Questions',
      img: 'static/img/icon/1.svg',
      className: 'success',
      link: '/admin/bonus',
    },
    {
      id: 10,
      title: 'Achievements Management',
      content: 'Manage Your Achievements',
      img: 'static/img/icon/2.svg',
      className: 'primary',
      link: '/admin/achievement',
    },
  ];
  const content2 = [
    {
      id: 1,
      title: 'Company Management',
      content: 'Manage the Companies',
      img: 'static/img/icon/chat.svg',
      className: 'primary',
      link: '/admin/company',
    },
    // {
    //   id: 2,
    //   title: 'Assign Component Management',
    //   content: 'Manage the assignment of components',
    //   img: 'static/img/icon/2.svg',
    //   className: 'secondary',
    //   link: '/admin/assigncomponent',
    // },
  ];
  return (
    <>
      {role ? (
        <>
          <PageHeader title="Welcome to Gamification Admin Dashboard" />
          <Main style={{ height: '100vh' }}>
            <Row gutter={25}>
              {role2 ? (
                ''
              ) : (
                <>
                  {content2.map(item => {
                    return (
                      <Col key={item.id} xxl={4} md={8} sm={12} xs={24}>
                        <MainCard item={item} />
                      </Col>
                    );
                  })}
                </>
              )}
              {content.map(item => {
                return (
                  <Col key={item.id} xxl={4} md={8} sm={12} xs={24}>
                    <MainCard item={item} />
                  </Col>
                );
              })}
            </Row>
          </Main>
        </>
      ) : (
        <>
          <PageHeader title="Welcome Learner" />
          <Main style={{ height: '100vh' }}></Main>
        </>
      )}
    </>
  );
};

export default UserDashboard;

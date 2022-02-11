import React, { useEffect, useState } from 'react';
// eslint-disable-next-line import/no-extraneous-dependencies
import { hot } from 'react-hot-loader/root';
import { Provider, useSelector, useDispatch } from 'react-redux';
import { ThemeProvider } from 'styled-components';
import { BrowserRouter as Router, Redirect, Route, useHistory } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import Admin from './routes/admin';
import AuthLayout from './routes/auth';
import './static/css/style.css';
import config from './config/config';
import store from './redux/store';
import ProtectedRoute from './components/utilities/protectedRoute';
import { autoLogin } from './redux/authentication/actionCreator';
import { getItem, setItem } from './utility/localStorageControl';

const { theme } = config;

const RoutesPath = () => {
  const dispatch = useDispatch();
  const { isLoggedIn } = useSelector(state => {
    return {
      isLoggedIn: state.auth.login,
    };
  });

  const currentUser = getItem('user');
  const history = useHistory();

  const [path, setPath] = useState(window.location.pathname);

  useEffect(() => {
    let unmounted = false;
    if (!unmounted) {
      setPath(window.location.pathname);
    }
    // eslint-disable-next-line no-return-assign
    return () => (unmounted = true);
  }, [setPath]);

  // useEffect(() => {
  //   if (currentUser) {
  //     if (currentUser.email) {
  //       dispatch(autoLogin(currentUser));
  //     } else if (currentUser.email === null) {
  //       // eslint-disable-next-line no-unused-expressions
  //       history?.push('/');
  //     }
  //   }
  // }, [currentUser, history]);

  return (
    <>
      {!isLoggedIn ? <Route path="/" component={AuthLayout} /> : <ProtectedRoute path="/admin" component={Admin} />}
      {isLoggedIn && (path === process.env.PUBLIC_URL || path === `${process.env.PUBLIC_URL}/`) && (
        <Redirect to="/admin" />
      )}
    </>
  );
};

const ProviderConfig = () => {
  const { rtl, topMenu, darkMode } = useSelector(state => {
    return {
      isLoggedIn: state.auth.login,
    };
  });

  return (
    <ConfigProvider>
      <ThemeProvider theme={{ ...theme }}>
        <Router basename={process.env.PUBLIC_URL}>
          <RoutesPath />
          {/* <Route path="/" component={Auth} />
          <Route path="/admin" component={Admin} /> */}
        </Router>
      </ThemeProvider>
    </ConfigProvider>
  );
};

function App() {
  return (
    <Provider store={store}>
      <ProviderConfig />
    </Provider>
  );
}

export default hot(App);

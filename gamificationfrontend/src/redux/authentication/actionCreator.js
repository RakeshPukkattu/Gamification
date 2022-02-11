import actions from './actions';

import { setItem, removeItem, getItem } from '../../utility/localStorageControl';
import axios from 'axios';

const {
  logoutBegin,
  logoutSuccess,
  logoutErr,

  autoLogin: autoLoginAction,
  setUserAuthToken,
  resetUserAuthToken,
} = actions;

const logOut = location => {
  return async dispatch => {
    try {
      dispatch(logoutBegin());
      removeItem('access_token');
      const user = getItem('user');
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
        },
        data: values,
      };
      axios(config)
        .then(function(response) {
          setTimeout(() => {
            location.reload();
          }, 20);
        })
        .catch(function(error) {
          console.log(error);
        });
      dispatch(resetUserAuthToken());
      dispatch(logoutSuccess(null));
    } catch (err) {
      dispatch(logoutErr(err));
    }
  };
};

const autoLogin = user => {
  return async dispatch => {
    dispatch(autoLoginAction(user));
    // const {
    //   accessToken: { jwtToken },
    // } = await Auth.currentSession();
    const jwtToken = user.accessToken;
    setItem('access_token', jwtToken);
    return dispatch(setUserAuthToken(jwtToken));
  };
};

export { logOut, autoLogin };

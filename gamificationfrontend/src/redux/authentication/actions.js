const actions = {
  LOGIN_BEGIN: 'LOGIN_BEGIN',
  LOGIN_SUCCESS: 'LOGIN_SUCCESS',
  LOGIN_ERR: 'LOGIN_ERR',

  LOGOUT_BEGIN: 'LOGOUT_BEGIN',
  LOGOUT_SUCCESS: 'LOGOUT_SUCCESS',
  LOGOUT_ERR: 'LOGOUT_ERR',

  AUTO_LOGIN: 'AUTO_LOGIN',
  CLEAR_AUTH_MSG: 'CLEAR_AUTH_MSG',

  SET_USER_AUTH_TOKEN: 'SET_USER_AUTH_TOKEN',
  RESET_USER_AUTH_TOKEN: 'RESET_USER_AUTH_TOKEN',

  loginBegin: () => {
    return {
      type: actions.LOGIN_BEGIN,
    };
  },

  loginSuccess: data => {
    return {
      type: actions.LOGIN_SUCCESS,
      data,
    };
  },

  loginErr: err => {
    return {
      type: actions.LOGIN_ERR,
      err,
    };
  },

  logoutBegin: () => {
    return {
      type: actions.LOGOUT_BEGIN,
    };
  },

  logoutSuccess: data => {
    return {
      type: actions.LOGOUT_SUCCESS,
      data,
    };
  },

  logoutErr: err => {
    return {
      type: actions.LOGOUT_ERR,
      err,
    };
  },

  clearAuthMessage: () => {
    return {
      type: actions.CLEAR_AUTH_MSG,
    };
  },

  autoLogin: data => {
    return {
      type: actions.AUTO_LOGIN,
      data,
    };
  },

  setUserAuthToken: token => {
    return {
      type: actions.SET_USER_AUTH_TOKEN,
      token,
    };
  },

  resetUserAuthToken: () => {
    return {
      type: actions.RESET_USER_AUTH_TOKEN,
    };
  },
};

export default actions;

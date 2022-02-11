import Cookies from 'js-cookie';
import actions from './actions';

const {
  LOGIN_BEGIN,
  LOGIN_SUCCESS,
  LOGIN_ERR,
  LOGOUT_BEGIN,
  LOGOUT_SUCCESS,
  LOGOUT_ERR,

  CLEAR_AUTH_MSG,
  AUTO_LOGIN,
  SET_USER_AUTH_TOKEN,
  RESET_USER_AUTH_TOKEN,
  TEMP_AUTH_INFO,
} = actions;

const initState = {
  user: null,
  loading: false,
  error: null,
  otp: null,
  msg: null,
  to: null,
  token: null,
  logoutDone: false,
  username: null,
  password: null,
};

/**
 *
 * @todo impure state mutation/explaination
 */
const AuthReducer = (state = initState, action) => {
  const { type, data, err, user, otp } = action;
  switch (type) {
    case LOGIN_BEGIN:
      return {
        ...state,
        loading: true,
        logoutDone: false,
      };
    case LOGIN_SUCCESS:
      return {
        ...state,
        user: data,
        loading: false,
      };
    case LOGIN_ERR:
      return {
        ...state,
        error: err,
        loading: false,
      };
    case LOGOUT_BEGIN:
      return {
        ...state,
        loading: true,
      };
    case LOGOUT_SUCCESS:
      return {
        ...state,
        loading: false,
        user: null,
        otp: null,
        token: null,
        logoutDone: true,
      };
    case LOGOUT_ERR:
      return {
        ...state,
        error: err,
        loading: false,
      };

    case CLEAR_AUTH_MSG:
      return {
        ...state,
        to: null,
        msg: null,
      };
    case AUTO_LOGIN:
      return {
        ...state,
        loading: false,
        user: data,
        login: data,
        logoutDone: false,
      };
    case SET_USER_AUTH_TOKEN:
      return {
        ...state,
        token: data,
        logoutDone: false,
      };
    case RESET_USER_AUTH_TOKEN:
      return {
        ...state,
        token: null,
        logoutDone: false,
      };
    default:
      return state;
  }
};
export default AuthReducer;

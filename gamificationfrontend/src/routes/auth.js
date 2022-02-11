import React, { lazy, Suspense } from 'react';
import { Spin } from 'antd';
import { Switch, Route, Redirect } from 'react-router-dom';

import Login from '../container/profile/authentication/overview/SignIn';
import Signup from '../container/profile/authentication/overview/Signup';
import CreatePass from '../container/profile/authentication/overview/CreatePassword';
import ForgotPass from '../container/profile/authentication/overview/ForgotPassword';

const NotFound = () => {
  return <Redirect to="/" />;
};

const FrontendRoutes = () => {
  return (
    <Switch>
      {/* <Suspense
        fallback={
          <div className="spin">
            <Spin />
          </div>
        }
      > */}
      <Route exact path="/createPassword" component={CreatePass} />
      <Route exact path="/forgotPassword" component={ForgotPass} />
      <Route exact path="/" component={Login} />
      <Route exact path="/register" component={Signup} />
      <Route exact path="*" component={NotFound} />
      {/* </Suspense> */}
    </Switch>
  );
};

export default FrontendRoutes;

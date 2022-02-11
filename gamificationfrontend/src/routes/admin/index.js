import React, { lazy, Suspense } from 'react';
import { Spin } from 'antd';
import { Switch, Route, useRouteMatch } from 'react-router-dom';
import withAdminLayout from '../../layout/withAdminLayout';

import AdminHomepage from '../../container/pages/AdminHomepage';

import UserManagement from '../../container/pages/UserManagement';
import AddUser from '../../container/pages/UserManagement/AddUser';
import AddExcel from '../../container/pages/UserManagement/AddExcel';

import AddGroupManagement from '../../container/pages/GroupManagement/AddGroup';
import EditGroupManagement from '../../container/pages/GroupManagement/EditGroup';
import CloneGroupManagement from '../../container/pages/GroupManagement/CloneGroup';
import GroupManagement from '../../container/pages/GroupManagement';

import AddSessionManagement from '../../container/pages/SessionManagement/AddSession';
import EditSessionManagement from '../../container/pages/SessionManagement/EditSession';
import SessionManagement from '../../container/pages/SessionManagement';

import AddUserSessionManagement from '../../container/pages/SessionManagement/UserSession/AddSession';
import EditUserSessionManagement from '../../container/pages/SessionManagement/UserSession/EditSession';
import UserSessionManagement from '../../container/pages/SessionManagement/UserSession';

import CountryLanguageManagement from '../../container/pages/CountryLanguageManagement';

import AddQuestions from '../../container/pages/SurveyManagement/AddQuestion';
import AddMMCQQuestions from '../../container/pages/SurveyManagement/AddMMCQuestion';
import AddDropdownQuestions from '../../container/pages/SurveyManagement/AddDropdownQuestion';
import EditQuestions from '../../container/pages/SurveyManagement/EditSurvey';
import SurveyManagement from '../../container/pages/SurveyManagement';
import QuestionSorting from '../../container/pages/SurveyManagement/QuestionSorting';
import Assign from '../../container/pages/SurveyManagement/Assign';
import EditAssign from '../../container/pages/SurveyManagement/EditAssign';
import CompanyManagement from '../../container/pages/CompanyManagement';
import AddCompany from '../../container/pages/CompanyManagement/AddCompany';
import EditCompany from '../../container/pages/CompanyManagement/EditCompany';
import AssignComponent from '../../container/pages/AssignComponent/index';
import EditAssignComponent from '../../container/pages/AssignComponent/edit';

import IndustryManagement from '../../container/pages/IndustryManagement';
import AssignGamesComponent from '../../container/pages/IndustryManagement/Assign';
import AssignThemesComponent from '../../container/pages/IndustryManagement/AssignTheme';
import AddGameComponent from '../../container/pages/GameManagement/AddGame';
import AddThemeComponent from '../../container/pages/ThemeManagement/AddTheme';
import EditAssignGameComponent from '../../container/pages/IndustryManagement/EditAssignGames';
import EditAssignThemeComponent from '../../container/pages/IndustryManagement/EditAssignThemes';

import GameManagement from '../../container/pages/GameManagement';
import ThemeManagement from '../../container/pages/ThemeManagement';
import EditGame from '../../container/pages/GameManagement/EditGame';
import EditTheme from '../../container/pages/ThemeManagement/EditTheme';

import BonusManagement from '../../container/pages/BonusManagement';
import AddBonusQuestion from '../../container/pages/BonusManagement/AddQuestion';
import AssignBonus from '../../container/pages/BonusManagement/Assign';
import EditAssignBonus from '../../container/pages/BonusManagement/EditAssign';

import AchievementManagement from '../../container/pages/AchievmentManagement';

const Admin = () => {
  const { path } = useRouteMatch();

  return (
    <Switch>
      {/* <Suspense
        fallback={
          <div className="spin">
            <Spin />
          </div>
        }
      > */}
      <Route path={path} exact component={AdminHomepage} />
      <Route path={`${path}/user`} component={UserManagement} />
      <Route path={`${path}/adduser`} component={AddUser} />
      <Route path={`${path}/addexcel`} component={AddExcel} />
      <Route path={`${path}/addgroup`} component={AddGroupManagement} />
      <Route path={`${path}/editgroup/:id`} component={EditGroupManagement} />
      <Route path={`${path}/clonegroup/:id`} component={CloneGroupManagement} />
      <Route path={`${path}/group`} component={GroupManagement} />
      <Route path={`${path}/addsession`} component={AddSessionManagement} />
      <Route path={`${path}/editsession/:id`} component={EditSessionManagement} />
      <Route path={`${path}/session`} component={SessionManagement} />
      <Route path={`${path}/addusersession`} component={AddUserSessionManagement} />
      <Route path={`${path}/editusersession/:id`} component={EditUserSessionManagement} />
      <Route path={`${path}/sessions/user`} component={UserSessionManagement} />
      <Route path={`${path}/country`} component={CountryLanguageManagement} />
      <Route path={`${path}/survey`} component={SurveyManagement} />
      <Route path={`${path}/addQuestion`} component={AddQuestions} />
      <Route path={`${path}/addmmcqQuestion`} component={AddMMCQQuestions} />
      <Route path={`${path}/adddropdownQuestion`} component={AddDropdownQuestions} />
      <Route path={`${path}/editQuestion/:id`} component={EditQuestions} />
      <Route path={`${path}/sort/:id`} component={QuestionSorting} />
      <Route path={`${path}/assign/:id`} component={Assign} />
      <Route path={`${path}/editassign/:id`} component={EditAssign} />
      <Route path={`${path}/company`} component={CompanyManagement} />
      <Route path={`${path}/addcompany`} component={AddCompany} />
      <Route path={`${path}/editcompany/:id`} component={EditCompany} />
      <Route path={`${path}/assigncomponent/:id`} component={AssignComponent} />
      <Route path={`${path}/editassigncomponent/:id`} component={EditAssignComponent} />
      <Route path={`${path}/industry`} component={IndustryManagement} />
      <Route path={`${path}/assigngamescomponent/:id`} component={AssignGamesComponent} />
      <Route path={`${path}/assignthemescomponent/:id`} component={AssignThemesComponent} />
      <Route path={`${path}/addgame`} component={AddGameComponent} />
      <Route path={`${path}/addtheme`} component={AddThemeComponent} />
      <Route path={`${path}/editassigngamescomponent/:id`} component={EditAssignGameComponent} />
      <Route path={`${path}/editassignthemescomponent/:id`} component={EditAssignThemeComponent} />
      <Route path={`${path}/game`} component={GameManagement} />
      <Route path={`${path}/theme`} component={ThemeManagement} />
      <Route path={`${path}/editgame/:id`} component={EditGame} />
      <Route path={`${path}/edittheme/:id`} component={EditTheme} />
      <Route path={`${path}/bonus`} component={BonusManagement} />
      <Route path={`${path}/addBonusQuestion`} component={AddBonusQuestion} />
      <Route path={`${path}/assignbonus/:id`} component={AssignBonus} />
      <Route path={`${path}/editassignbonus/:id`} component={EditAssignBonus} />
      <Route path={`${path}/achievement`} component={AchievementManagement} />

      {/* </Suspense> */}
    </Switch>
  );
};

export default withAdminLayout(Admin);

import React, { useState, useEffect } from 'react';
import { Modal, Input, Form, Spin } from 'antd';
import { Button } from '../../../components/buttons/buttons';
import { useHistory } from 'react-router-dom';
import { getItem } from '../../../utility/localStorageControl';
import axios from 'axios';
import { Link } from 'react-router-dom';

const AddExcel = props => {
  const history = useHistory();

  useEffect(() => {
    // console.log('in add excel');
    // setFlag(false);
    const user = getItem('user');
    let role = user.role.replace(/['"]+/g, '');
    //setFlag(false);
    console.log(flag);
  }, [props.flag2]);

  return <></>;
};

export default AddExcel;

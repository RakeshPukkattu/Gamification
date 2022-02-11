import React, { useState, useEffect, Component } from 'react';
import { Modal, Input, Form, Spin } from 'antd';
import { Button } from '../../../components/buttons/buttons';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';
import CreatableInputOnly from './AddLanguage';
import CreatableSelect from 'react-select/creatable';

const components = {
  DropdownIndicator: null,
};

const createOption = label => ({
  label,
  value: label,
});

const AddCountryLanguage = props => {
  const { singleCountry } = props;
  const history = useHistory();
  const [form] = Form.useForm();
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loading2, setLoading2] = useState(true);
  const [name, setName] = useState('');

  const [state, setState] = useState({
    inputValue: '',
    value: [],
  });

  useEffect(() => {
    form.setFieldsValue({
      country: singleCountry === null ? undefined : singleCountry.countryName,
    });
    let lang = [];
    if (singleCountry !== null) {
      setName(singleCountry.countryName);
      singleCountry.languages.map(lan => {
        lang.push({
          label: lan.languageName,
          value: lan.languageName,
        });
      });
    }
    setState({
      ...state,
      inputValue: '',
      value: lang,
    });
  }, [singleCountry]);

  const handleSubmit = values => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let language = [];
    state.value.map(lan => {
      language.push(lan.value);
    });
    const userValues = {
      country: values.country,
      languages: language,
    };
    console.log('countries', userValues);
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
    const URL =
      singleCountry === null
        ? `${api}countrylanguageManagement/addCountryAndLanguages`
        : `${api}countrylanguageManagement/modifyCountryAndLanguages/${singleCountry.countryID}`;
    console.log(URL);
    const method = singleCountry === null ? 'post' : 'put';
    var config = {
      method: method,
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user.accessToken}`,
      },
      data: data,
    };
    axios(config)
      .then(function(response) {
        setLoading(false);
        console.log(response.data);
        const result = response.data;
        if (result.code.includes('406')) {
          setFlag(true);
          setError(result.message);
        } else {
          form.setFieldsValue({
            country: '',
          });
          setState({
            ...state,
            inputValue: '',
            value: [],
          });
          singleCountry === null
            ? props.onAddEditCountry(`${values.country} has been successfully Added.`)
            : props.onAddEditCountry(`${values.country} has been successfully Updated.`);
          props.onCancel();
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleChange = e => {
    setName(e.target.value);
    setFlag(false);
  };

  useEffect(() => {
    if (name === '' || state.value.length === 0) {
      setLoading2(true);
    } else {
      setLoading2(false);
    }
  }, [name, state.value]);

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  const handleChange5 = value => {
    setState({ ...state, value });
  };
  const handleInputChange = inputValue => {
    setState({ ...state, inputValue });
  };
  const handleKeyDown = event => {
    const { inputValue } = state;
    if (!inputValue) return;
    switch (event.key) {
      case 'Enter':
      case 'Tab':
        if (state.value.length === 0) {
          setState({
            ...state,
            inputValue: '',
            value: [...state.value, createOption(inputValue)],
          });
        } else {
          const found = state.value.some(lan => lan.value.toLowerCase() === inputValue.toLowerCase());
          console.log(found);
          if (found) {
            console.log('Already Present');
          } else {
            setState({
              ...state,
              inputValue: '',
              value: [...state.value, createOption(inputValue)],
            });
          }
        }
        event.preventDefault();
    }
  };

  return (
    <>
      <Modal
        title={props.title}
        wrapClassName={props.wrapClassName}
        visible={props.visible}
        footer={null}
        onCancel={props.onCancel}
      >
        <Form name="AddCountryLanguage" form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item
            name="country"
            label="Country Name"
            rules={[{ message: 'Please select one country', required: true }]}
          >
            <Input placeholder="Country Name" onChange={handleChange} onKeyPress={specialCharacter} maxlength="40" />
          </Form.Item>

          <Form.Item name="languages" label="Add Language">
            <CreatableSelect
              components={components}
              inputValue={state.inputValue}
              isClearable
              isMulti
              menuIsOpen={false}
              onChange={handleChange5}
              onInputChange={handleInputChange}
              onKeyDown={handleKeyDown}
              placeholder="Type Language and press enter..."
              value={state.value}
            />
            <p style={{ fontSize: '13px', marginTop: '5px', color: 'gray' }}>
              Press Enter ot Tab after writing the language name
            </p>
            <div style={{ display: 'none' }}>
              <CreatableInputOnly />
            </div>
          </Form.Item>

          <div className="sDash-button-grp">
            <Button
              className="btn-signin"
              htmlType="submit"
              type={loading2 ? '' : 'primary'}
              size="large"
              disabled={loading2}
            >
              {loading ? <Spin size="medium" /> : singleCountry === null ? 'Create' : 'Update'}
            </Button>
          </div>
          <div>
            <p className="danger text-center" style={{ color: 'red' }}>
              {flag ? error : ''}
            </p>
          </div>
        </Form>
      </Modal>
    </>
  );
};

export default AddCountryLanguage;

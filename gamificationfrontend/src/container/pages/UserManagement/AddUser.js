import React, { useState, useEffect } from 'react';
import { Modal, Select, Input, Form } from 'antd';
import { Button } from '../../../components/buttons/buttons';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getItem } from '../../../utility/localStorageControl';

const AddUser = props => {
  const { singleUser } = props;
  const history = useHistory();
  const [form] = Form.useForm();
  const [error, setError] = useState('');
  const [flag, setFlag] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loading2, setLoading2] = useState(true);
  const [selectedCountry, setSelectedCountry] = useState();
  const [selectedRegion, setSelectedRegion] = useState();
  const [selectedState, setSelectedState] = useState();
  const [selectedCity, setSelectedCity] = useState();
  const [selectedCountryID, setSelectedCountryID] = useState();
  const [selectedRegionID, setSelectedRegionID] = useState();
  const [selectedStateID, setSelectedStateID] = useState();
  const [selectedCityID, setSelectedCityID] = useState();
  const [email, setEmail] = useState('');
  const [countries, setCountries] = useState([]);
  const [roles, setRoles] = useState([]);
  const [name, setName] = useState('');
  const [lastName, setLastName] = useState('');
  const [user, setUser] = useState(null);

  useEffect(() => {
    setUser(singleUser);
  }, [singleUser]);

  useEffect(() => {
    if (singleUser === null) {
      form.setFieldsValue({
        firstName: '',
        middleName: '',
        lastName: '',
        email: '',
        //selectRole: '',
        selectCountry: '',
        selectState:'',
        selectRegion: '',
        selectState: '',
        selectCity: '',
      });
    } else {
      if (user !== null) {
        if (user === singleUser) {
          setLoading2(true);
        } else if (user !== singleUser) {
          setLoading2(false);
        }
      }
    }
  }, [user]);

  useEffect(() => {
    getAllCountries();
  }, []);

  const getAllCountries = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    //const URL = `${api}countrylanguageManagement/countries`;
    const URL = `${api}countryManagement/countries`;
    var config = {
      method: 'get',
      url: URL,
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        const countries = result.countries.reverse();
        console.log('bgb', countries);

        let countryTableData = countries.filter(i=>i.enableDisableStatus);
        // let countryTableData = [];
        // countries.map(countryData => {
        //   const { id, countryName, enableDisableStatus} = countryData;
        //   if (enableDisableStatus === true) {
        //     return countryTableData.push(countryData);
        //   }
        //});
        setCountries(countryTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  // useEffect(() => {
  //   var config = {
  //     method: 'get',
  //     url: 'https://api.printful.com/countries',
  //     headers: {
  //       'Access-Control-Allow-Origin': '*',
  //     },
  //   };
  //   axios(config)
  //     .then(function(response) {
  //       const result = response.data;
  //       console.log(result);
  //     })
  //     .catch(function(error) {
  //       console.log(error);
  //     });
  //   // axios.get('https://api.printful.com/countries', { headers }).then(
  //   //   response => {
  //   //     setCountries(response.data);
  //   //     console.log(response.data);
  //   //   },
  //   //   error => {
  //   //     console.log(error);
  //   //   },
  //   // );
  // }, []);

  useEffect(() => {
    let arr = [];
    if (singleUser !== null) {
      arr = singleUser.name.split(' ');
    }
    form.setFieldsValue({
      firstName: singleUser === null ? '' : arr[0],
      middleName: singleUser === null ? '' : arr[1],
      lastName: singleUser === null ? '' : arr[2],
      email: singleUser === null ? '' : singleUser.email,
      selectRole: singleUser === null ? undefined : singleUser.roles,
      selectCountry: singleUser === null ? undefined : singleUser.country,
      selectRegion: singleUser === null ? undefined : singleUser.region,
      selectState: singleUser === null ? undefined : singleUser.state,
      selectCity: singleUser === null ? undefined : singleUser.city,
    });
  }, [singleUser]);

  const handleSubmit = values => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const fullName = `${values.firstName} ${values.middleName === undefined ? '' : values.middleName} ${
      values.lastName
    }`;

    const userValues = {
      name: fullName,
      email: values.email,
      roles: values.selectRole,
      countryID: selectedCountryID,
      regionID: selectedRegionID,
      stateID: selectedStateID,
      cityID: selectedCityID,
    };
    console.log({userValues});
    var data = new FormData();
    data.append('userJson', JSON.stringify(userValues));
    const URL =
      singleUser === null ? `${api}userManagement/addNewUser` : `${api}userManagement/modifyUser/${singleUser.id}`;

    const method = singleUser === null ? 'post' : 'put';
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
        const result = response.data;
        if (result.code.includes('406-NOT')) {
          setFlag(true);
          setError(result.message);
        } else {
          singleUser === null
            ? props.onAddEditUser(`${fullName} has been successfully Added.`)
            : props.onAddEditUser(`${fullName} has been successfully Updated.`);
          if(user.role == '"ADMIN"'){
            props.getCurrentUserCountry();
          }else{
            props.getAllUsers();
          }
          props.onCancel();
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  const handleChange = e => {
    if (user !== null) {
      setUser({ ...user, name: e.target.value });
    }
    setName(e.target.value);
    setFlag(false);
  };

  const handleChange2 = e => {
    if (user !== null) {
      setUser({ ...user, lastName: e.target.value });
    }
    setLastName(e.target.value);
    setFlag(false);
  };

  const handleChangeCountry = countryID => {
    // if (user !== null) {
    //   setUser({ ...user, country: e });
    // }

    let countryTemp = countries.find(i=>i.id == countryID);


    setSelectedCountry(countryTemp);
    setSelectedCountryID(countryID);
    console.log({countryTemp});
    console.log({countryID});
    
  };

  const handleChangeRegion = regionID => {
    // if (user !== null) {
    //   setUser({ ...user, country: e });
    // }

    let regionsTemp = selectedCountry?.regions.find(i=>i.id == regionID);


    setSelectedRegion(regionsTemp);
    setSelectedRegionID(regionID);
    console.log({regionsTemp});
    console.log({regionID});
    
    
  };

  const handleChangeState = stateID => {

    let statesTemp = selectedRegion?.states.find(i=>i.id == stateID);

    setSelectedState(statesTemp);
    setSelectedStateID(stateID);
    console.log({statesTemp});
    console.log({stateID});
    
  };

  const handleChangeCity = cityID => {
    // if (user !== null) {
    //   setUser({ ...user, country: e });
    // }

    let citiesTemp = selectedState.cities.find(i=>i.id == cityID);


    setSelectedCity(citiesTemp);
    setSelectedCityID(cityID);
    console.log({citiesTemp});
    console.log({cityID});
    
  };

  const handleChangeRole = e => {
    if (user !== null) {
      setUser({ ...user, roles: e });
    }
    setRoles(e);
  };

  function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
  }

  const handleEmail = e => {
    setFlag(false);
    setEmail(e.target.value);
  };

  const checkEmail = e => {
    if (validateEmail(email)) {
      setFlag(false);
      if (user !== null) {
        setUser({ ...user, email: email });
      }
    } else {
      setFlag(true);
      setError('Email is not Valid');
    }
  };

  useEffect(() => {
    if (name === '' ||
     lastName === '' ||
      email === ''  || 
      roles.length === '' ||
       selectedCountryID === '' ||
        selectedRegionID === '' ||
         selectedStateID === '' ||
         selectedCityID === '' 
         ) {
      setLoading2(true);
    } else {
      setLoading2(false);
    }
  }, [name, lastName, email, roles, selectedCountryID, selectedRegionID, selectedStateID, selectedCityID])

  useEffect(() => {
    if (user !== null) {
      console.log(user);
      if (
        user.name === '' ||
        user.lastName === '' ||
        user.email === '' ||
        user.roles.length === 0 || 
        user.countryID === '' || 
        user.regionID === '' ||
        user.stateID === '' || 
        user.cityID === ''
      ) {
        setLoading2(true);
      } else {
        setLoading2(false);
      }
    }
  }, [user]);

  const specialCharacter = event => {
    var regex = new RegExp('^[a-zA-Z0-9_ ]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
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
        <Form name="adduser" form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item
            name="firstName"
            rules={[{ message: 'Please input First Name!', required: true }]}
            label="First Name"
          >
            <Input placeholder="First Name" onChange={handleChange} onKeyPress={specialCharacter} maxlength="40" />
          </Form.Item>
          <Form.Item name="middleName" label="Middle Name">
            <Input placeholder="Middle Name" onKeyPress={specialCharacter} maxlength="40" />
          </Form.Item>
          <Form.Item name="lastName" rules={[{ message: 'Please input Last Name!', required: true }]} label="Last Name">
            <Input placeholder="Last Name" onChange={handleChange2} onKeyPress={specialCharacter} maxlength="40" />
          </Form.Item>
          <Form.Item
            name="email"
            rules={[{ message: 'Please input User Email!', required: true }]}
            label="Email Address"
          >
            <Input placeholder="Email" onChange={handleEmail} onBlur={checkEmail} />
          </Form.Item>
          <Form.Item
            name="selectRole"
            label="Select Role"
            rules={[{ message: 'Please select one role', required: true }]}
          >
            <Select placeholder="Select Role" mode="multiple" onChange={handleChangeRole} showArrow={true}>
              {/* <Option value="Superadmin">Super Admin</Option> */}
              <Option value="ADMIN">Admin</Option>
              <Option value="LEARNER">Learner</Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="selectCountry"
            label="Select Country"
            rules={[{ message: 'Please select country', required: true }]}
          >
            <Select placeholder="Select Country" onChange={handleChangeCountry}>
              {countries.map(country => (
                <Option value={country.id}>{country.countryName}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            name="selectRegion"
            label="Select Region"
            rules={[{ message: 'Please select region', required: true }]}
          >
            <Select placeholder="Select Region" onChange={handleChangeRegion}>
              {selectedCountry?.regions?.length?selectedCountry.regions.map(region => (
                <Option value={region.id}>{region.regionName}</Option>
              )):null}
            </Select>
          </Form.Item>
          <Form.Item
            name="selectState"
            label="Select State"
            rules={[{ message: 'Please select State', required: true }]}
          >
            <Select placeholder="Select State" onChange={handleChangeState}>
              {selectedRegion?.states?.length?selectedRegion.states.map(state => (
                <Option value={state.id}>{state.stateName}</Option>
              )):null}
            </Select>
          </Form.Item>
          <Form.Item
            name="selectCity"
            label="Select City"
            rules={[{ message: 'Please select city', required: true }]}
          >
            <Select placeholder="Select City" onChange={handleChangeCity}>
            {selectedState?.cities?.length?selectedState.cities.map(city => (
                <Option value={city.id}>{city.cityName}</Option>
              )):null}
            </Select>
          </Form.Item>
          <div className="sDash-button-grp">
            <Button
              className="btn-signin"
              htmlType="submit"
              type={loading2 ? '' : 'primary'}
              size="large"
              disabled={loading2}
            >
              {loading ? <Spin size="medium" /> : singleUser === null ? 'Create' : 'Update'}
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

export default AddUser;

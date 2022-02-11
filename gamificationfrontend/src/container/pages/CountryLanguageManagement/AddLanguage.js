import React, { Component } from 'react';

import CreatableSelect from 'react-select/creatable';

const components = {
  DropdownIndicator: null,
};

const createOption = label => ({
  label,
  value: label,
});

export default class CreatableInputOnly extends Component {
  state = {
    inputValue: '',
    value: [],
  };
  handleChange = value => {
    this.setState({ value });
  };
  handleInputChange = inputValue => {
    this.setState({ inputValue });
  };
  handleKeyDown = event => {
    console.log(this.state.value);
    const { inputValue, value } = this.state;
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
          state.value.map(lan => {
            if (lan.value.toLowerCase() === inputValue.toLowerCase()) {
              console.log('Already Present');
              return;
            } else {
              setState({
                ...state,
                inputValue: '',
                value: [...state.value, createOption(inputValue)],
              });
            }
          });
        }
        event.preventDefault();
    }
  };
  render() {
    const { inputValue } = this.state;
    return (
      <CreatableSelect
        components={components}
        inputValue={inputValue}
        isClearable
        isMulti
        menuIsOpen={false}
        onChange={this.handleChange}
        onInputChange={this.handleInputChange}
        onKeyDown={this.handleKeyDown}
        placeholder="Type Language and press enter..."
        value={this.state.value}
      />
    );
  }
}

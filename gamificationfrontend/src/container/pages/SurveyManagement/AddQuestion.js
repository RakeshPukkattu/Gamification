import React, { useState, useEffect } from 'react';
import { Row, Col, Form, Input, Modal, Alert, Select, Radio, Checkbox } from 'antd';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { Link } from 'react-router-dom';
import FeatherIcon from 'feather-icons-react';
import { useDispatch, useSelector } from 'react-redux';
import { WizardWrapper, Wizard, WizardBlock } from './Style';
import { Steps } from '../../../components/steps/steps';
import Heading from '../../../components/heading/heading';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { Button } from '../../../components/buttons/buttons';
import { Main, BasicFormWrapper } from '../../styled';
import { useHistory } from 'react-router-dom';
import { getItem, setItem } from '../../../utility/localStorageControl';
import axios from 'axios';

const AddQuestion = () => {
  const history = useHistory();
  const [form] = Form.useForm();
  const [totalQuestions, setTotalQuestions] = useState(null);
  const [ques, setQues] = useState([]);
  const [questionNumber, setQuestionNumber] = useState('');
  const [questionDetail, setQuestionDetail] = useState('');
  const [option1, setOption1] = useState('');
  const [option2, setOption2] = useState('');
  const [option3, setOption3] = useState('');
  const [option4, setOption4] = useState('');
  const [mcqAnswer, setMcqAnswer] = useState(0);
  const [mmcqAnswer, setMmcqAnswer] = useState([]);
  const [allQuestions, setAllQuestions] = useState([]);
  const [oneQuestion, setOneQuestion] = useState([]);
  const [flag, setFlag] = useState(true);
  const [show, setShow] = useState(false);
  const [show2, setShow2] = useState(false);
  const [flag2, setFlag2] = useState(false);
  const [error, setError] = useState('');
  const [flag3, setFlag3] = useState(true);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isModalVisible2, setIsModalVisible2] = useState(false);
  const [disable, setDisable] = useState(false);
  const [mcqOptions, setMcqOptions] = useState([{ value: null }, { value: null }]);
  const [mmcqOptions, setMmcqOptions] = useState([{ value: null }, { value: null }]);
  const [noOfMCQ, setNoOfMCQ] = useState(null);
  const [noOfMMCQ, setNoOfMMCQ] = useState(null);
  const [noOfDropdown, setNoOfDropdown] = useState(null);

  const [dropdownOptions, setDropdownOptions] = useState([{ value: null }, { value: null }]);
  const [labelOptions, setLabelOptions] = useState([{ value: null }, { value: null }]);
  const [dropdownAnswer, setDropdownAnswer] = useState([{ value: null }, { value: null }]);

  useEffect(() => {
    const survey = getItem('survey');
    setNoOfMCQ(survey.questions);
    setNoOfMMCQ(survey.mmcqQuestion);
    setNoOfDropdown(survey.dropdownQuestions);
    const total = parseInt(survey.questions) + parseInt(survey.mmcqQuestion);
    //+ parseInt(survey.dropdownQuestions);
    setTotalQuestions(total);
  }, []);

  const [state, setState] = useState({
    status: 'process',
    isFinished: false,
    current: 0,
  });

  const { status, isFinished, current } = state;

  useEffect(() => {
    if (allQuestions.length !== 0) {
      allQuestions.map(question => {
        if (question.questionNumber === current) {
          setQuestionNumber(question.questionNumber);
          setQuestionDetail(question.questionDetail);
          setMcqOptions(question.mcqOptions);
          setMmcqOptions(question.mmcqOptions);
          setMcqAnswer(question.mcqAnswer);
          setMmcqAnswer(question.mmcqAnswer);
          setDropdownOptions(question.dropdownOptions);
          setLabelOptions(question.labelOptions);
          setDropdownAnswer(question.dropdownAnswer);
          form.setFieldsValue({
            question: question.questionDetail,
            mcqAnswer: question.mcqAnswer,
            mmcqAnswer: question.mmcqAnswer,
          });
        }
      });
    }
  }, [state]);

  const next = () => {
    if (flag3 === true) {
      setFlag2(true);
      //setError('Please add question detail and option 1 and option 2');
    } else {
      setState({
        ...state,
        status: 'process',
        current: current + 1,
      });
      let question = [
        {
          questionNumber: current,
          questionDetail: questionDetail,
          mcqOptions: mcqOptions,
          mmcqOptions: mmcqOptions,
          mcqAnswer: mcqAnswer,
          mmcqAnswer: mmcqAnswer,
          dropdownOptions: dropdownOptions,
          labelOptions: labelOptions,
          dropdownAnswer: dropdownAnswer,
        },
      ];
      if (current >= allQuestions.length) {
        console.log('eee');
        let merged = allQuestions.concat(question);
        setAllQuestions(merged);
        setShow(false);
        setShow2(false);
        setFlag(true);
      } else if (allQuestions[current].questionNumber === current) {
        console.log('ssss');
        allQuestions[current].questionDetail = questionDetail;
        allQuestions[current].mcqOptions = mcqOptions;
        allQuestions[current].mmcqOptions = mmcqOptions;
        allQuestions[current].mcqAnswer = mcqAnswer;
        allQuestions[current].mmcqAnswer = mmcqAnswer;
        allQuestions[current].dropdownOptions = dropdownOptions;
        allQuestions[current].labelOptions = labelOptions;
        allQuestions[current].dropdownAnswer = dropdownAnswer;
      } else if (allQuestions[current].questionNumber !== current) {
        console.log('hhhh');
        let merged = [...allQuestions];
        merged.splice(current, 0, question[0]);
        setAllQuestions(merged);
        // console.log(merged, 'dcdv');
        // setShow(false);
        // setShow2(false);
        // setFlag(true);
      }

      form.setFieldsValue({
        question: '',
        mcqAnswer: '',
        mmcqAnswer: [],
      });
      setQuestionNumber('');
      setQuestionDetail('');
      setMcqAnswer(0);
      setMmcqAnswer([]);
      setMcqOptions([{ value: null }, { value: null }]);
      setMmcqOptions([{ value: null }, { value: null }]);
      setDropdownOptions([{ value: null }, { value: null }]);
      setLabelOptions([{ value: null }, { value: null }]);
      setDropdownAnswer([{ value: null }, { value: null }]);
    }
  };

  const prev = () => {
    setQuestionNumber('');
    setQuestionDetail('');
    setMcqAnswer(0);
    setMmcqAnswer([]);
    setMcqOptions([{ value: null }, { value: null }]);
    setMmcqOptions([{ value: null }, { value: null }]);
    setDropdownOptions([{ value: null }, { value: null }]);
    setLabelOptions([{ value: null }, { value: null }]);
    setDropdownAnswer([{ value: null }, { value: null }]);
    setState({
      ...state,
      status: 'process',
      current: current - 1,
    });
  };

  const done = () => {
    if (flag3 === true) {
      setFlag2(true);
      setError('Please add question detail and option 1 and option 2');
    } else {
      setIsModalVisible(true);
    }
  };

  const handleOk = () => {
    setState({
      ...state,
      isFinished: true,
      current: 0,
      visible: true,
    });
    setIsModalVisible(false);
    let question = [
      {
        questionNumber: current,
        questionDetail: questionDetail,
        mcqOptions: mcqOptions,
        mmcqOptions: mmcqOptions,
        mcqAnswer: mcqAnswer,
        mmcqAnswer: mmcqAnswer,
        dropdownOptions: dropdownOptions,
        labelOptions: labelOptions,
        dropdownAnswer: dropdownAnswer,
      },
    ];
    let merged = allQuestions.concat(question);
    setAllQuestions(merged);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let mcqquestions = [];
    let mmcqQuestions = [];
    let dropdownQuestions = [];
    merged.map(question => {
      const found = question.mcqOptions.some(row => row.value === null);
      const found2 = question.mmcqOptions.some(row => row.value === null);
      const found3 = question.dropdownOptions.some(row => row.value === null);
      if (!found) {
        let option = [];
        question.mcqOptions.map(val => {
          option.push(val.value);
        });
        mcqquestions.push({
          questionNo: question.questionNumber.toString(),
          questionType: 'MCQ',
          question: {
            questionStem: question.questionDetail,
            options: option,
            answer: question.mcqOptions[question.mcqAnswer - 1].value,
          },
        });
      }
      if (!found2) {
        let optioncd = [];
        let ans = [];
        question.mmcqOptions.map(val => {
          optioncd.push(val.value);
        });
        question.mmcqAnswer.map(val => {
          ans.push(question.mmcqOptions[val].value);
        });
        mmcqQuestions.push({
          questionNo: question.questionNumber.toString(),
          questionType: 'MMCQ',
          question: {
            questionStem: question.questionDetail,
            options: optioncd,
            answers: ans,
          },
        });
      }
      if (!found3) {
        let dropdown = [];
        let label = [];
        let answers = [];
        question.dropdownOptions.map(drop => {
          dropdown.push(drop.value);
        });
        question.labelOptions.map(drop => {
          label.push(drop.value);
        });
        question.dropdownAnswer.map(drop => {
          answers.push(drop.value);
        });
        dropdownQuestions.push({
          questionNo: question.questionNumber.toString(),
          questionType: 'DropDown',
          question: {
            questionStem: question.questionDetail,
            options: dropdown,
            labels: label,
            answers: answers,
          },
        });
      }
    });
    const combined = [...mcqquestions, ...mmcqQuestions, ...dropdownQuestions];
    const survey = getItem('survey');
    const country = survey.country;
    const surveyValues = {
      surveyName: survey.name,
      questions: combined,
    };
    console.log('vfv', surveyValues);
    var data = new FormData();
    data.append('userJson', JSON.stringify(surveyValues));
    data.append('country',country);
    const URL = `${api}surveyManagement/addSurvey`;
    var config = {
      method: 'post',
      url: URL,
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${user.accessToken}`,
      },
      data: data,
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        console.log(result);
        if (result.code.includes('400')) {
          console.log(result);
        } else {
          history.push({
            pathname: '/admin/survey',
            state: { detail: `${survey.name} Added Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
    //history.push('/admin/survey');
  };

  const handleOk2 = () => {
    setIsModalVisible2(false);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setIsModalVisible2(false);
  };

  const onChanges = current => {
    form.setFieldsValue({
      question: '',
      mcqAnswer: '',
      mmcqAnswer: [],
    });
    setQuestionNumber('');
    setQuestionDetail('');
    setMcqAnswer(0);
    setMmcqAnswer([]);
    setMcqOptions([{ value: null }, { value: null }]);
    setMmcqOptions([{ value: null }, { value: null }]);
    setDropdownOptions([{ value: null }, { value: null }]);
    setLabelOptions([{ value: null }, { value: null }]);
    setDropdownAnswer([{ value: null }, { value: null }]);
    setState({
      ...state,
      status: 'process',
      current: current,
    });
  };
  const steps2 = [];

  const handleMcqChange = (i, event) => {
    setFlag2(false);
    const values = [...mcqOptions];
    values[i].value = event.target.value;
    setMcqOptions(values);
  };

  const handleMcqAdd = () => {
    if (mcqOptions.length >= 5) {
      setFlag2(true);
      setError('Maximum 5 MCQ Options can be added');
    } else {
      const values = [...mcqOptions];
      values.push({ value: null });
      setMcqOptions(values);
    }
  };

  const handleMcqRemove = i => {
    if (mcqOptions.length <= 2) {
      setFlag2(true);
      setError('Minimum 2 MCQ Options must be there');
    } else {
      const values = [...mcqOptions];
      values.splice(i, 1);
      setMcqOptions(values);
    }
  };

  const handleMmcqChange = (i, event) => {
    setFlag2(false);
    const values = [...mmcqOptions];
    values[i].value = event.target.value;
    setMmcqOptions(values);
  };

  const handleMmcqAdd = () => {
    if (mmcqOptions.length >= 5) {
      setFlag2(true);
      setError('Maximum 5 MMCQ Options can be added');
    } else {
      const values = [...mmcqOptions];
      values.push({ value: null });
      setMmcqOptions(values);
    }
  };

  const handleMmcqRemove = i => {
    if (mmcqOptions.length <= 2) {
      setFlag2(true);
      setError('Minimum 2 MMCQ Options must be there');
    } else {
      const values = [...mmcqOptions];
      values.splice(i, 1);
      setMmcqOptions(values);
    }
  };

  const handleChange = (i, event) => {
    setFlag2(false);
    const values = [...dropdownOptions];
    values[i].value = event.target.value;
    setDropdownOptions(values);
  };

  const handleAdd = () => {
    const values = [...dropdownOptions];
    values.push({ value: null });
    setDropdownOptions(values);
  };

  const handleRemove = i => {
    if (dropdownOptions.length <= 2) {
      setFlag2(true);
      setError('Minimum 2 Dropdown Options must be there');
    } else {
      const values = [...dropdownOptions];
      values.splice(i, 1);
      setDropdownOptions(values);
    }
  };

  const handleChange1 = (i, event) => {
    setFlag2(false);
    const values1 = [...labelOptions];
    values1[i].value = event.target.value;
    setLabelOptions(values1);
  };

  const handleChange2 = (i, event) => {
    setFlag2(false);
    const values2 = [...dropdownAnswer];
    values2[i].value = event;
    setDropdownAnswer(values2);
  };

  const handleAdd1 = () => {
    if (dropdownOptions.length > labelOptions.length) {
      const values1 = [...labelOptions];
      values1.push({ value: null });
      setLabelOptions(values1);
      const values2 = [...dropdownAnswer];
      values2.push({ value: null });
      setDropdownAnswer(values2);
    } else {
      setFlag2(true);
      setError("Can't add label options more than Dropdown options");
    }
  };

  const handleRemove1 = i => {
    if (labelOptions.length <= 2) {
      setFlag2(true);
      setError('Minimum 2 Label Options must be there');
    } else {
      const values1 = [...labelOptions];
      values1.splice(i, 1);
      setLabelOptions(values1);
      const values2 = [...dropdownAnswer];
      values2.splice(i, 1);
      setDropdownAnswer(values2);
    }
  };

  const moveUp = (e, i, name) => {
    console.log(i);
    if (i >= ques.length) {
      var k = i - ques.length + 1;
      while (k--) {
        ques.push(undefined);
      }
    }
    ques.splice(i, 0, ques.splice(i - 1, 1)[0]);
  };

  const moveDown = (e, i, name) => {
    if (i - 1 >= ques.length) {
      var k = i - 1 - ques.length + 1;
      while (k--) {
        ques.push(undefined);
      }
    }
    ques.splice(i - 1, 0, ques.splice(i, 1)[0]);
  };

  let i = 1;
  useEffect(() => {
    for (let z = 1; z <= noOfMCQ; z++) {
      steps2.push({
        title: (
          <>
            <h2>
              Question {z}
              <Button type="primary" size="small" style={{ marginLeft: '10px' }} onClick={e => moveUp(e, z, 'mcq')}>
                <FeatherIcon icon="arrow-up" size={16} />
              </Button>
              <Button
                className="btn-signin"
                type="danger"
                size="small"
                style={{ marginLeft: '50px' }}
                onClick={e => removeParticularQuestion(e, i, 'mcq')}
                disabled={disable}
              >
                X
              </Button>
            </h2>
            <p>MCQ</p>
            <Button
              type="primary"
              size="small"
              style={{ marginLeft: '100px', marginTop: '-50px' }}
              onClick={e => moveDown(e, z, 'mcq')}
            >
              <FeatherIcon icon="arrow-down" size={16} />
            </Button>
          </>
        ),
        content: (
          <BasicFormWrapper className="basic-form-inner theme-light">
            <div className="atbd-form-checkout">
              <Row justify="center">
                <Col xs={24}>
                  <div className="create-account-form">
                    <Heading as="h4">{z}. Please Enter Question Details</Heading>
                    <Form form={form} name="question1">
                      <Form.Item name="question" label="Question">
                        <Input
                          placeholder="Question Detail"
                          onChange={e => {
                            setQuestionDetail(e.target.value);
                            setFlag2(false);
                          }}
                          maxLength="100"
                          required
                        />
                      </Form.Item>
                      <Form.Item name="option1" label="Question Options">
                        <Radio.Group
                          onChange={e => {
                            console.log('fvfvf', e.target.value + 1);
                            setMcqAnswer(e.target.value + 1);
                          }}
                          value={mcqAnswer - 1}
                        >
                          {mcqOptions.map((field, idx) => {
                            return (
                              <Row gutter={25} style={{ width: '100%' }}>
                                <Col sm={24} xs={24}>
                                  <Radio value={idx}>
                                    <div key={`${field}-${idx}`}>
                                      <Row gutter={25} style={{ width: '250%' }}>
                                        <Col sm={20} xs={24}>
                                          <Input
                                            style={{ marginTop: '10px' }}
                                            placeholder="Enter Option"
                                            value={field.value || ''}
                                            onChange={e => handleMcqChange(idx, e)}
                                          />
                                        </Col>
                                        <Col sm={4} xs={24}>
                                          <Button
                                            className="btn-signin"
                                            type="danger"
                                            size="large"
                                            style={{ marginLeft: '10px', marginTop: '10px' }}
                                            onClick={() => handleMcqRemove(idx)}
                                          >
                                            -
                                          </Button>
                                        </Col>
                                      </Row>
                                    </div>
                                  </Radio>
                                </Col>
                              </Row>
                            );
                          })}
                        </Radio.Group>{' '}
                        <br />
                        <Button
                          className="btn-signin"
                          type="danger"
                          size="large"
                          style={{ marginTop: '10px' }}
                          onClick={() => handleMcqAdd()}
                        >
                          Add More Options
                        </Button>
                      </Form.Item>
                    </Form>
                  </div>
                </Col>
              </Row>
            </div>
          </BasicFormWrapper>
        ),
      });
    }
    i = 1 + parseInt(noOfMCQ);
    for (let j = i; j <= parseInt(noOfMMCQ) + parseInt(noOfMCQ); j++) {
      steps2.push({
        title: (
          <>
            <h2>
              Question {j}
              <Button type="primary" size="small" style={{ marginLeft: '10px' }} onClick={e => moveUp(e, j, 'mmcq')}>
                <FeatherIcon icon="arrow-up" size={16} />
              </Button>
              <Button
                className="btn-signin"
                type="danger"
                size="small"
                style={{ marginLeft: '50px' }}
                onClick={e => removeParticularQuestion(e, i, 'mmcq')}
                disabled={disable}
              >
                X
              </Button>
            </h2>{' '}
            <p>MMCQ</p>
            <Button
              type="primary"
              size="small"
              style={{ marginLeft: '100px', marginTop: '-50px' }}
              onClick={e => moveDown(e, j, 'mmcq')}
            >
              <FeatherIcon icon="arrow-down" size={16} />
            </Button>
          </>
        ),
        content: (
          <BasicFormWrapper className="basic-form-inner theme-light">
            <div className="atbd-form-checkout">
              <Row justify="center">
                <Col xs={24}>
                  <div className="create-account-form">
                    <Heading as="h4">{j}. Please Enter Question Details</Heading>
                    <Form form={form} name="question1">
                      <Form.Item name="question" label="Question">
                        <Input
                          placeholder="Question Detail"
                          onChange={e => {
                            setQuestionDetail(e.target.value);
                            setFlag2(false);
                          }}
                          maxLength="100"
                          required
                        />
                      </Form.Item>
                      <Form.Item name="option1" label="Question Options">
                        <Checkbox.Group
                          onChange={e => {
                            setMmcqAnswer(e);
                          }}
                          value={mmcqAnswer}
                        >
                          {mmcqOptions.map((field, idx) => {
                            return (
                              <Row gutter={25} style={{ width: '100%' }}>
                                <Col sm={24} xs={24}>
                                  <Checkbox value={idx}>
                                    <div key={`${field}-${idx}`}>
                                      <Row gutter={25} style={{ width: '250%' }}>
                                        <Col sm={20} xs={24}>
                                          <Input
                                            style={{ marginTop: '10px' }}
                                            placeholder="Enter Option"
                                            value={field.value || ''}
                                            onChange={e => handleMmcqChange(idx, e)}
                                          />
                                        </Col>
                                        <Col sm={4} xs={24}>
                                          <Button
                                            className="btn-signin"
                                            type="danger"
                                            size="large"
                                            style={{ marginLeft: '10px', marginTop: '10px' }}
                                            onClick={() => handleMmcqRemove(idx)}
                                          >
                                            -
                                          </Button>
                                        </Col>
                                      </Row>
                                    </div>
                                  </Checkbox>
                                </Col>
                              </Row>
                            );
                          })}
                        </Checkbox.Group>
                        <br />
                        <Button
                          className="btn-signin"
                          type="danger"
                          size="large"
                          style={{ marginTop: '10px' }}
                          onClick={() => handleMmcqAdd()}
                        >
                          Add More Options
                        </Button>
                      </Form.Item>
                      {/* <Form.Item name="mmcqAnswer" label="Answer of the Question" style={{ marginTop: '10px' }}>
                        <Select
                          mode="multiple"
                          showArrow
                          defaultValue={mmcqAnswer}
                          placeholder="Select Answer"
                          onChange={e => {
                            setMmcqAnswer(e);
                            setFlag2(false);
                          }}
                        >
                          <Select.Option value="1">Option 1</Select.Option>
                          <Select.Option value="2">Option 2</Select.Option>
                          <Select.Option value="3">Option 3</Select.Option>
                          <Select.Option value="4">Option 4</Select.Option>
                        </Select>
                      </Form.Item> */}
                    </Form>
                  </div>
                </Col>
              </Row>
            </div>
          </BasicFormWrapper>
        ),
      });
    }
    i = 1 + parseInt(noOfMMCQ) + parseInt(noOfMCQ);
    for (let y = i; y < i + parseInt(noOfDropdown); y++) {
      steps2.push({
        title: (
          <>
            <h2>
              Question {y}{' '}
              <Button
                type="primary"
                size="small"
                style={{ marginLeft: '10px' }}
                onClick={e => moveUp(e, y, 'dropdown')}
              >
                <FeatherIcon icon="arrow-up" size={16} />
              </Button>
              <Button
                className="btn-signin"
                type="danger"
                size="small"
                style={{ marginLeft: '50px' }}
                onClick={e => removeParticularQuestion(e, i, 'dropdown')}
                disabled={disable}
              >
                X
              </Button>
              <p>Dropdown</p>
            </h2>
            <Button
              type="primary"
              size="small"
              style={{ marginLeft: '105px', marginTop: '-50px' }}
              onClick={e => moveDown(e, y, 'dropdown')}
            >
              <FeatherIcon icon="arrow-down" size={16} />
            </Button>
          </>
        ),
        content: (
          <BasicFormWrapper className="basic-form-inner theme-light">
            <div className="atbd-form-checkout">
              <Row justify="center">
                <Col xs={24}>
                  <div className="create-account-form">
                    <Heading as="h4">{y}. Please Enter Question Details</Heading>
                    <Form form={form} name="question1">
                      <Form.Item name="question" label="Question">
                        <Input
                          placeholder="Question Detail"
                          onChange={e => {
                            setQuestionDetail(e.target.value);
                            setFlag2(false);
                          }}
                          maxLength="100"
                          required
                        />
                      </Form.Item>

                      <Form.Item name="option1" label="Dropdown Options">
                        {dropdownOptions.map((field, idx) => {
                          return (
                            <div key={`${field}-${idx}`}>
                              <Row gutter={25}>
                                <Col sm={20} xs={24}>
                                  <Input
                                    style={{ marginTop: '10px' }}
                                    placeholder="Enter Option"
                                    value={field.value || ''}
                                    onChange={e => handleChange(idx, e)}
                                  />
                                </Col>
                                <Col sm={4} xs={24}>
                                  <Button
                                    className="btn-signin"
                                    type="danger"
                                    size="large"
                                    style={{ marginLeft: '10px', marginTop: '10px' }}
                                    onClick={() => handleRemove(idx)}
                                  >
                                    -
                                  </Button>
                                </Col>
                              </Row>
                            </div>
                          );
                        })}
                        <Button
                          className="btn-signin"
                          type="danger"
                          size="large"
                          style={{ marginTop: '10px' }}
                          onClick={() => handleAdd()}
                        >
                          Add More Options
                        </Button>
                      </Form.Item>
                      <Row gutter={25}>
                        <Col sm={12} xs={24}>
                          <Form.Item name="labels" label="Label Options">
                            {labelOptions.map((field, idx) => {
                              return (
                                <div key={`${field}-${idx}`}>
                                  <Row gutter={25}>
                                    <Col sm={20} xs={24}>
                                      <Input
                                        style={{ marginTop: '10px' }}
                                        placeholder="Enter Label"
                                        value={field.value || ''}
                                        onChange={e => handleChange1(idx, e)}
                                      />
                                    </Col>
                                    <Col sm={4} xs={24}>
                                      <Button
                                        className="btn-signin"
                                        type="danger"
                                        size="large"
                                        style={{ marginLeft: '-75px', marginTop: '10px' }}
                                        onClick={() => handleRemove1(idx)}
                                      >
                                        -
                                      </Button>
                                    </Col>
                                  </Row>
                                </div>
                              );
                            })}
                            <Button
                              className="btn-signin"
                              type="danger"
                              size="large"
                              style={{ marginTop: '10px' }}
                              onClick={() => handleAdd1()}
                            >
                              Add More Labels
                            </Button>
                          </Form.Item>
                        </Col>
                        <Col sm={12} xs={24}>
                          <Form.Item name="correct" label="Correct Option">
                            {dropdownAnswer.map((field, idx) => {
                              return (
                                <div key={`${field}-${idx}`}>
                                  <Row gutter={25}>
                                    <Col sm={20} xs={24}>
                                      <Select
                                        value={dropdownAnswer[idx].value}
                                        placeholder="Choose Dropdown Correct Option"
                                        onChange={e => {
                                          handleChange2(idx, e);
                                          setFlag2(false);
                                        }}
                                        style={{ marginTop: '7px' }}
                                      >
                                        {dropdownOptions.map(drop => (
                                          <Select.Option
                                            value={drop.value}
                                            //disabled={dropdownAnswer.includes(drop.value)}
                                            disabled={dropdownAnswer.some(row => row.value === drop.value)}
                                          >
                                            {drop.value}
                                          </Select.Option>
                                        ))}
                                      </Select>{' '}
                                    </Col>
                                  </Row>
                                </div>
                              );
                            })}
                          </Form.Item>
                        </Col>
                      </Row>
                    </Form>
                  </div>
                </Col>
              </Row>
            </div>
          </BasicFormWrapper>
        ),
      });
    }
    setQues(steps2);
  }, [
    mcqOptions,
    noOfMCQ,
    noOfMMCQ,
    mmcqOptions,
    noOfDropdown,
    dropdownOptions,
    labelOptions,
    dropdownAnswer,
    mcqAnswer,
    mmcqAnswer,
  ]);

  // useEffect(() => {
  //   if (new_index >= arr.length) {
  //     var k = new_index - arr.length + 1;
  //     while (k--) {
  //       arr.push(undefined);
  //     }
  //   }
  //   arr.splice(new_index, 0, arr.splice(old_index, 1)[0]);
  // }, [ques])

  const ss = [
    {
      title: (
        <div className="wizard-item">
          <h2>Question 1</h2>
          {/* <p>Lorem Ipsum is simply dummy text of the dummy typesetting industry.</p>
                        <img src={require('../../../../static/img/wizards/1.svg')} alt="" /> */}
        </div>
      ),
      content: (
        <BasicFormWrapper className="basic-form-inner">
          <div className="atbd-form-checkout">
            <Row justify="center">
              <Col xs={24}>
                <div className="create-account-form">
                  <Heading as="h4">1. vfdvf Entevdvfvdvdfvfdvr Question Details</Heading>
                  <Form form={form} name="question1">
                    <Form.Item name="question" label="Question">
                      <Input placeholder="Question Detail" />
                    </Form.Item>
                    <Form.Item name="option1" label="Option 1">
                      <Input placeholder="Option 1" />
                    </Form.Item>
                    <Form.Item name="option2" label="Option 2">
                      <Input placeholder="Option 2" />
                    </Form.Item>
                    <Form.Item name="option3" label="Option 3">
                      <Input placeholder="Option 3" />
                    </Form.Item>
                    <Form.Item name="option4" label="Option 4">
                      <Input placeholder="Option 4" />
                    </Form.Item>
                  </Form>
                </div>
              </Col>
            </Row>
          </div>
        </BasicFormWrapper>
      ),
    },
  ];

  const addQuestion = () => {
    setShowModal2(true);
    // let add = parseInt(totalQuestions) + 1;
    // setTotalQuestions(add);
  };

  // const removeQuestion = index => {
  //   let remove = parseInt(totalQuestions) - 1;
  //   setTotalQuestions(remove);
  // };

  const specialCharacter2 = event => {
    setFlag(false);
    var regex = new RegExp('^[0-9]+$');
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
      event.preventDefault();
      return false;
    }
  };

  useEffect(() => {
    if (questionDetail === '') {
      setFlag3(true);
      setError('Please add question stem');
    }
  }, [state, questionDetail]);

  useEffect(() => {
    if (mcqOptions.length > 0) {
      if (mcqAnswer === 0) {
        setFlag3(true);
        setError('Please Choose Answer of this question');
      } else {
        const found = mcqOptions.some(row => row.value === null);
        if (found) {
          setFlag3(true);
          setError("Don't leave any options blank");
        } else {
          setFlag3(false);
        }
      }
    } else {
      setFlag3(false);
    }
  }, [mcqOptions, mcqAnswer]);

  useEffect(() => {
    if (mmcqOptions.length > 0) {
      if (mmcqAnswer.length === 0) {
        setFlag3(true);
        setError('Please Choose Answer of this question');
      } else {
        const found = mmcqOptions.some(row => row.value === null);
        if (found) {
          setFlag3(true);
          setError("Don't leave any options blank");
        } else {
          setFlag3(false);
        }
      }
    } else {
      setFlag3(false);
    }
  }, [mmcqOptions, mmcqAnswer]);

  useEffect(() => {
    if (dropdownOptions.length > 0) {
      const found = dropdownOptions.some(row => row.value === null);
      if (found) {
        setFlag3(true);
        setError("Don't leave any field blank");
      } else {
        const found2 = labelOptions.some(row => row.value === null);
        if (found2) {
          setFlag3(true);
          setError("Don't leave any label options blank");
        } else {
          const found3 = dropdownAnswer.some(row => row.value === null);
          if (found3) {
            setFlag3(true);
            setError('Please choose Answer for the dropdown label');
          } else {
            setFlag3(false);
          }
        }
      }
    } else {
      setFlag3(false);
    }
  }, [labelOptions, dropdownAnswer]);

  const removeParticularQuestion = (e, i, name) => {
    if (name === 'mcq') {
      console.log(e.stopPropagation());
      let index = i - 1;
      var array = [...ques];
      if (index !== -1 && noOfMCQ > 2) {
        array.splice(index, 1);
        setQues(array);
        let remove = parseInt(totalQuestions) - 1;
        setTotalQuestions(remove);
        let rr = parseInt(noOfMCQ) - 1;
        setNoOfMCQ(rr);
        form.setFieldsValue({
          question: '',
          mcqAnswer: '',
        });
        setMcqOptions([{ value: null }, { value: null }]);
        setMcqAnswer(0);
      } else {
        setIsModalVisible2(true);
      }
    }
    if (name === 'mmcq') {
      console.log(e.stopPropagation());
      let index = i - 1;
      var array = [...ques];
      if (index !== -1 && noOfMMCQ > 2) {
        array.splice(index, 1);
        setQues(array);
        let remove = parseInt(totalQuestions) - 1;
        setTotalQuestions(remove);
        let rr = parseInt(noOfMMCQ) - 1;
        setNoOfMMCQ(rr);
        form.setFieldsValue({
          question: '',
          mcqAnswer: '',
        });
        setMmcqOptions([{ value: null }, { value: null }]);
        setMmcqAnswer([]);
      } else {
        setIsModalVisible2(true);
      }
    }
    if (name === 'dropdown') {
      console.log(e.stopPropagation());
      let index = i - 1;
      var array = [...ques];
      if (index !== -1 && noOfDropdown > 2) {
        array.splice(index, 1);
        setQues(array);
        let remove = parseInt(totalQuestions) - 1;
        setTotalQuestions(remove);
        let rr = parseInt(noOfDropdown) - 1;
        setNoOfDropdown(rr);
        form.setFieldsValue({
          question: '',
        });
        setDropdownOptions([{ value: null }, { value: null }]);
        setLabelOptions([{ value: null }, { value: null }]);
        setDropdownAnswer([{ value: null }, { value: null }]);
      } else {
        setIsModalVisible2(true);
      }
    }
  };

  useEffect(() => {
    if (state.status === 'finish') {
      setDisable(true);
    } else {
      setDisable(false);
    }
  }, [state]);

  const [showModal2, setShowModal2] = useState(false);
  const [mcq, setMcq] = useState('0');
  const [mmcq, setMmcq] = useState('0');
  const [dropdown, setDropdown] = useState('0');
  const [loading, setLoading] = useState(true);

  const handleCancel2 = () => {
    setShowModal2(false);
  };

  // useEffect(() => {
  //   if (mcq === '' || mmcq === '' || dropdown === '') {
  //     setLoading(true);
  //   } else {
  //     setLoading(false);
  //   }
  // }, [mcq, mmcq, dropdown]);

  const handleSubmit2 = values => {
    if (allQuestions.length !== 0) {
      let ss = parseInt(noOfMCQ);
      for (let i = 0; i < parseInt(noOfMMCQ) + parseInt(noOfDropdown) - 1; i++) {
        allQuestions[ss].questionNumber = parseInt(noOfMCQ) + parseInt(mcq) + i;
        ss = ss + 1;
      }
      let dd = parseInt(noOfMMCQ) + parseInt(noOfMCQ);
      let x = 0;
      for (let j = 1; j <= parseInt(noOfDropdown) - 1; j++) {
        allQuestions[dd].questionNumber = parseInt(noOfMCQ) + parseInt(mcq) + parseInt(noOfMMCQ) + parseInt(mmcq) + x;
        dd = dd + 1;
        x = x + 1;
      }
    }
    setNoOfMCQ(parseInt(noOfMCQ) + parseInt(mcq));
    setNoOfMMCQ(parseInt(noOfMMCQ) + parseInt(mmcq));
    setNoOfDropdown(parseInt(noOfDropdown) + parseInt(dropdown));
    setMcq('0');
    setMmcq('0');
    setDropdown('0');
    form.setFieldsValue({
      mcq: '',
      mmcq: '',
      dropdown: '',
    });
    handleCancel2();
  };

  return (
    <>
      <PageHeader title="Add Questions to the Survey" />
      <Main>
        <div className="wizard-side-border">
          <WizardBlock>
            <Cards headless>
              <div
                style={{
                  textAlign: 'left',
                  marginBottom: '25px',
                  marginTop: '-30px',
                  marginLeft: '40px',
                }}
              >
                <Button
                  className="btn-signin"
                  type={disable ? '' : 'primary'}
                  size="large"
                  onClick={addQuestion}
                  disabled={disable}
                >
                  Add More Question
                </Button>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                {flag2 ? (
                  <Alert message={error} type="error" showIcon style={{ width: '30%', textAlign: 'center' }} />
                ) : (
                  ''
                )}
              </div>
              <WizardWrapper className="bordered-wizard">
                <Wizard>
                  <Steps
                    isswitch
                    isvertical
                    current={0}
                    status={status}
                    steps={ques.length === 0 ? ss : ques}
                    onNext={next}
                    onPrev={prev}
                    onDone={done}
                    flag={flag3}
                    onChange={onChanges}
                    isfinished={isFinished}
                  />
                </Wizard>
              </WizardWrapper>
            </Cards>
          </WizardBlock>
        </div>
      </Main>
      <Modal title="Submit Questions" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel}>
        <p>Are you sure to submit all the question :)</p>
      </Modal>
      <Modal title="Error in Deleting Questions" visible={isModalVisible2} onOk={handleOk2} onCancel={handleCancel}>
        <p>A Survey must have atleast two questions.</p>
      </Modal>
      <Modal
        title="Add Questions"
        wrapClassName="sDash_export-wrap"
        visible={showModal2}
        footer={null}
        onCancel={handleCancel2}
      >
        <Form name="AddQuestions" form={form} onFinish={handleSubmit2} layout="vertical">
          <Form.Item
            name="mcq"
            label="No of MCQ Questions"
            // rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input
              placeholder="MCQ Questions"
              type="text"
              maxlength="2"
              onKeyPress={specialCharacter2}
              onChange={e => setMcq(e.target.value)}
            />
          </Form.Item>
          <Form.Item
            name="mmcq"
            label="No of MMCQ Questions"
            //rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input placeholder="MMCQ Questions" type="number" onChange={e => setMmcq(e.target.value)} />
          </Form.Item>
          <Form.Item
            name="dropdown"
            label="No of Dropdown Questions"
            //rules={[{ message: 'Please enter no of questions', required: true }]}
          >
            <Input placeholder="Dropdown Questions" type="number" onChange={e => setDropdown(e.target.value)} />
          </Form.Item>
          <div className="sDash-button-grp">
            <Button
              className="btn-signin"
              htmlType="submit"
              type="primary"
              //type={loading ? '' : 'primary'}
              size="large"
              //disabled={loading}
            >
              Add
            </Button>
          </div>
        </Form>
      </Modal>
    </>
  );
};

export default AddQuestion;

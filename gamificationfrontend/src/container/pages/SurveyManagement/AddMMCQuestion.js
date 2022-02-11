import React, { useState, useEffect } from 'react';
import { Row, Col, Form, Input, Modal, Alert, Select } from 'antd';
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
  const [answer, setAnswer] = useState([]);
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
  const [fields, setFields] = useState([{ value: null }, { value: null }]);

  useEffect(() => {
    const survey = getItem('survey');
    setTotalQuestions(survey.mmcqQuestion);
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
          // if (question.option3 !== '') {
          //   setShow(true);
          // } else {
          //   setShow(false);
          // }
          // if (question.option4 !== '') {
          //   setShow2(true);
          // } else {
          //   setShow2(false);
          // }
          // if (question.option3 !== '' && question.option4 !== '') {
          //   setFlag(false);
          // } else {
          //   setFlag(true);
          // }
          setQuestionNumber(question.questionNumber);
          setQuestionDetail(question.questionDetail);
          // setOption1(question.option1);
          // setOption2(question.option2);
          // setOption3(question.option3);
          // setOption4(question.option4);
          setFields(question.questionOptions);
          setAnswer(question.answer);
          form.setFieldsValue({
            question: question.questionDetail,
            // option1: question.option1,
            // option2: question.option2,
            // option3: question.option3,
            // option4: question.option4,
            answer: question.answer,
          });
        }
      });
    }
  }, [state]);

  const next = () => {
    if (flag3 === true) {
      setFlag2(true);
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
          // option1: option1,
          // option2: option2,
          // option3: option3,
          // option4: option4,
          questionOptions: fields,
          answer: answer,
        },
      ];
      if (current >= allQuestions.length) {
        let merged = allQuestions.concat(question);
        setAllQuestions(merged);
        setShow(false);
        setShow2(false);
        setFlag(true);
      } else {
        allQuestions[current].questionDetail = questionDetail;
        // allQuestions[current].option1 = option1;
        // allQuestions[current].option2 = option2;
        // allQuestions[current].option3 = option3;
        // allQuestions[current].option4 = option4;
        allQuestions[current].questionOptions = fields;
        allQuestions[current].answer = answer;

        // if (allQuestions[current] === undefined) {
        //   if (allQuestions[current].option3 !== '' && allQuestions[current].option3 ) {
        //     setShow(true);
        //   } else {
        //     setShow(false);
        //   }
        //   if (allQuestions[current].option4 !== '') {
        //     setShow2(true);
        //   } else {
        //     setShow2(false);
        //   }
        //   if (allQuestions[current].option4 !== '' && allQuestions[current].option3 !== '') {
        //     setFlag(false);
        //   } else {
        //     setFlag(true);
        //   }
        // }
      }

      form.setFieldsValue({
        question: '',
        option1: '',
        option2: '',
        option3: '',
        option4: '',
        answer: [],
      });
      setQuestionNumber('');
      setQuestionDetail('');
      setOption1('');
      setOption2('');
      setOption3('');
      setOption4('');
      setAnswer([]);
      setFields([{ value: null }, { value: null }]);
    }
  };

  const prev = () => {
    // allQuestions.map(question => {
    //   if (question.questionNumber === current - 1) {
    //     setQuestionNumber(question.questionNumber);
    //     setQuestionDetail(question.questionDetail);
    //     setOption1(question.option1);
    //     setOption1(question.option2);
    //     setOption1(question.option3);
    //     setOption1(question.option4);
    //   }
    // });
    setQuestionDetail('');
    setOption1('');
    setOption2('');
    setOption3('');
    setOption4('');
    setFields([{ value: null }, { value: null }]);
    setAnswer([]);
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
        questionOptions: fields,
        answer: answer,
      },
    ];
    let merged = allQuestions.concat(question);
    setAllQuestions(merged);
    console.log(merged);
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let questions = [];
    merged.map(question => {
      if (question.questionOptions.length === 2) {
        questions.push({
          questionNumberInSurvey: question.questionNumber,
          question: question.questionDetail,
          option1: question.questionOptions[0].value,
          option2: question.questionOptions[1].value,
          option3: '',
          option4: '',
          answer: question.answer,
        });
      } else if (question.questionOptions.length === 3) {
        questions.push({
          questionNumberInSurvey: question.questionNumber,
          question: question.questionDetail,
          option1: question.questionOptions[0].value,
          option2: question.questionOptions[1].value,
          option3: question.questionOptions[2].value,
          option4: '',
          answer: question.answer,
        });
      } else {
        questions.push({
          questionNumberInSurvey: question.questionNumber,
          question: question.questionDetail,
          option1: question.questionOptions[0].value,
          option2: question.questionOptions[1].value,
          option3: question.questionOptions[2].value,
          option4: question.questionOptions[3].value,
          answer: question.answer,
        });
      }
    });
    const survey = getItem('survey');
    const surveyValues = {
      surveyName: survey.name,
      totalNumbersOfMCQQuestions: survey.totalNumbersOfMCQQuestions,
      surveyMCQQuestions: survey.surveyMCQQuestions,
      totalNumbersOfMMCQQuestions: totalQuestions,
      surveyMMCQQuestions: questions,
      dropdownQuestions: survey.dropdownQuestions,
    };
    setItem('survey', surveyValues);
    history.push('/admin/adddropdownQuestion');
    // const survey = getItem('survey');
    // const surveyValues = {
    //   surveyName: survey.name,
    //   totalNumbersOfQuestions: totalQuestions,
    //   surveyQuestions: questions,
    // };
    // var data = new FormData();
    // data.append('userJson', JSON.stringify(surveyValues));
    // const URL = `${api}surveyManagement/addSurvey`;
    // var config = {
    //   method: 'post',
    //   url: URL,
    //   headers: {
    //     'Content-Type': 'multipart/form-data',
    //     Authorization: `Bearer ${user.accessToken}`,
    //   },
    //   data: data,
    // };
    // axios(config)
    //   .then(function(response) {
    //     const result = response.data;
    //     if (result.code.includes('406')) {
    //       console.log(result);
    //     } else {
    //       history.push({
    //         pathname: '/admin/survey',
    //         state: { detail: `${survey.name} Added Successfully` },
    //       });
    //     }
    //   })
    //   .catch(function(error) {
    //     console.log(error);
    //   });
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
      option1: '',
      option2: '',
      option3: '',
      option4: '',
      answer: [],
    });
    setFields([{ value: null }, { value: null }]);
    setState({
      ...state,
      status: 'process',
      current: current,
    });
  };
  const steps2 = [];

  const showOption = () => {
    if (show === false) {
      setShow(true);
    } else if (show === true && show2 === false) {
      setShow2(true);
      setFlag(false);
    }
  };

  const hideOption = () => {
    setOption3('');
    setShow(false);
    setFlag(true);
  };
  const hideOption2 = () => {
    setOption4('');
    setShow2(false);
    setFlag(true);
  };

  const handleChange = (i, event) => {
    setFlag2(false);
    const values = [...fields];
    values[i].value = event.target.value;
    setFields(values);
  };

  const handleAdd = () => {
    if (fields.length >= 4) {
      console.log("can't add");
    } else {
      const values = [...fields];
      values.push({ value: null });
      setFields(values);
    }
  };

  const handleRemove = i => {
    if (fields.length <= 2) {
      console.log("can't remove");
    } else {
      const values = [...fields];
      values.splice(i, 1);
      setFields(values);
    }
  };

  useEffect(() => {
    for (let i = 1; i <= totalQuestions; i++) {
      if (i == totalQuestions) {
        steps2.push({
          title: (
            <>
              <h2>
                Question {i}{' '}
                <Button
                  className="btn-signin"
                  type="danger"
                  size="small"
                  style={{ marginLeft: '100px' }}
                  onClick={e => removeParticularQuestion(e, i)}
                  disabled={disable}
                >
                  X
                </Button>
              </h2>
            </>
          ),
          content:
            status !== 'finish' ? (
              <BasicFormWrapper className="basic-form-inner theme-light" style={{ width: '100%' }}>
                <div className="atbd-form-checkout">
                  <Row justify="center">
                    <Col xs={24}>
                      <div className="create-account-form">
                        <Heading as="h4">{totalQuestions}. Please Enter Question Details</Heading>
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
                          {/* <Form.Item name="option1" label="Option 1">
                            <Input
                              placeholder="Option 1"
                              onChange={e => {
                                setOption1(e.target.value);
                                setFlag2(false);
                              }}
                              maxLength="100"
                              required
                            />
                          </Form.Item>
                          <Form.Item name="option2" label="Option 2">
                            <Input
                              placeholder="Option 2"
                              onChange={e => {
                                setOption2(e.target.value);
                                setFlag2(false);
                              }}
                              maxLength="100"
                              required
                            />
                          </Form.Item>
                          {show === true ? (
                            <>
                              <Row gutter={25}>
                                <Col sm={20} xs={24}>
                                  <Form.Item name="option3" label="Option 3">
                                    <Input
                                      placeholder="Option 3"
                                      onChange={e => setOption3(e.target.value)}
                                      maxLength="100"
                                    />
                                  </Form.Item>
                                </Col>
                                <Col sm={4} xs={24}>
                                  <Button
                                    className="btn-signin"
                                    type="danger"
                                    size="large"
                                    style={{ marginLeft: '10px', marginTop: '28px' }}
                                    onClick={hideOption}
                                  >
                                    -
                                  </Button>
                                </Col>
                              </Row>
                            </>
                          ) : (
                            ''
                          )}
                          {show2 === true ? (
                            <>
                              <Row gutter={25} style={{ marginTop: '15px' }}>
                                <Col sm={20} xs={24}>
                                  <Form.Item name="option4" label="Option 4">
                                    <Input
                                      placeholder="Option 4"
                                      onChange={e => setOption4(e.target.value)}
                                      maxLength="100"
                                    />
                                  </Form.Item>
                                </Col>
                                <Col sm={4} xs={24}>
                                  <Button
                                    className="btn-signin"
                                    type="danger"
                                    size="large"
                                    style={{ marginLeft: '10px', marginTop: '28px' }}
                                    onClick={hideOption2}
                                  >
                                    -
                                  </Button>
                                </Col>
                              </Row>
                            </>
                          ) : (
                            ''
                          )}
                          {flag === true ? (
                            <Button
                              className="btn-signin"
                              type="secondary"
                              size="small"
                              style={{ width: '100%', marginTop: '20px' }}
                              onClick={showOption}
                            >
                              Add One Option
                            </Button>
                          ) : (
                            ''
                          )} */}
                          <Form.Item name="option1" label="Question Options">
                            {fields.map((field, idx) => {
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
                          <Form.Item name="answer" label="Answer of the Question">
                            <Select
                              placeholder="Select Answer"
                              mode="multiple"
                              defaultValue={answer}
                              showArrow
                              onChange={e => {
                                console.log(e);
                                setAnswer(e);
                                setFlag2(false);
                              }}
                            >
                              <Select.Option value="1">Option 1</Select.Option>
                              <Select.Option value="2">Option 2</Select.Option>
                              <Select.Option value="3">Option 3</Select.Option>
                              <Select.Option value="4">Option 4</Select.Option>
                            </Select>
                          </Form.Item>
                        </Form>
                      </div>
                    </Col>
                  </Row>
                </div>
              </BasicFormWrapper>
            ) : (
              <Row justify="start" style={{ width: '100%' }}>
                <Col xl={20} xs={24}>
                  <div className="checkout-successful">
                    <Cards
                      headless
                      bodyStyle={{
                        backgroundColor: '#F8F9FB',
                        borderRadius: '20px',
                      }}
                    >
                      <Cards headless>
                        <span className="icon-success">
                          <FeatherIcon icon="check" />
                        </span>
                        <Heading as="h3">All Questions Added</Heading>
                        <p>Thank you! All Questions have been added to your survey</p>
                      </Cards>
                    </Cards>
                  </div>
                </Col>
              </Row>
            ),
        });
      } else {
        steps2.push({
          title: (
            <>
              <h2>
                Question {i}{' '}
                <Button
                  className="btn-signin"
                  type="danger"
                  size="small"
                  style={{ marginLeft: '100px' }}
                  onClick={e => removeParticularQuestion(e, i)}
                  disabled={disable}
                >
                  X
                </Button>
              </h2>
            </>
          ),
          content: (
            <BasicFormWrapper className="basic-form-inner theme-light">
              <div className="atbd-form-checkout">
                <Row justify="center">
                  <Col xs={24}>
                    <div className="create-account-form">
                      <Heading as="h4">{i}. Please Enter Question Details</Heading>
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
                        {/* <Form.Item name="option1" label="Option 1">
                          <Input
                            placeholder="Option 1"
                            onChange={e => {
                              setOption1(e.target.value);
                              setFlag2(false);
                            }}
                            maxLength="100"
                            required
                          />
                        </Form.Item>
                        <Form.Item name="option2" label="Option 2">
                          <Input
                            placeholder="Option 2"
                            onChange={e => {
                              setOption2(e.target.value);
                              setFlag2(false);
                            }}
                            maxLength="100"
                            required
                          />
                        </Form.Item>
                        {show === true ? (
                          <>
                            <Row gutter={25}>
                              <Col sm={20} xs={24}>
                                <Form.Item name="option3" label="Option 3">
                                  <Input
                                    placeholder="Option 3"
                                    onChange={e => setOption3(e.target.value)}
                                    maxLength="100"
                                  />
                                </Form.Item>
                              </Col>
                              <Col sm={4} xs={24}>
                                <Button
                                  className="btn-signin"
                                  type="danger"
                                  size="large"
                                  style={{ marginLeft: '10px', marginTop: '28px' }}
                                  onClick={hideOption}
                                >
                                  -
                                </Button>
                              </Col>
                            </Row>
                          </>
                        ) : (
                          ''
                        )}
                        {show2 === true ? (
                          <>
                            <Row gutter={25} style={{ marginTop: '15px' }}>
                              <Col sm={20} xs={24}>
                                <Form.Item name="option4" label="Option 4">
                                  <Input
                                    placeholder="Option 4"
                                    onChange={e => setOption4(e.target.value)}
                                    maxLength="100"
                                  />
                                </Form.Item>
                              </Col>
                              <Col sm={4} xs={24}>
                                <Button
                                  className="btn-signin"
                                  type="danger"
                                  size="large"
                                  style={{ marginLeft: '10px', marginTop: '28px' }}
                                  onClick={hideOption2}
                                >
                                  -
                                </Button>
                              </Col>
                            </Row>
                          </>
                        ) : (
                          ''
                        )}
                        {flag === true ? (
                          <Button
                            className="btn-signin"
                            type="secondary"
                            size="small"
                            style={{ width: '100%', marginTop: '20px' }}
                            onClick={showOption}
                          >
                            Add One Option
                          </Button>
                        ) : (
                          ''
                        )} */}
                        <Form.Item name="option1" label="Question Options">
                          {fields.map((field, idx) => {
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
                        <Form.Item name="answer" label="Answer of the Question" style={{ marginTop: '10px' }}>
                          <Select
                            mode="multiple"
                            showArrow
                            defaultValue={answer}
                            placeholder="Select Answer"
                            onChange={e => {
                              console.log(e);
                              setAnswer(e);
                              setFlag2(false);
                            }}
                          >
                            <Select.Option value="1">Option 1</Select.Option>
                            <Select.Option value="2">Option 2</Select.Option>
                            <Select.Option value="3">Option 3</Select.Option>
                            <Select.Option value="4">Option 4</Select.Option>
                          </Select>
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
    }
    setQues(steps2);
  }, [totalQuestions, show, show2, flag, state, disable, fields]);

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
    let add = parseInt(totalQuestions) + 1;
    setTotalQuestions(add);
  };

  // const removeQuestion = index => {
  //   let remove = parseInt(totalQuestions) - 1;
  //   setTotalQuestions(remove);
  // };

  useEffect(() => {
    // if (questionDetail === '' || option1 === '' || option2 === '') {
    //   setFlag3(true);
    //   setError('Please add question detail and option 1 and option 2');
    // } else {
    //   setFlag3(false);
    // }
    // if (option1 === option2 || option1 === option3 || option1 === option4) {
    //   setFlag3(true);
    //   setError("Two Options can't be same");
    // }
    // if (option2 === option1 || option2 === option3 || option2 === option4) {
    //   setFlag3(true);
    //   setError("Two Options can't be same");
    // }
    // if (option3 === option1 || option3 === option2) {
    //   setFlag3(true);
    //   setError("Two Options can't be same");
    // }
    // if (option4 === option1 || option4 === option2) {
    //   setFlag3(true);
    //   setError("Two Options can't be same");
    // }
    // if (answer.length === 0) {
    //   setFlag3(true);
    //   setError('Please select answer for this question');
    // }
    if (questionDetail === '' || fields.length === 0 || answer.length === 0) {
      setFlag3(true);
      setError('Please add question detail and options and answer');
    } else if (fields.length > 0) {
      const found = fields.some(row => row.value === null);
      if (found) {
        setFlag3(true);
        setError('Please add question detail, options and answer');
      } else {
        setFlag3(false);
      }
    } else {
      setFlag3(false);
    }
  }, [questionDetail, fields, answer]);

  const removeParticularQuestion = (e, i) => {
    console.log(e.stopPropagation());
    let index = i - 1;
    var array = [...ques];
    if (index !== -1 && totalQuestions > 2) {
      array.splice(index, 1);
      setQues(array);
      let remove = parseInt(totalQuestions) - 1;
      setTotalQuestions(remove);
      form.setFieldsValue({
        question: '',
        option1: '',
        option2: '',
        option3: '',
        option4: '',
        answer: [],
      });
      setFields([{ value: null }, { value: null }]);
    } else {
      setIsModalVisible2(true);
    }
  };

  useEffect(() => {
    if (state.status === 'finish') {
      setDisable(true);
    } else {
      setDisable(false);
    }
  }, [state]);

  return (
    <>
      <PageHeader title="Add MMCQ Question to the Survey" />
      <Main>
        <div className="wizard-side-border">
          <WizardBlock>
            <Cards headless>
              <div style={{ textAlign: 'center', marginBottom: '25px', marginTop: '-30px' }}>
                <Button
                  className="btn-signin"
                  type={disable ? '' : 'primary'}
                  size="large"
                  onClick={addQuestion}
                  disabled={disable}
                >
                  Add One More Question
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
    </>
  );
};

export default AddQuestion;

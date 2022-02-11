import React, { useState, useEffect } from 'react';
import { Table } from 'antd';
import FeatherIcon from 'feather-icons-react';
import { sortableContainer, sortableElement, sortableHandle } from 'react-sortable-hoc';
import arrayMove from 'array-move';
import PropTypes from 'prop-types';
import { TableWrapper, DragDropStyle, Main } from '../../styled';
import Heading from '../../../components/heading/heading';
import { Button } from '../../../components/buttons/buttons';
import { Cards } from '../../../components/cards/frame/cards-frame';
import { PageHeader } from '../../../components/page-headers/page-headers';
import { useHistory, useParams } from 'react-router-dom';
import axios from 'axios';
import { setItem, getItem } from '../../../utility/localStorageControl';

const DragHandle = sortableHandle(() => <FeatherIcon style={{ cursor: 'pointer', color: '#999' }} icon="move" />);

const QuestionSelection = () => {
  const { id } = useParams();
  const history = useHistory();
  const [questions, setQuestions] = useState([]);
  const [survey, setSurvey] = useState(null);
  const [state, setState] = useState({
    dataSource: questions,
  });
  useEffect(() => {
    getAllSurveys();
  }, []);

  const getAllSurveys = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    const URL = `${api}surveyManagement/surveyQuestions/${id}`;
    var config = {
      method: 'get',
      url: URL,
      headers: {
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${user.accessToken}`,
      },
    };
    axios(config)
      .then(function(response) {
        const result = response.data;
        setSurvey(result);
        let questionsTableData = [];
        // let combinedArray = [...result.questionsMCQ, ...result.questionsMMCQ, ...result.questionsDropDown];
        result.questions.map((questionss, index) => {
          console.log(questionss, 'dccdc');
          questionsTableData.push({
            key: index + 1,
            index,
            questionNumber: parseInt(questionss.questionNumber),
            questionDetail: questionss.question.questionStem,
            mcqOptions: questionss.questionType === 'MCQ' ? '' : `${questionss.question.options}`,
            mmcqOptions: questionss.questionType === 'MMCQ' ? '' : `${questionss.question.options}`,
            dropdownOptions: questionss.questionType === 'DropDown' ? '' : `${questionss.question.options}`,
          });
        });
        setState({ dataSource: questionsTableData });
        setQuestions(questionsTableData);
      })
      .catch(function(error) {
        console.log(error);
      });
  };
  const questionsTableData = [];

  const questionsTableColumns = [
    {
      dataIndex: 'sort',
      width: 30,
      className: 'drag-visible',
      render: () => <DragHandle />,
    },
    {
      dataIndex: 'questionNumber',
    },

    {
      dataIndex: 'questionDetail',
    },
    {
      dataIndex: 'mcqOptions',
    },
    {
      dataIndex: 'mmcqOptions',
    },
    {
      dataIndex: 'dropdownOptions',
    },
    // {
    //   dataIndex: 'option4',
    // },
  ];

  const { dataSource } = state;

  const SortableItem = sortableElement(props => <tr {...props} />);
  const SortableContainer = sortableContainer(props => <tbody {...props} />);

  const onSortEnd = ({ oldIndex, newIndex }) => {
    if (oldIndex !== newIndex) {
      const newData = arrayMove([].concat(dataSource), oldIndex, newIndex).filter(el => !!el);
      setState({ ...state, dataSource: newData });
    }
  };

  const DraggableBodyRow = ({ className, style, ...restProps }) => {
    // function findIndex base on Table rowKey props and should always be a right array index
    const index = dataSource.findIndex(x => x.index === restProps['data-row-key']);
    return <SortableItem index={index} {...restProps} />;
  };

  DraggableBodyRow.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
  };

  const DraggableContainer = props => (
    <SortableContainer useDragHandle helperClass="row-dragging" onSortEnd={onSortEnd} {...props} />
  );

  const save = () => {
    const user = getItem('user');
    const api = process.env.REACT_APP_BACKEND_API;
    let combined = [];
    state.dataSource.map(question => {
      console.log(question);
      survey.questions.map(ques => {
        if (question.questionNumber === parseInt(ques.questionNumber)) {
          combined.push(ques);
        }
      });
    });
    const country = survey.country;
    const surveyValues = {
      surveyName: survey.name,
      questions: combined,
    };
    console.log('fv', surveyValues);
    var data = new FormData();
    data.append('userJson', JSON.stringify(surveyValues));
    data.append('country',country);
    const URL = `${api}surveyManagement/modifySurvey/${id}`;
    var config = {
      method: 'put',
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
          console.log('Errororror');
          // setShowAlert(true);
          // setAlertText('Group with same name already exists!');
          // setAlertType('danger');
          setTimeout(() => {
            setShowAlert(false);
          }, 3000);
        } else {
          history.push({
            pathname: '/admin/survey',
            state: { detail: `${survey.name} Questions Sorted Successfully` },
          });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  };

  return (
    <>
      <PageHeader title="Arrange Questions" />
      <Main>
        <DragDropStyle>
          <Cards title="Questions Detail">
            <TableWrapper className="table-responsive">
              <Table
                pagination={false}
                dataSource={dataSource}
                columns={questionsTableColumns}
                rowKey="index"
                components={{
                  body: {
                    wrapper: DraggableContainer,
                    row: DraggableBodyRow,
                  },
                }}
              />
            </TableWrapper>
            <Button
              className="btn-signin"
              htmlType="submit"
              type="primary"
              size="large"
              style={{ marginTop: '10px', width: '20%' }}
              onClick={save}
            >
              Save
            </Button>
          </Cards>
        </DragDropStyle>
      </Main>
    </>
  );
};

export default QuestionSelection;

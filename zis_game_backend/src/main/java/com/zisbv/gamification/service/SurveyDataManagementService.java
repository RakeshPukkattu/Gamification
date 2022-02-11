package com.zisbv.gamification.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.entities.AssignedSurveyData;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.SurveyData;
import com.zisbv.gamification.entities.SurveyDropDownQuestionData;
import com.zisbv.gamification.entities.SurveyMCQQuestionData;
import com.zisbv.gamification.entities.SurveyMMCQQuestionData;
import com.zisbv.gamification.entities.SurveyQuestionData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.exceptions.MyFileNotFoundException;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.AssignedSurveyGroupsModel;
import com.zisbv.gamification.models.AssignedSurveyResponseModel;
import com.zisbv.gamification.models.AssignedSurveyUsersModel;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.models.QuestionDataResponse;
import com.zisbv.gamification.models.SurveyModel;
import com.zisbv.gamification.models.SurveyQuestionsResponseModel;
import com.zisbv.gamification.models.SurveyResponseModel;
import com.zisbv.gamification.repositories.AssignedSurveyRepository;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GroupRepository;
import com.zisbv.gamification.repositories.SurveyDropDownQuestionRepository;
import com.zisbv.gamification.repositories.SurveyMCQQuestionsRepository;
import com.zisbv.gamification.repositories.SurveyMMCQQuestionRepository;
import com.zisbv.gamification.repositories.SurveyQuestionDataRepository;
import com.zisbv.gamification.repositories.SurveyRepository;
import com.zisbv.gamification.repositories.UserRepository;

@Service(value = "surveyDataManagementService")
public class SurveyDataManagementService {

	@Autowired
	private SurveyRepository surveyRepository;

	@Autowired
	private SurveyQuestionDataRepository surveyQuestionDataRepository;

	@Autowired
	private SurveyMCQQuestionsRepository surveyMCQQuestionsRepository;

	@Autowired
	private SurveyMMCQQuestionRepository surveyMMCQQuestionsRepository;

	@Autowired
	private SurveyDropDownQuestionRepository surveyDropDownQuestionsRepository;

	@Autowired
	AssignedSurveyRepository assignedSurveyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	CountryRepository countryRepository;

	@Value("${dev.url}")
	String devURl;

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	// Native methods --> Start

	public List<SurveyData> findAllSurvey() {
		List<SurveyData> list = new ArrayList<>();
		surveyRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	public SurveyData findWithSurveyid(Long id) {
		SurveyData dataFromDB = surveyRepository.getSurveyWithID(id);
		return dataFromDB;
	}

	public SurveyData findWithSurveyName(String name) {
		SurveyData dataFromDB = surveyRepository.getSurveyWithName(name);
		return dataFromDB;
	}

	public SurveyQuestionData findWithQuestionDataId(Long id) {
		SurveyQuestionData dataFromDB = surveyQuestionDataRepository.getSurveyQuestionWithID(id);
		return dataFromDB;
	}

	public SurveyMCQQuestionData findWithMCQQuestionId(Long id) {
		SurveyMCQQuestionData dataFromDB = surveyMCQQuestionsRepository.getSurveyQuestionWithID(id);
		return dataFromDB;
	}

	public SurveyMMCQQuestionData findWithMMCQQuestionId(Long id) {
		SurveyMMCQQuestionData dataFromDB = surveyMMCQQuestionsRepository.getSurveyQuestionWithID(id);
		return dataFromDB;
	}

	public SurveyDropDownQuestionData findWithDropDownQuestionId(Long id) {
		SurveyDropDownQuestionData dataFromDB = surveyDropDownQuestionsRepository.getSurveyQuestionWithID(id);
		return dataFromDB;
	}

	public SurveyData updateSurvey(SurveyData survey) {
		return surveyRepository.save(survey);
	}

	public AssignedSurveyData getSurveyAssignmentsData(Long id) {
		return assignedSurveyRepository.getAssignedSurveyWithSurveyID(id);
	}

	public void removeSurveyAssignments(Long id) {
		assignedSurveyRepository.deleteById(id);
	}

	public void removeSurveyMCQuestion(Long id) {
		surveyMCQQuestionsRepository.deleteById(id);
	}

	public void removeSurveyMMCQuestion(Long id) {
		surveyMMCQQuestionsRepository.deleteById(id);
	}

	public void removeSurveyDropDownQuestion(Long id) {
		surveyDropDownQuestionsRepository.deleteById(id);
	}

	public void removeSurveyQuestionData(Long id) {
		surveyQuestionDataRepository.deleteById(id);
	}

	public void removeSurvey(Long id) {
		surveyRepository.deleteById(id);
	}

	// Native methods --> End

	// Business Logic --> Start

	// Add survey
	public AppResponse createSurvey(String surveyJson, Long countryID) throws JSONException {
		// Input data
		JSONObject surveyDataInput = new JSONObject(surveyJson);

		SurveyData surveyData = new SurveyData();
		SurveyQuestionData surveyQuestionData = new SurveyQuestionData();
		String questionNOString = null;
		String questionTypeString = null;
		String questionIDString = null;
		int counter = 0;

		String surveyName = (String) surveyDataInput.get("surveyName");

		if (surveyRepository.getSurveyWithName(surveyName) != null) {
			throw new MyFileNotFoundException("Survey name is already used !!!!");
		}

		surveyData.setSurveyName(surveyName);
		// Country countryData = countryRepository.getCountryWithID(countryID);
		// surveyData.setCountryName(countryData.getCountryName());
		surveyData.setCountryID(countryID);
		surveyData.setEnableDisableStatus(true);
		surveyData.setIsAssigned(false);

		JSONArray questions = (JSONArray) surveyDataInput.get("questions");
		for (int i = 0; i < questions.length(); i++) {

			JSONObject questionData = (JSONObject) questions.get(i);
			surveyQuestionData.setSurveyName(surveyName);

			// Question numbers
			String questionNo = (String) questionData.get("questionNo");
			if (questionNOString == null) {
				questionNOString = questionNo;
			} else {
				questionNOString = questionNOString + "," + questionNo;
			}

			// Question types
			String questionType = (String) questionData.get("questionType");
			if (questionTypeString == null) {
				questionTypeString = questionType;
			} else {
				questionTypeString = questionTypeString + "," + questionType;
			}

			// Adding MCQ to DB.
			if (questionType.equalsIgnoreCase("MCQ")) {

				JSONObject questionStemData = (JSONObject) questionData.get("question");
				SurveyMCQQuestionData surveyMCQQuestionData = new SurveyMCQQuestionData();

				// Question Stem
				String questionStem = (String) questionStemData.get("questionStem");
				surveyMCQQuestionData.setQuestionStem(questionStem);

				// Options
				JSONArray options = (JSONArray) questionStemData.get("options");
				String optionsString = null;
				for (int j = 0; j < options.length(); j++) {
					String option = (String) options.get(j);
					if (optionsString == null) {
						optionsString = option;
					} else {
						optionsString = optionsString + "," + option;
					}
				}
				surveyMCQQuestionData.setOptions(optionsString);

				// Answer
				String answer = (String) questionStemData.get("answer");
				surveyMCQQuestionData.setAnswer(answer);
				String sID = surveyName + counter;
				counter++;
				surveyMCQQuestionData.setsID(sID);
				surveyMCQQuestionData.setLast_modified(new Date());
				surveyMCQQuestionsRepository.save(surveyMCQQuestionData);

				// getting mcqID
				SurveyMCQQuestionData dataFromDB = surveyMCQQuestionsRepository.getSurveyQuestionWithSID(sID);
				Long idMCQ = dataFromDB.getId();

				if (questionIDString == null) {
					questionIDString = String.valueOf(idMCQ);
				} else {
					questionIDString = questionIDString + "," + String.valueOf(idMCQ);
				}

			}
			// Adding MCQ to DB.-END>

			// Adding MMCQ to DB.
			if (questionType.equalsIgnoreCase("MMCQ")) {

				JSONObject questionStemData = (JSONObject) questionData.get("question");
				SurveyMMCQQuestionData questionMMCQ = new SurveyMMCQQuestionData();

				// Question Stem
				String questionStem = (String) questionStemData.get("questionStem");
				questionMMCQ.setQuestionStem(questionStem);

				// Options
				JSONArray options = (JSONArray) questionStemData.get("options");
				String optionsString = null;
				for (int j = 0; j < options.length(); j++) {
					String option = (String) options.get(j);
					if (optionsString == null) {
						optionsString = option;
					} else {
						optionsString = optionsString + "," + option;
					}
				}
				questionMMCQ.setOptions(optionsString);

				// Answers
				JSONArray answers = (JSONArray) questionStemData.get("answers");
				String answersString = null;
				for (int j = 0; j < answers.length(); j++) {
					String answer = (String) answers.get(j);
					if (answersString == null) {
						answersString = answer;
					} else {
						answersString = answersString + "," + answer;
					}
				}
				questionMMCQ.setAnswers(answersString);
				String sID = surveyName + counter;
				counter++;
				questionMMCQ.setsID(sID);
				questionMMCQ.setLast_modified(new Date());
				surveyMMCQQuestionsRepository.save(questionMMCQ);

				// getting mcqID
				SurveyMMCQQuestionData dataFromDB = surveyMMCQQuestionsRepository.getSurveyQuestionWithSID(sID);
				Long idMMCQ = dataFromDB.getId();

				if (questionIDString == null) {
					questionIDString = String.valueOf(idMMCQ);
				} else {
					questionIDString = questionIDString + "," + String.valueOf(idMMCQ);
				}

			}
			// Adding MMCQ to DB.END>

			// Adding DropDown to DB.
			if (questionType.equalsIgnoreCase("DropDown")) {

				JSONObject questionStemData = (JSONObject) questionData.get("question");
				SurveyDropDownQuestionData questionDropDown = new SurveyDropDownQuestionData();

				// QuestionStem
				String questionStem = (String) questionStemData.get("questionStem");
				questionDropDown.setQuestionStem(questionStem);

				// Options
				JSONArray options = (JSONArray) questionStemData.get("options");
				String optionsString = null;
				for (int j = 0; j < options.length(); j++) {
					String option = (String) options.get(j);
					if (optionsString == null) {
						optionsString = option;
					} else {
						optionsString = optionsString + "," + option;
					}
				}
				questionDropDown.setOptions(optionsString);

				// Labels
				JSONArray labels = (JSONArray) questionStemData.get("labels");
				String labelsString = null;
				for (int j = 0; j < labels.length(); j++) {
					String label = (String) labels.get(j);
					if (labelsString == null) {
						labelsString = label;
					} else {
						labelsString = labelsString + "," + label;
					}
				}
				questionDropDown.setLabels(labelsString);

				// Answers
				JSONArray answers = (JSONArray) questionStemData.get("answers");
				String answersString = null;
				for (int j = 0; j < answers.length(); j++) {
					String answer = (String) answers.get(j);
					if (answersString == null) {
						answersString = answer;
					} else {
						answersString = answersString + "," + answer;
					}
				}
				questionDropDown.setAnswers(answersString);
				String sID = surveyName + counter;
				counter++;
				questionDropDown.setsID(sID);
				questionDropDown.setLast_modified(new Date());
				surveyDropDownQuestionsRepository.save(questionDropDown);

				// getting dropDownID
				SurveyDropDownQuestionData dataFromDB = surveyDropDownQuestionsRepository.getSurveyQuestionWithSID(sID);
				Long idDropDown = dataFromDB.getId();

				if (questionIDString == null) {
					questionIDString = String.valueOf(idDropDown);
				} else {
					questionIDString = questionIDString + "," + String.valueOf(idDropDown);
				}
			}
			// Adding DropDown to DB.END>

		}

		surveyQuestionData.setQuestionNumbers(questionNOString);
		surveyQuestionData.setQuestionTypes(questionTypeString);
		surveyQuestionData.setQuestionIDs(questionIDString);
		surveyQuestionData.setLast_modified(new Date());
		surveyQuestionDataRepository.save(surveyQuestionData);

		SurveyQuestionData dataFromDb = surveyQuestionDataRepository.getSurveyQuestionWithName(surveyName);
		Long id = dataFromDb.getId();
		surveyData.setQuestionArrayID(String.valueOf(id));
		surveyRepository.save(surveyData);

		return new AppResponse("200", "Survey added successfully.");
	}

	public AppResponse modifySurvey(String surveyJson, Long id, Long countryID) throws JSONException {

		String assignedUserIDS = null;
		String assignedGroupIDS = null;

		SurveyData dataFromDb = surveyRepository.getById(id);
		// String surveyNameFromDB = dataFromDb.getSurveyName();
		String questionDataIDFromDB = dataFromDb.getQuestionArrayID();

		SurveyQuestionData questionDataFromDB = surveyQuestionDataRepository
				.getById(Long.valueOf(questionDataIDFromDB));
		String questionTypes = questionDataFromDB.getQuestionTypes();
		String[] questionTypeArray = questionTypes.split(",");
		String questionIDs = questionDataFromDB.getQuestionIDs();
		String[] questionIDsArray = questionIDs.split(",");
		int typeLength = questionTypeArray.length;
		int counter = -1;

		for (String questionID : questionIDsArray) {
			counter = counter + 1;
			if (counter < typeLength) {
				String type = questionTypeArray[counter];
				if (type.equalsIgnoreCase("MCQ")) {
					removeSurveyMCQuestion(Long.valueOf(questionID));
				} else if (type.equalsIgnoreCase("MMCQ")) {
					removeSurveyMMCQuestion(Long.valueOf(questionID));
				} else if (type.equalsIgnoreCase("DropDown")) {
					removeSurveyDropDownQuestion(Long.valueOf(questionID));
				}
			}
		}

		removeSurveyQuestionData(Long.valueOf(questionDataIDFromDB));

		if (dataFromDb.getIsAssigned()) {
			AssignedSurveyData assignedDetails = assignedSurveyRepository.getAssignedSurveyWithSurveyID(id);
			assignedUserIDS = assignedDetails.getUserID();
			assignedGroupIDS = assignedDetails.getGroupID();
		}

		removeSurvey(id);

		createSurvey(surveyJson, countryID);
		// Input data
		JSONObject surveyDataInput = new JSONObject(surveyJson);
		String surveyName = (String) surveyDataInput.get("surveyName");
		SurveyData newSurvey = surveyRepository.getSurveyWithName(surveyName);
		Long newID = newSurvey.getId();
		if (assignedUserIDS != null || assignedGroupIDS != null) {
			AssignedSurveyData assignedDetails = new AssignedSurveyData();
			assignedDetails.setSurveyID(newID);
			assignedDetails.setUserID(assignedUserIDS);
			assignedDetails.setGroupID(assignedGroupIDS);
			assignedDetails.setLast_modified(new Date());
			saveAssignment(assignedDetails);
		}

		return new AppResponse("200", "Survey added successfully.");
	}

	// Update Status
	public SurveyData updateStatus(SurveyData existingSurvey, Boolean status) {

		SurveyData survey = new SurveyData();

		survey.setId(existingSurvey.getId());
		survey.setSurveyName(existingSurvey.getSurveyName());
		survey.setCountryID(existingSurvey.getCountryID());
		// survey.setCountryName(existingSurvey.getCountryName());
		survey.setEnableDisableStatus(status);
		survey.setIsAssigned(existingSurvey.getIsAssigned());
		survey.setQuestionArrayID(existingSurvey.getQuestionArrayID());

		return surveyRepository.save(survey);
	}

	// Get all surveys
	public SurveyResponseModel getSurveyResponseModel() {

		// final response
		SurveyResponseModel surveyResponseModel = new SurveyResponseModel();
		List<SurveyModel> responseModel = new ArrayList<SurveyModel>();

		// Data from database
		List<SurveyData> dataFromDb = findAllSurvey();

		// Iterating data from database
		for (SurveyData eachSurvey : dataFromDb) {

			List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();
			SurveyModel survey = new SurveyModel();
			survey.setSurveyId(eachSurvey.getId());
			survey.setSurveyName(eachSurvey.getSurveyName());
			Country countryFromDB = countryRepository.getCountryWithID(eachSurvey.getCountryID());
			survey.setCountryName(countryFromDB.getCountryName());
			survey.setEnableDisableStatus(eachSurvey.getEnableDisableStatus());
			survey.setIsAssigned(eachSurvey.getIsAssigned());

			// Question details
			String questionArrrayID = eachSurvey.getQuestionArrayID();
			SurveyQuestionData questionData = surveyQuestionDataRepository
					.getSurveyQuestionWithID(Long.parseLong(questionArrrayID));

			// question number in survey
			String questionNumbersString = questionData.getQuestionNumbers();
			String[] questionNumbersStringArray = questionNumbersString.split(",");

			// question type
			String questiontypesString = questionData.getQuestionTypes();
			String[] questiontypesStringArray = questiontypesString.split(",");
			int typeCount = -1;

			// question ids
			String questionIDsString = questionData.getQuestionIDs();
			String[] questionIDsStringArray = questionIDsString.split(",");
			int idCount = -1;

			// Populating
			for (String questionNumber : questionNumbersStringArray) {

				typeCount = typeCount + 1;
				idCount = idCount + 1;
				QuestionDataResponse responseQuestion = new QuestionDataResponse();
				responseQuestion.setQuestionNumber(questionNumber);

				if (typeCount < questiontypesStringArray.length) {

					// question type
					String type = questiontypesStringArray[typeCount];
					responseQuestion.setQuestionType(type);

					if (idCount < questionIDsStringArray.length) {

						Long localid = Long.parseLong(questionIDsStringArray[idCount]);

						// MCQ
						if (type.equalsIgnoreCase("MCQ")) {
							SurveyMCQQuestionData question = surveyMCQQuestionsRepository.getById(localid);
							responseQuestion.setQuestion(question);
							questions.add(responseQuestion);
						}

						// MMCQ
						if (type.equalsIgnoreCase("MMCQ")) {
							SurveyMMCQQuestionData question = surveyMMCQQuestionsRepository.getById(localid);
							responseQuestion.setQuestion(question);
							questions.add(responseQuestion);
						}

						// Drop Down
						if (type.equalsIgnoreCase("DropDown")) {
							SurveyDropDownQuestionData question = surveyDropDownQuestionsRepository.getById(localid);
							responseQuestion.setQuestion(question);
							questions.add(responseQuestion);
						}
					}
				}
			}

			// add questions here
			survey.setQuestions(questions);

			responseModel.add(survey);
		}

		surveyResponseModel.setSurveys(responseModel);
		return surveyResponseModel;
	}

	// Get all surveys
	public SurveyResponseModel getSurveyResponseModelInCountry(String country) {

		// final response
		SurveyResponseModel surveyResponseModel = new SurveyResponseModel();
		List<SurveyModel> responseModel = new ArrayList<SurveyModel>();

		// Data from database
		List<SurveyData> dataFromDb = findAllSurvey();

		// Iterating data from database
		for (SurveyData eachSurvey : dataFromDb) {
			Long countryID = eachSurvey.getCountryID();
			Country countryData = countryRepository.getById(countryID);

			if (countryData.getCountryName().equalsIgnoreCase(country)) {
				List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();
				SurveyModel survey = new SurveyModel();
				survey.setSurveyId(eachSurvey.getId());
				survey.setSurveyName(eachSurvey.getSurveyName());
				survey.setCountryName(countryData.getCountryName());
				survey.setEnableDisableStatus(eachSurvey.getEnableDisableStatus());
				survey.setIsAssigned(eachSurvey.getIsAssigned());

				// Question details
				String questionArrrayID = eachSurvey.getQuestionArrayID();
				SurveyQuestionData questionData = surveyQuestionDataRepository
						.getSurveyQuestionWithID(Long.parseLong(questionArrrayID));

				// question number in survey
				String questionNumbersString = questionData.getQuestionNumbers();
				String[] questionNumbersStringArray = questionNumbersString.split(",");

				// question type
				String questiontypesString = questionData.getQuestionTypes();
				String[] questiontypesStringArray = questiontypesString.split(",");
				int typeCount = -1;

				// question ids
				String questionIDsString = questionData.getQuestionIDs();
				String[] questionIDsStringArray = questionIDsString.split(",");
				int idCount = -1;

				// Populating
				for (String questionNumber : questionNumbersStringArray) {

					typeCount = typeCount + 1;
					idCount = idCount + 1;
					QuestionDataResponse responseQuestion = new QuestionDataResponse();
					responseQuestion.setQuestionNumber(questionNumber);

					if (typeCount < questiontypesStringArray.length) {

						// question type
						String type = questiontypesStringArray[typeCount];
						responseQuestion.setQuestionType(type);

						if (idCount < questionIDsStringArray.length) {

							Long localid = Long.parseLong(questionIDsStringArray[idCount]);

							// MCQ
							if (type.equalsIgnoreCase("MCQ")) {
								SurveyMCQQuestionData question = surveyMCQQuestionsRepository.getById(localid);
								responseQuestion.setQuestion(question);
								questions.add(responseQuestion);
							}

							// MMCQ
							if (type.equalsIgnoreCase("MMCQ")) {
								SurveyMMCQQuestionData question = surveyMMCQQuestionsRepository.getById(localid);
								responseQuestion.setQuestion(question);
								questions.add(responseQuestion);
							}

							// Drop Down
							if (type.equalsIgnoreCase("DropDown")) {
								SurveyDropDownQuestionData question = surveyDropDownQuestionsRepository
										.getById(localid);
								responseQuestion.setQuestion(question);
								questions.add(responseQuestion);
							}
						}
					}
				}

				// add questions here
				survey.setQuestions(questions);

				responseModel.add(survey);
			}
		}

		surveyResponseModel.setSurveys(responseModel);
		return surveyResponseModel;
	}

	// Get survey [ID]
	public SurveyModel getSurveyModel(Long id) {

		List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();
		SurveyData surveyData = surveyRepository.getById(id);

		SurveyModel surveyModel = new SurveyModel();
		surveyModel.setSurveyId(surveyData.getId());
		surveyModel.setSurveyName(surveyData.getSurveyName());
		Country countryData = countryRepository.getCountryWithID(surveyData.getCountryID());
		surveyModel.setCountryName(countryData.getCountryName());
		surveyModel.setEnableDisableStatus(surveyData.getEnableDisableStatus());
		surveyModel.setIsAssigned(surveyData.getIsAssigned());

		// Question details
		String questionArrrayID = surveyData.getQuestionArrayID();
		SurveyQuestionData questionData = surveyQuestionDataRepository
				.getSurveyQuestionWithID(Long.parseLong(questionArrrayID));

		// question number in survey
		String questionNumbersString = questionData.getQuestionNumbers();
		String[] questionNumbersStringArray = questionNumbersString.split(",");

		// question type
		String questiontypesString = questionData.getQuestionTypes();
		String[] questiontypesStringArray = questiontypesString.split(",");
		int typeCount = -1;

		// question ids
		String questionIDsString = questionData.getQuestionIDs();
		String[] questionIDsStringArray = questionIDsString.split(",");
		int idCount = -1;

		// Populating
		for (String questionNumber : questionNumbersStringArray) {

			typeCount = typeCount + 1;
			idCount = idCount + 1;
			QuestionDataResponse responseQuestion = new QuestionDataResponse();
			responseQuestion.setQuestionNumber(questionNumber);

			if (typeCount < questiontypesStringArray.length) {

				// question type
				String type = questiontypesStringArray[typeCount];
				responseQuestion.setQuestionType(type);

				if (idCount < questionIDsStringArray.length) {

					Long localid = Long.parseLong(questionIDsStringArray[idCount]);

					// MCQ
					if (type.equalsIgnoreCase("MCQ")) {
						SurveyMCQQuestionData question = surveyMCQQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}

					// MMCQ
					if (type.equalsIgnoreCase("MMCQ")) {
						SurveyMMCQQuestionData question = surveyMMCQQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}

					// Drop Down
					if (type.equalsIgnoreCase("DropDown")) {
						SurveyDropDownQuestionData question = surveyDropDownQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}
				}
			}
		}

		// add questions here
		surveyModel.setQuestions(questions);

		return surveyModel;
	}

	public SurveyQuestionsResponseModel getSurveyQuestionsResponseModel(Long id) {

		// final response
		SurveyQuestionsResponseModel surveyQuestionsResponseModel = new SurveyQuestionsResponseModel();
		List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();

		// Data from database
		SurveyData dataFromDb = findWithSurveyid(id);

		// Populating database
		surveyQuestionsResponseModel.setId(dataFromDb.getId());
		surveyQuestionsResponseModel.setName(dataFromDb.getSurveyName());

		// Question details
		String questionArrrayID = dataFromDb.getQuestionArrayID();
		SurveyQuestionData questionData = surveyQuestionDataRepository
				.getSurveyQuestionWithID(Long.parseLong(questionArrrayID));

		// question number in survey
		String questionNumbersString = questionData.getQuestionNumbers();
		String[] questionNumbersStringArray = questionNumbersString.split(",");

		// question type
		String questiontypesString = questionData.getQuestionTypes();
		String[] questiontypesStringArray = questiontypesString.split(",");
		int typeCount = -1;

		// question ids
		String questionIDsString = questionData.getQuestionIDs();
		String[] questionIDsStringArray = questionIDsString.split(",");
		int idCount = -1;

		// Populating
		for (String questionNumber : questionNumbersStringArray) {

			typeCount = typeCount + 1;
			idCount = idCount + 1;
			QuestionDataResponse responseQuestion = new QuestionDataResponse();
			responseQuestion.setQuestionNumber(questionNumber);

			if (typeCount < questiontypesStringArray.length) {

				// question type
				String type = questiontypesStringArray[typeCount];
				responseQuestion.setQuestionType(type);

				if (idCount < questionIDsStringArray.length) {

					Long localid = Long.parseLong(questionIDsStringArray[idCount]);

					// MCQ
					if (type.equalsIgnoreCase("MCQ")) {
						SurveyMCQQuestionData question = surveyMCQQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}

					// MMCQ
					if (type.equalsIgnoreCase("MMCQ")) {
						SurveyMMCQQuestionData question = surveyMMCQQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}

					// Drop Down
					if (type.equalsIgnoreCase("DropDown")) {
						SurveyDropDownQuestionData question = surveyDropDownQuestionsRepository.getById(localid);
						responseQuestion.setQuestion(question);
						questions.add(responseQuestion);
					}
				}
			}
		}

		surveyQuestionsResponseModel.setQuestions(questions);
		return surveyQuestionsResponseModel;
	}

	public void saveAssignment(AssignedSurveyData assignedSurveyDataDB, AssignedSurveyData data) {

		// email notification for users assigned for survey
		if (assignedSurveyDataDB.getUserID() != null) {
			if (!assignedSurveyDataDB.getUserID().equals("")) {

				String userIdInExistingSurvey = assignedSurveyDataDB.getUserID();
				String[] userIdInExistingSurveyArray = userIdInExistingSurvey.split(",");

				String userIdInNewSurvey = data.getUserID();
				String[] userIdInNewSurveyArray = userIdInNewSurvey.split(",");

				for (int i = 0; i < userIdInExistingSurveyArray.length; i++) {

					// if
					// (Arrays.stream(userIdInNewSurveyArray).anyMatch(userIdInExistingSurveyArray[i]::equalsIgnoreCase))
					// {
					// UserData userModel =
					// userRepository.findById(Long.parseLong(userIdInExistingSurveyArray[i]));
					// sendSurveyModifiedEmail(userModel, devURl);
					// }
					if (!Arrays.stream(userIdInNewSurveyArray)
							.anyMatch(userIdInExistingSurveyArray[i]::equalsIgnoreCase)) {
						UserData userModel = userRepository.findById(Long.parseLong(userIdInExistingSurveyArray[i]));
						sendSurveyEndedEmail(userModel, devURl);
					}
				}

				for (int i = 0; i < userIdInNewSurveyArray.length; i++) {
					if (!Arrays.stream(userIdInExistingSurveyArray)
							.anyMatch(userIdInNewSurveyArray[i]::equalsIgnoreCase)) {
						UserData userModel = userRepository.findById(Long.parseLong(userIdInNewSurveyArray[i]));
						sendSurveyAssignedEmail(userModel, devURl);
					}
				}

			}
		}

		// email notification for groups assigned for survey
		if (assignedSurveyDataDB.getGroupID() != null) {
			if (!assignedSurveyDataDB.getGroupID().equals("")) {

				String groupIdInExistingSurvey = assignedSurveyDataDB.getGroupID();
				String[] groupIdInExistingSurveyArray = groupIdInExistingSurvey.split(",");

				String groupIdInNewSurvey = data.getGroupID();
				String[] groupIdInNewSurveyArray = groupIdInNewSurvey.split(",");

				for (int i = 0; i < groupIdInExistingSurveyArray.length; i++) {

					// if
					// (Arrays.stream(userIdInNewSurveyArray).anyMatch(userIdInExistingSurveyArray[i]::equalsIgnoreCase))
					// {
					// UserData userModel =
					// userRepository.findById(Long.parseLong(userIdInExistingSurveyArray[i]));
					// sendSurveyModifiedEmail(userModel, devURl);
					// }
					if (!Arrays.stream(groupIdInNewSurveyArray)
							.anyMatch(groupIdInExistingSurveyArray[i]::equalsIgnoreCase)) {
						GroupData groupData = groupRepository
								.getGroupWithID(Long.parseLong(groupIdInExistingSurveyArray[i]));

						List<UserData> groupMembers = groupData.getMembers();
						for (UserData groupMember : groupMembers) {
							sendSurveyEndedEmail(groupMember, devURl, groupData.getGroupName());
						}

//						String memberIds = groupData.getGroupMembersIds();
//						String[] memberIdsArray = memberIds.split(",");
//						for (String memId : memberIdsArray) {
//							UserData userModel = userRepository.findById(Long.parseLong(memId));
//							sendSurveyEndedEmail(userModel, devURl, groupData.getGroupName());
//						}
					}
				}

				for (int i = 0; i < groupIdInNewSurveyArray.length; i++) {
					if (!Arrays.stream(groupIdInExistingSurveyArray)
							.anyMatch(groupIdInNewSurveyArray[i]::equalsIgnoreCase)) {

						GroupData groupData = groupRepository
								.getGroupWithID(Long.parseLong(groupIdInExistingSurveyArray[i]));

						List<UserData> groupMembers = groupData.getMembers();
						for (UserData groupMember : groupMembers) {
							sendSurveyAssignedEmailForGroups(groupMember, devURl, groupData.getGroupName());
						}

//						String memberIds = groupData.getGroupMembersIds();
//						String[] memberIdsArray = memberIds.split(",");
//						for (String memId : memberIdsArray) {
//							UserData userModel = userRepository.findById(Long.parseLong(memId));
//							sendSurveyAssignedEmailForGroups(userModel, devURl, groupData.getGroupName());
//						}
					}
				}
			}
		}

		assignedSurveyRepository.save(data);
	}

	public void saveAssignment(AssignedSurveyData data) {

		// Sending assigned survey notification to users
		if (data.getUserID() != null) {
			if (!data.getUserID().isEmpty()) {

				String userIds = data.getUserID();
				String[] userIdsArray = userIds.split(",");

				for (String userId : userIdsArray) {
					UserData userData = userRepository.findById(Long.parseLong(userId));
					sendSurveyAssignedEmail(userData, devURl);
				}

			}
		}

		// Sending assigned survey notification to groups
		if (data.getGroupID() != null) {
			if (!data.getGroupID().isEmpty()) {

				String groupIds = data.getGroupID();
				String[] groupIdsArray = groupIds.split(",");

				for (String groupId : groupIdsArray) {
					GroupData groupData = groupRepository.getGroupWithID(Long.parseLong(groupId));

					List<UserData> groupMembers = groupData.getMembers();
					for (UserData groupMember : groupMembers) {
						sendSurveyAssignedEmailForGroups(groupMember, devURl, groupData.getGroupName());
					}

//					String groupmembers = groupData.getGroupMembersIds();
//					String[] groupmembersArray = groupmembers.split(",");
//
//					for (String memberId : groupmembersArray) {
//						UserData userData = userRepository.findById(Long.parseLong(memberId));
//						sendSurveyAssignedEmailForGroups(userData, devURl, groupData.getGroupName());
//					}

				}

			}
		}

		assignedSurveyRepository.save(data);
	}

	public void updateAssignedSurveyStatus(SurveyData surveyModel, boolean flag) {

		SurveyData newSurveyData = new SurveyData();
		newSurveyData.setId(surveyModel.getId());
		newSurveyData.setSurveyName(surveyModel.getSurveyName());
		newSurveyData.setCountryID(surveyModel.getCountryID());
		newSurveyData.setQuestionArrayID(surveyModel.getQuestionArrayID());
		newSurveyData.setEnableDisableStatus(surveyModel.getEnableDisableStatus());
		newSurveyData.setIsAssigned(flag);

		updateSurvey(newSurveyData);
	}

	public AssignedSurveyResponseModel getAssignedSurveyResponseModel(String id) {

		// response
		AssignedSurveyResponseModel assignedSurveyResponseModel = new AssignedSurveyResponseModel();

		// getting assignment details with id.
		AssignedSurveyData assignedSurveyData = assignedSurveyRepository
				.getAssignedSurveyWithSurveyID(Long.parseLong(id));

		// getting survey details.
		SurveyQuestionsResponseModel surveyQuestionsResponseModel = getSurveyQuestionsResponseModel(Long.parseLong(id));
		SurveyModel surveyModel = getSurveyModel(Long.parseLong(id));
		Boolean surveyStatus = surveyModel.getEnableDisableStatus();
		Boolean isAssigned = surveyModel.getIsAssigned();

		if (assignedSurveyData != null) {

			// getting users in assigned survey.
			List<AssignedSurveyUsersModel> assignedUsersList = new ArrayList<AssignedSurveyUsersModel>();
			String userIdString = assignedSurveyData.getUserID();

			if (userIdString != null) {
				if (!userIdString.isEmpty()) {
					String[] userIDSArray = userIdString.split(",");
					for (String userID : userIDSArray) {

						AssignedSurveyUsersModel assignedSurveyUsersModel = new AssignedSurveyUsersModel();
						UserData userData = userRepository.findById(Long.parseLong(userID));

						assignedSurveyUsersModel.setUserID(userData.getId());
						assignedSurveyUsersModel.setUserName(userData.getUserName());
						assignedSurveyUsersModel.setUserEmail(userData.getEmail());
						assignedSurveyUsersModel.setUserImageKey(userData.getImageKey());

						assignedUsersList.add(assignedSurveyUsersModel);

					}
				}
			}

			assignedSurveyResponseModel.setAssignedUsers(assignedUsersList);

			// getting groups in assigned survey.
			List<AssignedSurveyGroupsModel> assignedGroupsList = new ArrayList<AssignedSurveyGroupsModel>();
			String groupIDString = assignedSurveyData.getGroupID();

			if (groupIDString != null) {
				if (!groupIDString.isEmpty()) {
					String[] groupIDsArray = groupIDString.split(",");
					for (String groupID : groupIDsArray) {

						AssignedSurveyGroupsModel assignedSurveyGroupsModel = new AssignedSurveyGroupsModel();
						List<AssignedSurveyUsersModel> assignedGroupMembersList = new ArrayList<AssignedSurveyUsersModel>();
						GroupData groupData = groupRepository.getGroupWithID(Long.parseLong(groupID));

						assignedSurveyGroupsModel.setGroupID(groupData.getGroupID());
						assignedSurveyGroupsModel.setGroupName(groupData.getGroupName());
						assignedSurveyGroupsModel.setGroupStatus(groupData.getGroupStatus());

						List<UserData> groupMembers = groupData.getMembers();
						for (UserData groupMember : groupMembers) {
							AssignedSurveyUsersModel assignedSurveyGroupMembersModel = new AssignedSurveyUsersModel();
							assignedSurveyGroupMembersModel.setUserID(groupMember.getId());
							assignedSurveyGroupMembersModel.setUserName(groupMember.getUserName());
							assignedSurveyGroupMembersModel.setUserEmail(groupMember.getEmail());
							assignedSurveyGroupMembersModel.setUserImageKey(groupMember.getImageKey());

							assignedGroupMembersList.add(assignedSurveyGroupMembersModel);
						}

//						String groupMembersIDString = groupData.getGroupMembersIds();
//						String[] groupMembersIDArray = groupMembersIDString.split(",");
//						for (String groupMemberID : groupMembersIDArray) {
//							UserData memberData = userRepository.findById(Long.parseLong(groupMemberID));
//							AssignedSurveyUsersModel assignedSurveyGroupMembersModel = new AssignedSurveyUsersModel();
//							assignedSurveyGroupMembersModel.setUserID(memberData.getId());
//							assignedSurveyGroupMembersModel.setUserName(memberData.getUserName());
//							assignedSurveyGroupMembersModel.setUserEmail(memberData.getEmail());
//							assignedSurveyGroupMembersModel.setUserImageKey(memberData.getImageKey());
//
//							assignedGroupMembersList.add(assignedSurveyGroupMembersModel);
//						}
						assignedSurveyGroupsModel.setAssignedUsers(assignedGroupMembersList);
						assignedGroupsList.add(assignedSurveyGroupsModel);
					}
				}
			}
			assignedSurveyResponseModel.setAssignedGroups(assignedGroupsList);
		}

		// setting response
		assignedSurveyResponseModel.setIsAssigned(isAssigned);
		assignedSurveyResponseModel.setSurveyStatus(surveyStatus);
		assignedSurveyResponseModel.setSurveyQuestionsResponseModel(surveyQuestionsResponseModel);

		return assignedSurveyResponseModel;
	}

	// Business Logic --> End

	// Private Methods --> Start

	private void sendSurveyAssignedEmail(UserData user, String siteURL) {

		try {

			String subject = "Survey Assigned :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>You have been assigned a new survey in Gamification."
					+ "<br>For more details regarding surveys or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	private void sendSurveyAssignedEmailForGroups(UserData user, String siteURL, String groupName) {
		try {

			String subject = "Survey Assigned for Group :: Gamification.";
			String content = "Dear [[name]],<br>"
					+ "<br><br><br>Your Group [[group]] have been assigned a new survey in Gamification."
					+ "<br>For more details regarding surveys or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[group]]", groupName);
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	private void sendSurveyEndedEmail(UserData user, String siteURL) {

		try {

			String subject = "Survey Assigned :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>Your survey has been completed in Gamification."
					+ "<br>For more details regarding surveys or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	private void sendSurveyEndedEmail(UserData user, String siteURL, String groupName) {

		try {

			String subject = "Survey Assigned :: Gamification.";
			String content = "Dear [[name]],<br>"
					+ "<br><br><br>Your group survey has been completed in Gamification for [[group]]."
					+ "<br>For more details regarding surveys or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[group]]", groupName);
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	///////////////////////////////////////////////////// --> END.

}

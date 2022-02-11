package com.zisbv.gamification.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.dto.AssignIndustryDto;
import com.zisbv.gamification.dto.IndustryDto;
import com.zisbv.gamification.entities.GameData;
import com.zisbv.gamification.entities.IndustryData;
import com.zisbv.gamification.entities.ThemeData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.AssignedIndustryModel;
import com.zisbv.gamification.models.AssignedIndustryModelResponse;
import com.zisbv.gamification.models.GameDataResponse;
import com.zisbv.gamification.models.ModifyIndustryModel;
import com.zisbv.gamification.models.ThemeDataResponse;
import com.zisbv.gamification.repositories.GameDataRepository;
import com.zisbv.gamification.repositories.IndustryDataRepository;
import com.zisbv.gamification.repositories.ThemeDataRepository;
import com.zisbv.gamification.util.AppConstants;

@Service(value = "industryServiceImpl")
public class IndustryService {

	@Autowired
	IndustryDataRepository industryDataRepository;

	@Autowired
	GameDataRepository gameDataRepository;

	@Autowired
	ThemeDataRepository themeDataRepository;

	public List<IndustryData> findAllIndustries() {
		List<IndustryData> list = new ArrayList<>();
		industryDataRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	public IndustryData findWithIndustryId(Long id) {
		IndustryData dataFromDB = industryDataRepository.getIndustryWithID(id);
		return dataFromDB;
	}

	public IndustryData findWitIndustryName(String name) {
		IndustryData dataFromDB = industryDataRepository.getIndustryWithName(name);
		return dataFromDB;
	}

	public AssignedIndustryModelResponse getIndustryModelsList() {

		AssignedIndustryModelResponse finalResponse = new AssignedIndustryModelResponse();

		List<AssignedIndustryModel> industries = new ArrayList<AssignedIndustryModel>();

		List<IndustryData> dataFromDB = findAllIndustries();

		for (IndustryData industryFromDB : dataFromDB) {

			AssignedIndustryModel response = new AssignedIndustryModel();

			response.setIndustryID(industryFromDB.getId());
			response.setIndustryName(industryFromDB.getIndustryName());
			response.setIndustryStatus(industryFromDB.getStatus());

			if (industryFromDB.getGameID() != null) {
				if (industryFromDB.getGameID() != "") {
					String gameIdsString = industryFromDB.getGameID();
					String[] gameIdsStringArray = gameIdsString.split(",");
					List<GameDataResponse> games = new ArrayList<GameDataResponse>();

					for (String gameid : gameIdsStringArray) {
						if (gameid != null) {
							if (!gameid.equals("")) {
								Long id = Long.parseLong(gameid);
								GameData gamesFromDB = gameDataRepository.getById(id);
								GameDataResponse game = new GameDataResponse();
								game.setId(gamesFromDB.getId());
								game.setGameName(gamesFromDB.getGameName());
								game.setThumbNailKey(gamesFromDB.getThumbNailKey());
								game.setImageKey(gamesFromDB.getImageKey());
								game.setZipFileKey(gamesFromDB.getZipFileKey());
								game.setKeyWords(gamesFromDB.getKeyWords());
								game.setAssessment(gamesFromDB.getAssessment());
								game.setStatus(gamesFromDB.getGameStatus());

								games.add(game);
							}
						}
					}
					response.setGames(games);
				}
			}

			if (industryFromDB.getThemeID() != null) {
				if (industryFromDB.getThemeID() != "") {
					String themeIdsString = industryFromDB.getThemeID();
					String[] themeIdsStringArray = themeIdsString.split(",");
					List<ThemeDataResponse> themes = new ArrayList<ThemeDataResponse>();

					for (String themeid : themeIdsStringArray) {
						if (themeid != null) {
							if (!themeid.equals("")) {
								Long id = Long.parseLong(themeid);
								ThemeData themeFromDB = themeDataRepository.getById(id);
								ThemeDataResponse theme = new ThemeDataResponse();
								theme.setId(themeFromDB.getId());
								theme.setThemeName(themeFromDB.getThemeName());
								theme.setThumbNailKey(themeFromDB.getThumbNailKey());
								theme.setImageKey(themeFromDB.getImageKey());
								theme.setZipFileKey(themeFromDB.getZipFileKey());
								theme.setKeyWords(themeFromDB.getKeyWords());
								theme.setStatus(themeFromDB.getThemeStatus());

								themes.add(theme);
							}
						}
					}
					response.setThemes(themes);
				}
			}

			industries.add(response);
		}

		finalResponse.setIndustries(industries);
		return finalResponse;
	}

	public AssignedIndustryModel getIndustryModel(Long id) {

		IndustryData dataFromDB = industryDataRepository.getIndustryWithID(id);
		AssignedIndustryModel response = new AssignedIndustryModel();
		response.setIndustryID(dataFromDB.getId());
		response.setIndustryName(dataFromDB.getIndustryName());

		List<ThemeDataResponse> themes = new ArrayList<ThemeDataResponse>();
		String themesID = dataFromDB.getThemeID();
		if (!themesID.equals("")) {

			String[] themesIDArray = themesID.split(",");
			for (String tID : themesIDArray) {
				ThemeData themeFromDB = themeDataRepository.getById(Long.parseLong(tID));
				ThemeDataResponse theme = new ThemeDataResponse();
				theme.setId(themeFromDB.getId());
				theme.setThemeName(themeFromDB.getThemeName());
				theme.setThumbNailKey(themeFromDB.getThumbNailKey());
				theme.setImageKey(themeFromDB.getImageKey());
				theme.setZipFileKey(themeFromDB.getZipFileKey());
				theme.setKeyWords(themeFromDB.getKeyWords());
				theme.setStatus(themeFromDB.getThemeStatus());

				themes.add(theme);
			}
			response.setThemes(themes);
		} else {
			response.setThemes(null);
		}

		List<GameDataResponse> games = new ArrayList<GameDataResponse>();
		String gamesID = dataFromDB.getGameID();
		if (!gamesID.equals("")) {

			String[] gamesIDArray = gamesID.split(",");
			for (String gID : gamesIDArray) {
				GameData gameFromDB = gameDataRepository.getById(Long.parseLong(gID));
				GameDataResponse game = new GameDataResponse();
				game.setId(gameFromDB.getId());
				game.setGameName(gameFromDB.getGameName());
				game.setThumbNailKey(gameFromDB.getThumbNailKey());
				game.setImageKey(gameFromDB.getImageKey());
				game.setZipFileKey(gameFromDB.getZipFileKey());
				game.setKeyWords(gameFromDB.getKeyWords());
				game.setAssessment(gameFromDB.getAssessment());
				game.setStatus(gameFromDB.getGameStatus());

				games.add(game);
			}
			response.setGames(games);
		} else {
			response.setGames(null);
		}

		response.setIndustryStatus(dataFromDB.getStatus());

		return response;

	}

	public void createIndustry(IndustryDto industry) {
		IndustryData newIndustry = new IndustryData();

		newIndustry.setIndustryName(industry.getName());
		newIndustry.setStatus(true);
		newIndustry.setGameID("");
		newIndustry.setThemeID("");
		newIndustry.setLast_modified(new Date());

		industryDataRepository.save(newIndustry);
	}

	public void assignIndustry(AssignIndustryDto assignment, Long id) {
		IndustryData assignedIndustry = new IndustryData();

		IndustryData dataFromDb = industryDataRepository.getIndustryWithID(id);

		assignedIndustry.setId(dataFromDb.getId());
		assignedIndustry.setIndustryName(dataFromDb.getIndustryName());
		assignedIndustry.setStatus(dataFromDb.getStatus());

		//
		Boolean haveTheme = assignment.getThemeId().isEmpty();
		if (haveTheme) {
			assignedIndustry.setThemeID(dataFromDb.getThemeID());
		} else {
			String themeString = assignment.getThemeId().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			assignedIndustry.setThemeID(themeString);

		}

		Boolean haveGame = assignment.getGamesId().isEmpty();
		if (haveGame) {
			assignedIndustry.setGameID(dataFromDb.getGameID());
		} else {
			String gameString = assignment.getGamesId().stream().map(Object::toString).collect(Collectors.joining(","));
			assignedIndustry.setGameID(gameString);
		}
		//

		assignedIndustry.setLast_modified(new Date());

		industryDataRepository.save(assignedIndustry);

	}

	public AppResponse modifyIndustry(ModifyIndustryModel modifiedIndustry, long id) {

		if (industryDataRepository.getById(id) == null) {
			return new AppResponse("400:BAD REQUEST", "Industry not found with id " + id);
		}

		IndustryData dataFromDB = industryDataRepository.getById(id);
		IndustryData modifiedData = new IndustryData();

		modifiedData.setId(dataFromDB.getId());

		if (modifiedIndustry.getIndustryName() != null) {
			if (industryDataRepository.getIndustryWithName(modifiedIndustry.getIndustryName()) != null) {
				return new AppResponse("400:BAD REQUEST", "Duplicate name not allowed.");
			} else {
				modifiedData.setIndustryName(modifiedIndustry.getIndustryName());
			}
		} else {
			modifiedData.setIndustryName(dataFromDB.getIndustryName());
		}

		if (modifiedIndustry.getIndustryStatus() != null) {
			modifiedData.setStatus(modifiedIndustry.getIndustryStatus());
		} else {
			modifiedData.setStatus(dataFromDB.getStatus());
		}

		Boolean isThemeEmpty = modifiedIndustry.getThemes().isEmpty();
		if (isThemeEmpty) {
			modifiedData.setThemeID(dataFromDB.getThemeID());
		} else {

			for (String tID : modifiedIndustry.getThemes()) {
				ThemeData themeFromDB = themeDataRepository.getById(Long.parseLong(tID));
				ThemeData newThemeData = new ThemeData();
				newThemeData.setId(themeFromDB.getId());
				newThemeData.setThemeName(themeFromDB.getThemeName());
				newThemeData.setThumbNailKey(themeFromDB.getThumbNailKey());
				newThemeData.setImageKey(themeFromDB.getImageKey());
				newThemeData.setZipFileKey(themeFromDB.getZipFileKey());
				newThemeData.setKeyWords(themeFromDB.getKeyWords());
				newThemeData.setThemeStatus(themeFromDB.getThemeStatus());
				newThemeData.setLast_modified(new Date());

				themeDataRepository.save(newThemeData);
			}

			String themesString = modifiedIndustry.getThemes().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			modifiedData.setThemeID(themesString);
		}

		Boolean isGameEmpty = modifiedIndustry.getGames().isEmpty();
		if (isGameEmpty) {
			modifiedData.setGameID(dataFromDB.getGameID());
		} else {
			String gamesString = modifiedIndustry.getGames().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			modifiedData.setGameID(gamesString);
		}

		// modifiedData.setIsAssigned(dataFromDB.getIsAssigned());
		modifiedData.setLast_modified(new Date());

		industryDataRepository.save(modifiedData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "modified successfully");
	}
}

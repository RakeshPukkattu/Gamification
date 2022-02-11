package com.zisbv.gamification.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.zisbv.gamification.dto.GameDataDto;
import com.zisbv.gamification.entities.GameData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GameDataResponse;
import com.zisbv.gamification.models.GameDataResponseList;
import com.zisbv.gamification.repositories.GameDataRepository;
import com.zisbv.gamification.util.AppConstants;

@Service("gameDataService")
public class GameDataService {

	@Autowired
	GameDataRepository gameDataRepository;

	@Autowired
	FileStorageGameService fileStorageGameService;

	// GET ALL GAMES FROM DB
	public List<GameData> findAll() {

		List<GameData> gamesListFromDB = new ArrayList<GameData>();
		gameDataRepository.findAll().iterator().forEachRemaining(gamesListFromDB::add);
		return gamesListFromDB;

	}

	// GET GAME FROM DB USING ID
	public GameData findWithid(Long id) {

		GameData dataFromDB = gameDataRepository.getGameDataWithID(id);
		return dataFromDB;

	}

	// GET GAME FROM DB USING NAME
	public GameData findWithName(String name) {

		GameData dataFromDB = gameDataRepository.getGameDataWithName(name);
		return dataFromDB;

	}

	// GET GAMEDATARESPONSE WITH ID
	public GameDataResponse getGameDataResponse(Long id) {
		GameData game = gameDataRepository.getById(id);

		GameDataResponse gameDataResponse = new GameDataResponse();
		gameDataResponse.setId(game.getId());
		gameDataResponse.setGameName(game.getGameName());
		gameDataResponse.setThumbNailKey(game.getThumbNailKey());
		gameDataResponse.setImageKey(game.getImageKey());
		gameDataResponse.setZipFileKey(game.getZipFileKey());
		gameDataResponse.setKeyWords(game.getKeyWords());
		gameDataResponse.setAssessment(game.getAssessment());
		gameDataResponse.setStatus(game.getGameStatus());

		return gameDataResponse;
	}

	// GET ALL GAMES AS LIST
	public GameDataResponseList getGameDataResponseList() {
		List<GameData> listFromDB = gameDataRepository.findAll();

		GameDataResponseList gameDataResponseList = new GameDataResponseList();
		List<GameDataResponse> gameDataResponse = new ArrayList<GameDataResponse>();

		// iterate listFromDB and add to response
		for (GameData game : listFromDB) {
			GameDataResponse response = new GameDataResponse();
			response.setId(game.getId());
			response.setGameName(game.getGameName());
			response.setThumbNailKey(game.getThumbNailKey());
			response.setImageKey(game.getImageKey());
			response.setZipFileKey(game.getZipFileKey());
			response.setKeyWords(game.getKeyWords());
			response.setAssessment(game.getAssessment());
			response.setStatus(game.getGameStatus());
			gameDataResponse.add(response);
		}

		gameDataResponseList.setGames(gameDataResponse);
		return gameDataResponseList;

	}

	// CREATE NEW GAME.
	public void createGame(GameDataDto gameData, String thumbNailKey, String imageKey, String zipFileKey) {

		GameData data = new GameData();
		data.setGameName(gameData.getGameName());
		data.setThumbNailKey(thumbNailKey);
		data.setImageKey(imageKey);
		data.setZipFileKey(zipFileKey);
		String keywordsString = gameData.getKeywords().stream().map(Object::toString).collect(Collectors.joining(","));
		data.setKeyWords(keywordsString);
		data.setAssessment(gameData.getAssessment());
		data.setGameStatus(true);
		data.setLast_modified(new Date());

		gameDataRepository.save(data);

	}

	// MODIFY GAME WITH ID
	public AppResponse modifyGame(GameDataDto modifiedGame, Long id, MultipartFile thumbNail, MultipartFile image,
			MultipartFile file) throws IOException {

		// Check existing game or not
		if (gameDataRepository.getById(id) == null) {
			return new AppResponse("400:BAD REQUEST", "Game not found with id " + id);
		}

		// Getting game from repository
		GameData dataFromDB = gameDataRepository.getById(id);

		// Modified response
		GameData modifiedData = new GameData();

		// ID
		modifiedData.setId(dataFromDB.getId());

		// NAME
		if (modifiedGame != null) {

			if (modifiedGame.getGameName() != null) {
				// Checks duplicate name
				if (gameDataRepository.getGameDataWithName(modifiedGame.getGameName()) != null) {
					return new AppResponse("400:BAD REQUEST", "Duplicate name not allowed.");
				} else {
					modifiedData.setGameName(modifiedGame.getGameName());
				}
			} else {
				modifiedData.setGameName(dataFromDB.getGameName());
			}

		} else {
			modifiedData.setGameName(dataFromDB.getGameName());
		}

		// Thumb nail
		if (thumbNail != null) {
			// uploading thumb nail
			String thumbNailName = fileStorageGameService.storeFile(thumbNail);
			String thumbNailDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("Games/downloadFile/").path(thumbNailName).toUriString();
			modifiedData.setThumbNailKey(thumbNailDownloadUri);
		} else {
			modifiedData.setThumbNailKey(dataFromDB.getThumbNailKey());
		}

		// Image
		if (image != null) {
			// uploading image
			String imageName = fileStorageGameService.storeFile(image);
			String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Games/downloadFile/")
					.path(imageName).toUriString();
			modifiedData.setImageKey(imageDownloadUri);
		} else {
			modifiedData.setImageKey(dataFromDB.getImageKey());
		}

		// File
		if (file != null) {
			// uploading file
			String fileName = fileStorageGameService.storeZipFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Games/downloadFile/")
					.path(fileName).toUriString();
			modifiedData.setZipFileKey(fileDownloadUri);
		} else {
			modifiedData.setZipFileKey(dataFromDB.getZipFileKey());
		}

		// Keywords
		if (modifiedGame != null) {
			if (!modifiedGame.getKeywords().isEmpty()) {
				String keyWordsString = modifiedGame.getKeywords().stream().map(Object::toString)
						.collect(Collectors.joining(","));
				modifiedData.setKeyWords(keyWordsString);
			} else {
				modifiedData.setKeyWords(dataFromDB.getKeyWords());
			}
		} else {
			modifiedData.setKeyWords(dataFromDB.getKeyWords());
		}

		// Assessment
		if (modifiedGame != null) {
			if (modifiedGame.getAssessment() != null) {
				modifiedData.setAssessment(modifiedGame.getAssessment());
			} else {
				modifiedData.setAssessment(dataFromDB.getAssessment());
			}
		} else {
			modifiedData.setAssessment(dataFromDB.getAssessment());
		}

		modifiedData.setGameStatus(dataFromDB.getGameStatus());
		modifiedData.setLast_modified(new Date());

		gameDataRepository.save(modifiedData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "modified successfully");
	}

	// MODIFY GAME STATUS WITH ID
	public AppResponse modifyGameStatus(Boolean status, Long id) {

		GameData dataFromDB = gameDataRepository.getById(id);
		GameData modifiedData = new GameData();
		modifiedData.setId(id);
		modifiedData.setGameName(dataFromDB.getGameName());
		modifiedData.setThumbNailKey(dataFromDB.getThumbNailKey());
		modifiedData.setImageKey(dataFromDB.getImageKey());
		modifiedData.setZipFileKey(dataFromDB.getZipFileKey());
		modifiedData.setKeyWords(dataFromDB.getKeyWords());
		modifiedData.setAssessment(dataFromDB.getAssessment());
		modifiedData.setGameStatus(status);
		modifiedData.setLast_modified(new Date());

		gameDataRepository.save(modifiedData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "status modified successfully");
	}
}

package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.GameDataDto;
import com.zisbv.gamification.entities.GameData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GameDataResponse;
import com.zisbv.gamification.models.GameDataResponseList;
import com.zisbv.gamification.service.FileStorageGameService;
import com.zisbv.gamification.service.GameDataService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/Games")
public class GamesController {

	@Autowired
	GameDataService gameDataService;

	@Autowired
	FileStorageGameService fileStorageGameService;

	ObjectMapper objectMapper = new ObjectMapper();

	// ALL GAMES
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/allGames")
	public GameDataResponseList getGames() {
		return gameDataService.getGameDataResponseList();
	}

	// GAME WITH ID
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/game/{id}")
	public GameDataResponse get(@PathVariable Long id) throws Exception {
		try {
			GameDataResponse result = gameDataService.getGameDataResponse(id);
			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// ADD GAME
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addGame", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse addGame(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String gameJson,
			@RequestParam(required = true, value = "thumbNail") MultipartFile thumbNail,
			@RequestParam(required = true, value = "image") MultipartFile image,
			@RequestParam(required = true, value = AppConstants.USER_FILE_PARAM) MultipartFile file)
			throws JsonParseException, JsonMappingException, IOException {

		GameDataDto gameDataDto = objectMapper.readValue(gameJson, GameDataDto.class);

		// uploading thumb nail
		String thumbNailName = fileStorageGameService.storeFile(thumbNail);
		String thumbNailDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Games/downloadFile/")
				.path(thumbNailName).toUriString();

		// uploading image
		String imageName = fileStorageGameService.storeFile(image);
		String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Games/downloadFile/")
				.path(imageName).toUriString();

		// uploading zip file
		String zipName = fileStorageGameService.storeZipFile(file);
		String zipDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Games/downloadFile/")
				.path(zipName).toUriString();

		GameData existingGame = gameDataService.findWithName(gameDataDto.getGameName());
		if (existingGame != null) {
			String Name = existingGame.getGameName();
			if (Name != null) {
				return new AppResponse("406", "Game name : " + Name + " is already used. ");
			}
		}

		gameDataService.createGame(gameDataDto, thumbNailDownloadUri, imageDownloadUri, zipDownloadUri);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Game added successfully");
	}

	// MODIFY GAME WITH ID
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyGame/{id}")
	public AppResponse modifyGame(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = false) String modifiedJson,
			@RequestParam(required = false, value = "thumbNail") MultipartFile thumbNail,
			@RequestParam(required = false, value = "image") MultipartFile image,
			@RequestParam(required = false, value = AppConstants.USER_FILE_PARAM) MultipartFile file,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		GameDataDto modifiedGame = null;
		if (modifiedJson != null) {
			modifiedGame = objectMapper.readValue(modifiedJson, GameDataDto.class);
		}

		AppResponse response = gameDataService.modifyGame(modifiedGame, id, thumbNail, image, file);
		return response;
	}

	// MODIFY GAME WITH ID
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyGameStatus/{id}")
	public AppResponse modifyGameStatus(@RequestParam(value = "status", required = true) Boolean status,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		AppResponse response = gameDataService.modifyGameStatus(status, id);
		return response;

	}

	// DOWNLOAD FILES
	@CrossOrigin(origins = "*")
	@GetMapping(value = AppConstants.DOWNLOAD_URI)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileStorageGameService.loadFileAsResource(fileName);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (contentType == null) {
			contentType = AppConstants.DEFAULT_CONTENT_TYPE;
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						String.format(AppConstants.FILE_DOWNLOAD_HTTP_HEADER, resource.getFilename()))
				.body(resource);
	}
}

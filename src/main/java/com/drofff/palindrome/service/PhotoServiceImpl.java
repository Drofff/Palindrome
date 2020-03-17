package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.FormattingUtils.concatPathSegments;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;

@Service
public class PhotoServiceImpl implements PhotoService {

	private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();

	private final FileService fileService;

	@Autowired
	public PhotoServiceImpl(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	public String savePhotoForUser(MultipartFile photo, User user) {
		try {
			validatePhoto(photo);
			String filename = generatePhotoFilenameForUser(user);
			saveFileWithName(photo, filename);
			return filename;
		} catch(IOException e) {
			throw new PalindromeException("Error while saving photo");
		}
	}

	private void validatePhoto(MultipartFile photo) throws IOException {
		validateNotNull(photo, "Photo should be provided");
		validateFileIsNotEmpty(photo);
	}

	private void validateFileIsNotEmpty(MultipartFile file) throws IOException {
		if(isFileEmpty(file)) {
			throw new ValidationException("Photo is empty");
		}
	}

	private boolean isFileEmpty(MultipartFile file) throws IOException {
		return file.getBytes().length == 0;
	}

	private String generatePhotoFilenameForUser(User user) {
		String photoId = UUID.randomUUID().toString();
		return concatPathSegments(user.getUsername(), photoId);
	}

	private void saveFileWithName(MultipartFile photo, String filename) throws IOException {
		fileService.saveFile(filename, photo.getBytes());
	}

	@Override
	public String loadEncodedPhotoByUri(String photoUri) {
		byte[] fileByUri = loadPhotoByUri(photoUri);
		return BASE_64_ENCODER.encodeToString(fileByUri);
	}

	@Override
	public byte[] loadPhotoByUri(String photoUri) {
		validateNotNull(photoUri, "Photo uri is required");
		return fileService.getFileByName(photoUri);
	}

}

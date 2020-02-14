package com.drofff.palindrome.service;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;

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
			String filename = generatePhotoFilenameForUser(user);
			saveFileWithName(photo, filename);
			return filename;
		} catch(IOException e) {
			throw new PalindromeException("Error while saving photo");
		}
	}

	private String generatePhotoFilenameForUser(User user) {
		String photoId = UUID.randomUUID().toString();
		return user.getUsername() + "/" + photoId;
	}

	private void saveFileWithName(MultipartFile photo, String filename) throws IOException {
		fileService.saveFile(filename, photo.getBytes());
	}

	@Override
	public String loadEncodedPhotoByUri(String photoUri) {
		byte[] fileByUri = fileService.getFileByName(photoUri);
		byte[] encodedPhoto = BASE_64_ENCODER.encode(fileByUri);
		return new String(encodedPhoto);
	}

}

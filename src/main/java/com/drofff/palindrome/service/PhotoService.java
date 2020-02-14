package com.drofff.palindrome.service;

import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.User;

public interface PhotoService {

	String savePhotoForUser(MultipartFile photo, User user);

	String loadEncodedPhotoByUri(String photoUri);

}

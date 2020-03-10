package com.drofff.palindrome.service;

import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;

public interface PoliceService {

	void createPoliceProfileWithPhoto(Police police, MultipartFile photo);

	boolean hasNoPoliceProfile(User user);

	Police getPoliceByUserId(String id);

	Police getPoliceById(String id);

}
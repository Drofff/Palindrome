package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import org.springframework.web.multipart.MultipartFile;

public interface PoliceService {

	void createPoliceProfileWithPhoto(Police police, MultipartFile photo);

	void updatePoliceProfile(Police police);

	boolean hasNoPoliceProfile(User user);

	Police getPoliceByUserId(String id);

	Police getPoliceById(String id);

}
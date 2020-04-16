package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import org.springframework.web.multipart.MultipartFile;

public interface PoliceService {

	void createPoliceProfileWithPhoto(Police police, MultipartFile photo);

	boolean hasNoPoliceProfile(User user);

	void updatePoliceProfile(Police police);

	Police getPoliceById(String id);

	Police getPoliceByUserId(String id);

}
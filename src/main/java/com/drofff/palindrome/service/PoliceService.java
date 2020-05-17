package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import org.springframework.web.multipart.MultipartFile;

public interface PoliceService {

	void createPoliceProfileWithPhoto(Police police, MultipartFile photo);

	void updatePoliceProfile(Police police);

	void updatePolicePhoto(MultipartFile photo);

	boolean hasNoPoliceProfile(User user);

	void assignAccessTokenToPoliceWithId(String accessToken, String policeId);

	Police getPoliceById(String id);

	void enableTwoStepAuth();

	void disableTwoStepAuth();

	Police getPoliceByUserId(String id);

}
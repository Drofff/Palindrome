package com.drofff.palindrome.service;

import java.util.List;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.document.Driver;

public interface ChangeRequestService {

	void requestDriverInfoChangeWithComment(Driver driver, String comment);

	void requestCarInfoChangeWithComment(Car car, String comment);

	void approveChangeById(String id);

	void refuseChangeById(String id);

	List<ChangeRequest> getAllChangeRequests();

	ChangeRequest getDriverChangeRequestById(String id);

	ChangeRequest getCarChangeRequestById(String id);

	int countChangeRequests();

}

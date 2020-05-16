package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Police;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ChangeRequestService {

	void requestDriverInfoChangeWithComment(Driver driver, String comment);

	void requestCarInfoChangeWithComment(Car car, String comment);

	void approveChangeById(String id);

	void refuseChangeById(String id);

	List<ChangeRequest> getAllChangeRequests();

	Page<ChangeRequest> getPageOfChangeRequestsSentBy(Police sender, Pageable pageable);

	ChangeRequest getDriverChangeRequestById(String id);

	ChangeRequest getCarChangeRequestById(String id);

	int countChangeRequests();

	Map<LocalDate, Integer> countApprovedChangeRequestsPerLastDays(int days);

}

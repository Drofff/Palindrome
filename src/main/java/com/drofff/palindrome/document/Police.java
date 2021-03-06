package com.drofff.palindrome.document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.annotation.NonEditable;
import com.drofff.palindrome.repository.DepartmentRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Document
public class Police implements UserProfile {

	@Id
	private String id;

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	private String middleName;

	@NotBlank(message = "Position should be provided")
	private String position;

	@NotBlank(message = "Token number is required")
	private String tokenNumber;

	@NonEditable
	private String photoUri;

	@NotBlank(message = "Select a department")
	@FromRepository(DepartmentRepository.class)
	private String departmentId;

	@NonEditable
	private String userId;

	private boolean twoStepAuthEnabled;

	private List<String> accessTokens = new ArrayList<>();

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	@Override
	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isTwoStepAuthEnabled() {
		return twoStepAuthEnabled;
	}

	public void setTwoStepAuthEnabled(boolean twoStepAuthEnabled) {
		this.twoStepAuthEnabled = twoStepAuthEnabled;
	}

	public List<String> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(List<String> accessTokens) {
		this.accessTokens = accessTokens;
	}

}

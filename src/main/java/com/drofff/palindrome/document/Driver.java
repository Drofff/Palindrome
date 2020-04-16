package com.drofff.palindrome.document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.annotation.NonEditable;
import com.drofff.palindrome.repository.UserRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Document
public class Driver implements Entity {

	@Id
	private String id;

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	private String middleName;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "Licence number should be provided")
	private String licenceNumber;

	@NonEditable
	private String photoUri;

	@NonEditable
	private Set<String> ownedCarIds = new HashSet<>();

	@NonEditable
	@FromRepository(UserRepository.class)
	private String userId;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	public Set<String> getOwnedCarIds() {
		return ownedCarIds;
	}

	public void setOwnedCarIds(Set<String> ownedCarIds) {
		this.ownedCarIds = ownedCarIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Driver) {
			Driver driver = (Driver) obj;
			return id.equals(driver.id);
		}
		return super.equals(obj);
	}

}

package com.drofff.palindrome.dto;

import com.drofff.palindrome.enums.Role;

public class CreateUserDto {

	private String username;

	private Role role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}

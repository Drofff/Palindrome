package com.drofff.palindrome.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

	ADMIN, POLICE, DRIVER;

	@Override
	public String getAuthority() {
		return name();
	}

}

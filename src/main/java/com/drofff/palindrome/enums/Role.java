package com.drofff.palindrome.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

	ADMIN, POLICE, DRIVER, APP {

		@Override
		public String toString() {
			return "user-app";
		}

	};

	@Override
	public String getAuthority() {
		return name();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
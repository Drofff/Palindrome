package com.drofff.palindrome.dto;

public class ChangePasswordDto {

	private String password;

	private String newPassword;

	private boolean byMail;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public boolean isByMail() {
		return byMail;
	}

	public void setByMail(boolean byMail) {
		this.byMail = byMail;
	}

}

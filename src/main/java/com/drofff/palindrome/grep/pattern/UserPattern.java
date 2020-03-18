package com.drofff.palindrome.grep.pattern;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.annotation.TargetField;
import com.drofff.palindrome.dto.UsersUserDto;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.grep.strategy.StartsWithComparisonStrategy;

@Pattern(forClass = UsersUserDto.class)
public class UserPattern {

	@Strategy(StartsWithComparisonStrategy.class)
	private String username;

	private Role role;

	@TargetField(name = "enabled")
	private Boolean active;

	private Boolean blocked;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

}

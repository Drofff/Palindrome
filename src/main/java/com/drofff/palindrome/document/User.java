package com.drofff.palindrome.document;

import com.drofff.palindrome.enums.Role;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Document
public class User implements UserDetails {

	@Id
	private String id;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String username;

	@NotBlank(message = "Password is required")
	@Length(min = 8, message = "Password should be at least 8 characters long")
	private String password;

	@NotNull(message = "User role is required")
	private Role role;

	private boolean active;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(role);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

	public boolean isDriver() {
		return role == Role.DRIVER;
	}

	public boolean isNotAdmin() {
		return !isAdmin();
	}

	public boolean isAdmin() {
		return role == Role.ADMIN;
	}

	public boolean isPolice() {
		return role == Role.POLICE;
	}

	public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken() {
		return new UsernamePasswordAuthenticationToken(this, password);
	}

}

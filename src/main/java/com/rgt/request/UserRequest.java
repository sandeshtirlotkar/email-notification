package com.rgt.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequest {
	
	@Email
	@NotBlank
	private String emailId;
	@NotBlank
	private String password;
	
	private String username;
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}

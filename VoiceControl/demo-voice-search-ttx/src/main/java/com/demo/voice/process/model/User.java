package com.demo.voice.process.model;

public class User {
	private String id;
	private String fullName;
	private String userId;
	private boolean passSelfServiceRegistration;
	private boolean accountStatus;
	private String email;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isPassSelfServiceRegistration() {
		return passSelfServiceRegistration;
	}
	public void setPassSelfServiceRegistration(boolean passSelfServiceRegistration) {
		this.passSelfServiceRegistration = passSelfServiceRegistration;
	}
	public boolean isAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(boolean accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}

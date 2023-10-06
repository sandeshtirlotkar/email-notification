package com.rgt.bean;

public class RegisterUserBean {
	
	private String username;
	private String address;
	private String city;
	private String pincode;
	private String dob;
	
	
	
	public RegisterUserBean(String username, String address, String city, String pincode, String dob) {
		this.username = username;
		this.address = address;
		this.city = city;
		this.pincode = pincode;
		this.dob = dob;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	

}

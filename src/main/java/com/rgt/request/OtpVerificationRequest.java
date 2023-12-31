package com.rgt.request;

import com.rgt.enums.OtpContext;

public class OtpVerificationRequest {
	
	private String emailId;
	private Integer otp;
	private OtpContext otpContext;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Integer getOtp() {
		return otp;
	}
	public void setOtp(Integer otp) {
		this.otp = otp;
	}
	public OtpContext getOtpContext() {
		return otpContext;
	}
	public void setOtpContext(OtpContext otpContext) {
		this.otpContext = otpContext;
	}
	
	

}

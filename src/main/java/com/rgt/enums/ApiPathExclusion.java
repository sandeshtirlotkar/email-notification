package com.rgt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiPathExclusion {
	ACTUATOR("/actuator/**"), LOGIN("/login"), SIGN_UP("/sign-up"),
    OTP_VERIFICATION("/verify-otp"), REFRESH_TOKEN("/refresh-token");
	
	private String path;


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}

package com.rgt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.rgt")
public class OneTimePasswordConfigurationProperties {
	
	private OTP otp = new OTP();
	
	@Data
	public class OTP {
		private Integer expirationMinutes;

		public Integer getExpirationMinutes() {
			return expirationMinutes;
		}

		public void setExpirationMinutes(Integer expirationMinutes) {
			this.expirationMinutes = expirationMinutes;
		}

		
		
	}

	public OTP getOtp() {
		return otp;
	}

	public void setOtp(OTP otp) {
		this.otp = otp;
	}

	
	
	

}


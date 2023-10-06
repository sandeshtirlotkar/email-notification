package com.rgt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties
public class JwtConfigurationProperties {
	
//	@Value("${jwt.secret-key}")
//	private String jwtSecretKey;
	
	private JWT jwt = new JWT();
	
	
	
	public JWT getJwt() {
		return jwt;
	}



	public void setJwt(JWT jwt) {
		this.jwt = jwt;
	}



	public class JWT{
		private String secretKey;

		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}
		
		
		
		
	}

}

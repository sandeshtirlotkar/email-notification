package com.rgt.service;

import org.springframework.http.ResponseEntity;

import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.TokenRefreshRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.UserLoginResponse;

public interface UserService {

	ResponseEntity<?> createAccount(UserRequest userRequest);

	ResponseEntity<UserLoginResponse> verifyOtp(OtpVerificationRequest otpVerificationRequest);

	ResponseEntity<?> userLogin(UserRequest userRequest);

	ResponseEntity<?> tokenRefresh(TokenRefreshRequest tokenRefreshRequest);

	ResponseEntity<?> getDetails(String emailId);

	ResponseEntity<?> deleteUserAccount(String extractEmail);

}

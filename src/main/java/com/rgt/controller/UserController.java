package com.rgt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.TokenRefreshRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.UserLoginResponse;
import com.rgt.service.UserService;
import com.rgt.utils.JwtUtils;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping(value = "/sign-up")
	public ResponseEntity<?> userCreation(@RequestBody UserRequest userRequest) {
		
		return userService.createAccount(userRequest);
		
	}
	
	@PostMapping(value = "/verify-otp")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<UserLoginResponse> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest){
		return userService.verifyOtp(otpVerificationRequest);
	}
	
	@PostMapping(value = "/login")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> userLogin(@RequestBody UserRequest userRequest){
		return userService.userLogin(userRequest);
		
	}
	
	
	@PostMapping(value = "/refresh-token")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> tokenRefresh(@RequestBody TokenRefreshRequest tokenRefreshRequest){
		return userService.tokenRefresh(tokenRefreshRequest);
		
	}
	
	@GetMapping(value = "/user")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> loggedInUserDetails(@RequestHeader(required = true, name = "Authorization") String header){
		return userService.getDetails(jwtUtils.extractEmail(header));
		
	}
	
	
	@DeleteMapping(value = "/deleteUser")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> deleteUserAccount(@RequestHeader(required = true, name = "Authorization") String header){
		return userService.deleteUserAccount(jwtUtils.extractEmail(header));
		
	}
	
	

}

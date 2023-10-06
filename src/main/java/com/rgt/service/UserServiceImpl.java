package com.rgt.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.cache.LoadingCache;
import com.rgt.entity.RegisterUserEntity;
import com.rgt.enums.OtpContext;
import com.rgt.repository.RegisterUserRepository;
import com.rgt.request.OtpVerificationRequest;
import com.rgt.request.TokenRefreshRequest;
import com.rgt.request.UserRequest;
import com.rgt.response.UserLoginResponse;
import com.rgt.utils.CommonUtility;
import com.rgt.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService{
	
	private static Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private RegisterUserRepository registerUserRepository;
	
	@Autowired()
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LoadingCache<String, Integer> oneTimePasswordCache;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public ResponseEntity<?> createAccount(UserRequest userRequest) {
		
		if(registerUserRepository.existsByEmailId(userRequest.getEmailId())) 
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User account already exists for provided email");
		
		RegisterUserEntity registerUserEntity = new RegisterUserEntity();
		registerUserEntity.setEmailId(userRequest.getEmailId());
		registerUserEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		registerUserEntity.setEmailVerified(true);
		registerUserEntity.setActive(true);
		registerUserEntity.setUsername(userRequest.getUsername());
		registerUserEntity.setCreatedOn(new Date());
		
		RegisterUserEntity entity = registerUserRepository.save(registerUserEntity);
		
		sendOtp(entity , "Verify your account");
		
		
		
		
		return ResponseEntity.ok(getOtpSendMessage());
	}

	private Map<String, String> getOtpSendMessage() {
		 HashMap<String , String>  response= new HashMap<String , String>();
		 
		 response.put("message", "OTP sent successfully sent to your registered email-address. verify using /rgt/verify-otp endpoint");
		
		return response;
	}

	private void sendOtp(RegisterUserEntity entity, String subject) {
		
		try {
			if(oneTimePasswordCache.get(entity.getEmailId()) !=null) 
				oneTimePasswordCache.invalidate(entity.getEmailId());
			
		}catch(ExecutionException e) {
			logger.error("Failed to fetch OTP cache: {}" ,e);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
			
		}
		
		int otp = new Random().ints(1, 100000 , 999999).sum();
		oneTimePasswordCache.put(entity.getEmailId(), otp);
		
		
		CompletableFuture.supplyAsync(() -> {
            mailService.sendEmail(entity.getEmailId(), subject, "OTP: " + otp);
            return HttpStatus.OK;
        });
		
	}

	@Override
	public ResponseEntity<UserLoginResponse> verifyOtp(OtpVerificationRequest otpVerificationRequest) {
		
		RegisterUserEntity registerUserEntity = registerUserRepository.findByEmailId(otpVerificationRequest.getEmailId());
				if(registerUserEntity !=null) {
					
					Integer storedOneTimePassword = null;
			        try {
			            storedOneTimePassword = oneTimePasswordCache.get(registerUserEntity.getEmailId());
			        } catch (ExecutionException e) {
			            logger.error("FAILED TO FETCH PAIR FROM OTP CACHE: {}", e);
			            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
			        }

			        if (storedOneTimePassword != null) {
			            if (storedOneTimePassword.equals(otpVerificationRequest.getOtp())) {
			                if (otpVerificationRequest.getOtpContext().equals(OtpContext.SIGN_UP)) {
			                	registerUserEntity.setEmailVerified(true);
			                	registerUserEntity = registerUserRepository.save(registerUserEntity);
			                    return ResponseEntity
			                            .ok(UserLoginResponse.builder().accessToken(jwtUtils.generateAccessToken(registerUserEntity))
			                                    .refreshToken(jwtUtils.generateRefreshToken(registerUserEntity)).build());
			                }
			                if (otpVerificationRequest.getOtpContext().equals(OtpContext.ACCOUNT_DELETION)) {
			                	registerUserEntity.setActive(false);
			                	registerUserEntity = registerUserRepository.save(registerUserEntity);
			                    return ResponseEntity.ok().build();
			                }
			            }
			            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			        } else
			            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			    }else {
					
			    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email Id");
				}
		
				
		
		
		
		
	}

	@Override
	public ResponseEntity<?> userLogin(UserRequest userRequest) {
		
		RegisterUserEntity registerUserEntity = registerUserRepository.findByEmailId(userRequest.getEmailId());
		if(registerUserEntity !=null) {
			
			if(!passwordEncoder.matches(userRequest.getPassword(), registerUserEntity.getPassword()))
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Login Creadentials");
			
			if(!registerUserEntity.isActive())
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account Not Actve");
			
			sendOtp(registerUserEntity, "Request to log in to your account");
			
			return ResponseEntity.ok(getOtpSendMessage());
			
		}else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email Id");
		}
		
		
	}

	//Refresh Token Service
	@Override
	public ResponseEntity<?> tokenRefresh(TokenRefreshRequest tokenRefreshRequest) {
		if(jwtUtils.isTokenExpired(tokenRefreshRequest.getRefreshToken()))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token has expired");
		
		RegisterUserEntity registerUserEntity = registerUserRepository.findByEmailId(jwtUtils.extractEmail(tokenRefreshRequest.getRefreshToken()));
		if(registerUserEntity !=null) {
			
			return ResponseEntity.ok(UserLoginResponse.builder().refreshToken(tokenRefreshRequest.getRefreshToken())
					.accessToken(jwtUtils.generateAccessToken(registerUserEntity)).build());
			
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getDetails(String emailId) {
		 RegisterUserEntity registerUserEntity = registerUserRepository.findByEmailId(emailId);
	        final var response = new HashMap<String, String>();
	        response.put("email_id", registerUserEntity.getEmailId());
	        response.put("created_at",CommonUtility.getCurrentDateDDMMYYYY( registerUserEntity.getCreatedOn()));
	        return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<?> deleteUserAccount(String emailId) {

		RegisterUserEntity registerUserEntity = registerUserRepository.findByEmailId(emailId);
		if(registerUserEntity !=null) {
			sendOtp(registerUserEntity, "2FA : Confirm Account Deletion");
			return ResponseEntity.ok(getOtpSendMessage());
		}else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Email Id");
		}
		
	}

}

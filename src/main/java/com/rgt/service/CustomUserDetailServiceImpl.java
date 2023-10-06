package com.rgt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rgt.entity.RegisterUserEntity;
import com.rgt.repository.RegisterUserRepository;

@Configuration
@Service
public class CustomUserDetailServiceImpl  implements UserDetailsService {
	
	@Autowired
	private RegisterUserRepository registerUserRepository;

//	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		return convert(registerUserRepository.findByEmailId(emailId));
	}
	
	//convert 
		public User convert(RegisterUserEntity registerUserEntity) {
	        return new User(registerUserEntity.getEmailId(), registerUserEntity.getPassword(), List.of() );
	    }
		
		
		 
}

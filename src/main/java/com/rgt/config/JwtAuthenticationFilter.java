package com.rgt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rgt.service.CustomUserDetailServiceImpl;
import com.rgt.utils.JwtUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	 private JwtUtils jwtUtils;
	    private  CustomUserDetailServiceImpl customUserDetialService;

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {

	       String authorizationHeader = request.getHeader("Authorization");

	        if (authorizationHeader != null) {
	            if (authorizationHeader.startsWith("Bearer ")) {
	                 String token = authorizationHeader.substring(7);
	               String emailId = jwtUtils.extractEmail(token);

	                if (emailId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	                    UserDetails userDetails = customUserDetialService.loadUserByUsername(emailId);

	                    if (jwtUtils.validateToken(token, userDetails)) {

	                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
	                                userDetails, null, userDetails.getAuthorities());
	                        usernamePasswordAuthenticationToken
	                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

	                    }
	                }
	            }
	        }
	        filterChain.doFilter(request, response);
	    }

}

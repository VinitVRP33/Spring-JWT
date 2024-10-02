package com.jwt.controller;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.entities.LoginInfo;
import com.jwt.token.JwtService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class JwtController {
	
	private final AuthenticationManager manager;
	private final JwtService service;
	private final UserDetailsService userDetailsService;

	@GetMapping("/home")
	public String forAll() {
		
		return "HOME";
	}
	
	@GetMapping("/user/home")
	public String forUser() {
		
		return "USER/HOME";
	}
	
	@GetMapping("/admin/home")
	public String forAdmin() {
		
		return "ADMIN/HOME";
	}
	
	@PostMapping("/authenticate")
	public String getToken(@RequestBody LoginInfo info) {
		
		Authentication authenticate = manager.authenticate(new UsernamePasswordAuthenticationToken(
				info.username(), info.password()));
		
		if(authenticate.isAuthenticated()) {
			
			return service.generateToken(userDetailsService.loadUserByUsername(info.username()));
		}
		else {
			throw new UsernameNotFoundException("WRONG CREDENTIALS SIR !!!");
		}
	}
}

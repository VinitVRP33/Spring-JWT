package com.jwt.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.jwt.entities.Authority;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GrantedAuthoritiesImpl implements GrantedAuthority{
	
	private final Authority authority;

	@Override
	public String getAuthority() {
		
		return authority.getName();
	}

}

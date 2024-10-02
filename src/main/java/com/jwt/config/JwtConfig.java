package com.jwt.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.filters.CustomJwtFilter;

@Configuration
public class JwtConfig {
	
	@Autowired
	private UserDetailsService service;
	@Autowired
	private CustomJwtFilter filter;
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(x->x.disable())
				.authorizeHttpRequests(x->x
				.requestMatchers("/home","/authenticate").permitAll()
				.requestMatchers(HttpMethod.GET,"/user/**").hasAnyAuthority("ADMIN","USER")
				.requestMatchers(HttpMethod.GET,"/admin/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				) 
				.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationProvider provider() {
		
		DaoAuthenticationProvider dao= new DaoAuthenticationProvider();
		dao.setUserDetailsService(service);
		dao.setPasswordEncoder(passwordEncoder());
		
		return dao;
			
	}
	
	@Bean
	public AuthenticationManager manager() {
		
		return new ProviderManager(provider());
	}
	
	 
}

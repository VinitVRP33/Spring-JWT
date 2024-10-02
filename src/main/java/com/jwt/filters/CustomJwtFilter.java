package com.jwt.filters;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.token.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomJwtFilter extends OncePerRequestFilter{

	private final JwtService service;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
			String headerAuthorization=request.getHeader("Authorization");
			
			if(headerAuthorization==null || !headerAuthorization.startsWith("Bearer ")) {
				
				filterChain.doFilter(request, response);
				return ;
			}
			
			String headerToken=headerAuthorization.substring(7);
			
			String username=service.getUserName(headerToken);
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
		
			
			if(userDetails!=null && service.isTokenValid(headerToken)) {
				UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(
						username,
						userDetails.getPassword(),
						userDetails.getAuthorities()
					);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//Basically this line helps us to add the details of the user who is requesting (host etc)
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
	}

}

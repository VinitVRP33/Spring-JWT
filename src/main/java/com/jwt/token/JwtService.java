package com.jwt.token;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "F0FF937BFA31B374CCA6FC1784506BEC92C62A6FD2714"
			+ "CEDBE07467C5B1980AD2704FC94158773547AD5694009D41716ED35659639A87C1712AD29818D90E0E8";

	private static final long VALAIDITY = TimeUnit.MINUTES.toMillis(30);

	public String generateToken(UserDetails details) {

		Map<String, String> claims = new HashMap<>();
		claims.put("iss", "https://com.vineet@gmail.com");

		return Jwts.builder()
				.claims(claims)
				.subject(details.getUsername())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(VALAIDITY)))
				.signWith(generateSecretKeyUsingSecret(SECRET))
				.compact();//to convert it into JSON

	}

	public SecretKey generateSecretKeyUsingSecret(String secret) {

		byte[] decodedKey = Base64.getDecoder().decode(secret);
		return Keys.hmacShaKeyFor(decodedKey);
		
	}
	
	public Claims getClaims(String jwt) {
		
		return Jwts.parser()
				.verifyWith(generateSecretKeyUsingSecret(SECRET))
				.build()
				.parseSignedClaims(jwt)
				.getPayload();
	
	}
	
	public String getUserName(String jwt) {
		
		return getClaims(jwt).getSubject();
	}
	
	public boolean isTokenValid(String jwt) {
		
		return getClaims(jwt).getExpiration()
				.after(Date.from(Instant.now()));
		
		//This means the expiration time is after the current time 
	}
}

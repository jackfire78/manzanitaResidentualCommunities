package com.manzanita.spring.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.manzanita.spring.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//provides the methods needed to create, parse, and validate JSON Web Token
@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${manzanita.app.jwtSecret}")
	private String jwtSecret;

	@Value("${manzanita.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	//method to generate a JSON web token that will be issued to users accessing the site
	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		//build the JSON web token with username, date given, expiration, and signature
		return Jwts.builder().setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact(); //password is hidden
	}

	//method to retrieve username from token
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	//method to validate JSON web token.
	//try catch will either return true or error with correct caught exception message
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {	//wrong signature
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {		//wrong token
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) { 	 //expired token
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) { 	  //unsupported token
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {    //empty JSON web token
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}

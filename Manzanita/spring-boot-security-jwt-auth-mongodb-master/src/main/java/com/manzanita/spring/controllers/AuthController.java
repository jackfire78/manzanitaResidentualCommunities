package com.manzanita.spring.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manzanita.spring.models.ERole;
import com.manzanita.spring.models.Role;
import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.RoleRepository;
import com.manzanita.spring.repository.UserRepository;
import com.manzanita.spring.request.LoginRequest;
import com.manzanita.spring.request.SignupRequest;
import com.manzanita.spring.response.JwtResponse;
import com.manzanita.spring.response.MessageResponse;
import com.manzanita.spring.security.jwt.JwtUtils;
import com.manzanita.spring.services.UserDetailsImpl;

//controller used to make sure authorization is completed before accessing any of the API mappings
//"/signin" used to logging in and "signup" used for registering a new account
//"signin" will provide you with the bearer token to proceed on within the rest of the site. Otherwise back to login or previous page you will go
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")   //sign into an existing account post mapping
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		//authenticate username and password to see if they match in database to a certain account
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		//set authentication (include jwt token)
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		//return correct response entity alongside user information, token, and HTTP status code
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")  //register a new account post mapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		//if username exists in database then return error saying username is taken
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		//if email exists in repo then return error saying email is taken
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
							 encoder.encode(signUpRequest.getPassword()),
							 signUpRequest.getEmail(),
							 signUpRequest.getPhoneNumber(),
							 signUpRequest.getUnitNumber(),
							 signUpRequest.getRole());

		Set<Role> roles = new HashSet<>();

		if (signUpRequest.getRole().isEmpty()) { //if database has any problems then return exception
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
				switch (signUpRequest.getRole()) {
				case "admin": //set account role to admin
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod": //set account role to moderator
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default: //if not previous two then set account to user role
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			
		}
		//set role for user and save user to database
		user.setRoles(roles);
		userRepository.save(user);
		//return register success message
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}

package com.manzanita.spring.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.RoleRepository;
import com.manzanita.spring.repository.UserRepository;
import com.manzanita.spring.security.jwt.JwtUtils;

//controller used for admin services
//"/signin" used to logging in and "signup" used for registering a new account
//"signin" will provide you with the bearer token to proceed on within the rest of the site. Otherwise back to login or previous page you will go
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/adminServices")
public class AdminController {
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

	//method used to retrieve logged in personal profile information
	@GetMapping("/getAccountInfo/{id}")
	public ResponseEntity<?> getAccountInfo(@PathVariable("id")String id) {
		try{
			//create a variable to store the user retrieved matching id passed
			Optional<User> getUser = userRepository.findById(id);

			//if the user is empty then return no content was found status
			if(getUser.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getUser, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//method used to retrieve all user role accounts
	@GetMapping("/allUsers")
	public ResponseEntity<?> getAllUsers() {		
		try{
			//create a List variable to store all user role accounts
			List<User> userList = new ArrayList<User>();
			userRepository.findByRole("user").forEach(userList::add);
			if (userList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(userList, HttpStatus.OK);	
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//method used to retrieve all mod and admin role accounts
	@GetMapping("/allWorkers")
	public ResponseEntity<?> getAllWorkers() {		
		try{
			//create a List variable to store all mod and admin role accounts
			List<User> workerList = new ArrayList<User>();
			userRepository.findByRole("mod").forEach(workerList::add);
			userRepository.findByRole("admin").forEach(workerList::add);

			if (workerList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(workerList, HttpStatus.OK);	
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//method that deletes chosen account
	@DeleteMapping("/deleteAccount/{id}")
	public ResponseEntity<HttpStatus> deleteAccountb(@PathVariable("id") String id) {
		try {
			//delete the account that matches given id
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}

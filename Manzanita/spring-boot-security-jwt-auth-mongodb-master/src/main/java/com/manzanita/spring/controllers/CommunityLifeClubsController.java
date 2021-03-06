package com.manzanita.spring.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.EventsAttended;
import com.manzanita.spring.models.JoinedLifeClubs;
import com.manzanita.spring.models.LifeClubs;
import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.ClubMembers;
import com.manzanita.spring.repository.CommunityLifeClubsRepository;
import com.manzanita.spring.repository.EventAttendees;
import com.manzanita.spring.repository.UserRepository;

//testing controller used to display correct message depending on what is being accessed.
//all can be accessed by anyone while other 3 require one correct role
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lifeClub")
public class CommunityLifeClubsController {

	@Autowired
	CommunityLifeClubsRepository lifeClubsRepo;
	@Autowired
	ClubMembers clubMembers;
	@Autowired
	UserRepository userRepo;
	
	//method to create a new community life club from parameters passed.
	@PostMapping("/createLifeClub")
	public ResponseEntity<LifeClubs> createCommunityLifeClub(@RequestBody LifeClubs lifeClub){
		try{
			//save the new community life club along with all given information to database
			LifeClubs newLifeClub = lifeClubsRepo.save(new LifeClubs(
					lifeClub.getClubName(),
					lifeClub.getClubDescription()));
			return new ResponseEntity<>(newLifeClub, HttpStatus.CREATED);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method used to retrieve all Community life club
	@GetMapping("/allComLifeClubs")
	public ResponseEntity<List<LifeClubs>> getAllLifeClubs(@RequestParam(required = false)  String clubName) {
		try{
			//create a List variable to store all community life clubs retrieved
			List<LifeClubs> lifeClubsList = new ArrayList<LifeClubs>();
			
			if(clubName == null ) {  //if nothing was passed then return all
				lifeClubsRepo.findAll().forEach(lifeClubsList::add);
			}else { //if life clubs with given name are found then return all matching
				lifeClubsRepo.findByclubName(clubName).forEach(lifeClubsList::add);
			}
		    if (lifeClubsList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(lifeClubsList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method used to retrieve a single community life club
	@GetMapping("/getComLifeClub/{id}")
	public ResponseEntity<?> getComLifeClub(@PathVariable("id")String id) {
		try{
			//create a variable to store the club retrieved matching id passed
			Optional<LifeClubs> getClub = lifeClubsRepo.findById(id);

			//if the life club is empty then return no content was found status
			if(getClub.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getClub, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method for joining a Community life club
	@PutMapping("/joinCommunityLifeClub/{id}")
	public ResponseEntity<LifeClubs> joinCommunityLifeClub(@PathVariable("id")String id, @RequestBody User user) {
//		System.out.println(id);
//		System.out.println(user.getId());

		Optional<LifeClubs> communityLifeClubData = lifeClubsRepo.findById(id);
		//Optional<User> userData = userRepo.findById(user.getId());

		//if user not null, create new object and save it to club members database
		if (user.getId() != null) {
			JoinedLifeClubs joinedClub = new JoinedLifeClubs(id, user.getId(), communityLifeClubData.get().getClubName() );
			System.out.println(joinedClub.toString());
			clubMembers.save(joinedClub);
			return new ResponseEntity<>( HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method used to retrieve all Community life clubs the current user is attending
	@GetMapping("/getMyComLifeClub/{id}")
	public ResponseEntity<List<LifeClubs>> myCommunityLifeClub(@PathVariable("id")String id) {
		try{
			//create a List variable to store all community life clubs retrieved
			List<LifeClubs> communityLifeClubsList = new ArrayList<LifeClubs>();
			
			clubMembers.findByuserId(id).forEach(communityLifeClubsList::add);
			
		    if (communityLifeClubsList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(communityLifeClubsList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	//method that edits a community life club
	@PutMapping("/editComLifeClub/{id}")
	public ResponseEntity<LifeClubs> editComLifeClub(@PathVariable("id")String id, @RequestBody LifeClubs lifeClub) {
		Optional<LifeClubs> comLifeClubData = lifeClubsRepo.findById(id);
		//if club was found update that club with the new information passed
		if (comLifeClubData.isPresent()) {
			LifeClubs updatedComLifeClub = comLifeClubData.get();
			updatedComLifeClub.setClubName(lifeClub.getClubName());
			updatedComLifeClub.setClubDescription(lifeClub.getClubDescription());
			
			return new ResponseEntity<>(lifeClubsRepo.save(updatedComLifeClub), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	//method that deletes a Community life club previously attended
	@DeleteMapping("/leaveCommunityLifeClub/{id}")
	public ResponseEntity<HttpStatus> leaveCommunityLifeClub(@PathVariable("id") String id) {
		try {
			//delete the joined club that matches given id
			clubMembers.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method that deletes a community life club
	@DeleteMapping("/deleteComLifeClub/{id}")
	public ResponseEntity<HttpStatus> deleteComLifeClub(@PathVariable("id") String id) {
		try {
			//delete the life club that matches given id
			lifeClubsRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}

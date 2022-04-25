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
import com.manzanita.spring.models.LifeActivities;
import com.manzanita.spring.models.LifeActivitiesAttended;
import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.ActivityAttendees;
import com.manzanita.spring.repository.CommunityLifeActivitesRepository;
import com.manzanita.spring.repository.EventAttendees;
import com.manzanita.spring.repository.UserRepository;

//testing controller used to display correct message depending on what is being accessed.
//all can be accessed by anyone while other 3 require one correct role
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lifeActivity")
public class CommunityLifeActivitiesController {

	@Autowired
	CommunityLifeActivitesRepository lifeActivitiesRepo;
	@Autowired
	ActivityAttendees activityAttendees;
	@Autowired
	UserRepository userRepo;
	
	//method to create a new community life activity from parameters passed.
	@PostMapping("/createLifeActivity")
	public ResponseEntity<LifeActivities> createCommunityLifeActivity(@RequestBody LifeActivities lifeActivity){
		try{
			//save the new community life activity along with all given information to database
			LifeActivities newLifeActivity = lifeActivitiesRepo.save(new LifeActivities(
					lifeActivity.getActivityName(),lifeActivity.getActivityDate(),
					lifeActivity.getActivityDescription(),lifeActivity.getActivityPrice()));
			return new ResponseEntity<>(newLifeActivity, HttpStatus.CREATED);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method used to retrieve all Community life activities
	@GetMapping("/allComLifeActivities")
	public ResponseEntity<List<LifeActivities>> getAllLifeActivities(@RequestParam(required = false)  String activityName) {
		try{
			//create a List variable to store all community life activities retrieved
			List<LifeActivities> lifeActivitiesList = new ArrayList<LifeActivities>();
			
			if(activityName == null ) {  //if nothing was passed then return all
				lifeActivitiesRepo.findAll().forEach(lifeActivitiesList::add);
			}else { //if life activities with given name are found then return all matching
				lifeActivitiesRepo.findByactivityName(activityName).forEach(lifeActivitiesList::add);
			}
		    if (lifeActivitiesList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(lifeActivitiesList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method used to retrieve a single community life activity
	@GetMapping("/getComLifeActivity/{id}")
	public ResponseEntity<?> getComLifeActivity(@PathVariable("id")String id) {
		try{
			//create a variable to store the event retrieved matching id passed
			Optional<LifeActivities> getEvent = lifeActivitiesRepo.findById(id);

			//if the life activity is empty then return no content was found status
			if(getEvent.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getEvent, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method for attending a Community life activity
	@PutMapping("/attendCommunityLifeActivity/{id}")
	public ResponseEntity<LifeActivities> attendCommunityLifeActivity(@PathVariable("id")String id, @RequestBody User user) {
//		System.out.println(id);
//		System.out.println(user.getId());

		Optional<LifeActivities> communityLifeActivityData = lifeActivitiesRepo.findById(id);
		//Optional<User> userData = userRepo.findById(user.getId());

		//if user not null, create new object and save it to attendee database
		if (user.getId() != null) {
			LifeActivitiesAttended attendedActivity = new LifeActivitiesAttended(id, user.getId(), communityLifeActivityData.get().getActivityName() );
			System.out.println(attendedActivity.getActivityName());
			activityAttendees.save(attendedActivity);
			return new ResponseEntity<>( HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method used to retrieve all life activities the current user is attending
	@GetMapping("/myCommunityLifeActivities/{id}")
	public ResponseEntity<List<LifeActivities>> myCommunityLifeActivities(@PathVariable("id")String id) {
		try{
			//create a List variable to store all community life activities retrieved
			List<LifeActivities> communityLifeActivityList = new ArrayList<LifeActivities>();
			
			activityAttendees.findByuserId(id).forEach(communityLifeActivityList::add);
			
		    if (communityLifeActivityList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(communityLifeActivityList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//method that edits a community life activity
	@PutMapping("/editComLifeActivity/{id}")
	public ResponseEntity<LifeActivities> editComLifeActivity(@PathVariable("id")String id, @RequestBody LifeActivities lifeActivity) {
		Optional<LifeActivities> comLifeActivityData = lifeActivitiesRepo.findById(id);
		//if event was found update that event with the new information passed
		if (comLifeActivityData.isPresent()) {
			LifeActivities updatedComLifeActivity = comLifeActivityData.get();
			updatedComLifeActivity.setActivityName(lifeActivity.getActivityName());
			updatedComLifeActivity.setActivityDate(lifeActivity.getActivityDate());
			updatedComLifeActivity.setActivityDescription(lifeActivity.getActivityDescription());
			updatedComLifeActivity.setActivityPrice(lifeActivity.getActivityPrice());
			
			return new ResponseEntity<>(lifeActivitiesRepo.save(updatedComLifeActivity), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method that deletes a Community life activity previously attended
	@DeleteMapping("/leaveCommunityLifeActivity/{id}")
	public ResponseEntity<HttpStatus> leaveCommunityLifeActivity(@PathVariable("id") String id) {
		try {
			//delete the attended activity that matches given id
			activityAttendees.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method that deletes a community life activity
	@DeleteMapping("/deleteComLifeActivity/{id}")
	public ResponseEntity<HttpStatus> deleteComLifeActivity(@PathVariable("id") String id) {
		try {
			//delete the life activity that matches given id
			lifeActivitiesRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}

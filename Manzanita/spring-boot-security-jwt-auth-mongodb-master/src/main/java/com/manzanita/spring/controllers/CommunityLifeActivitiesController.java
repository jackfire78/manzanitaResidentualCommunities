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

import com.manzanita.spring.models.LifeActivities;
import com.manzanita.spring.repository.LifeActivitiesRepository;

//testing controller used to display correct message depending on what is being accessed.
//all can be accessed by anyone while other 3 require one correct role
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lifeActivity")
public class CommunityLifeActivitiesController {

	@Autowired
	LifeActivitiesRepository lifeActivitiesRepo;
	
	//method to create a new community life activity from parameters passed.
	@PostMapping("/createLifeActivity")
	public ResponseEntity<LifeActivities> createCommunityEvent(@RequestBody LifeActivities lifeActivity){
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

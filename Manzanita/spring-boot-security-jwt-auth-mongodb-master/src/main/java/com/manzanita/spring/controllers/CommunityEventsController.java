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
import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.CommunityEventsRepository;
import com.manzanita.spring.repository.EventAttendees;
import com.manzanita.spring.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/communityEvents")
public class CommunityEventsController {
	
	@Autowired
	CommunityEventsRepository communityEventsRepo;
	@Autowired
	EventAttendees eventAttendees;
	@Autowired
	UserRepository userRepo;
	
	//method to create a new Community Event from parameters passed.
	@PostMapping("/createCommunityEvent")
	public ResponseEntity<Events> createCommunityEvent(@RequestBody Events communityEvent){
		try{
			//save the new community Event along with all given information to database
			Events newCommunityEvent = communityEventsRepo.save(new Events(
					communityEvent.getEventName(),communityEvent.getEventDescription(),
					communityEvent.getEventPicture(),communityEvent.getEventPrice(),
					communityEvent.getEventDate(),communityEvent.getEventPresenters()));
			return new ResponseEntity<>(newCommunityEvent, HttpStatus.CREATED);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method used to retrieve all Community Events
	@GetMapping("/allCommunityEvents")
	public ResponseEntity<List<Events>> getAllCommunityEvents(@RequestParam(required = false)  String eventName) {
		try{
			//create a List variable to store all community events retrieved
			List<Events> communityEventList = new ArrayList<Events>();
			
			if(eventName == null ) {  //if nothing was passed then return all
				communityEventsRepo.findAll().forEach(communityEventList::add);
			}else { //if workOrders with given username are found then return all matching
				communityEventsRepo.findByeventName(eventName).forEach(communityEventList::add);
			}
		    if (communityEventList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(communityEventList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method used to retrieve a single Community Event
	@GetMapping("/getCommunityEvent/{id}")
	public ResponseEntity<?> getCommunityEvent(@PathVariable("id")String id) {
		try{
			//create a variable to store the event retrieved matching id passed
			Optional<Events> getEvent = communityEventsRepo.findById(id);

			//if the event is empty then return no content was found status
			if(getEvent.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getEvent, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method for joining a Community Event
	@PutMapping("/joinCommunityEvent/{id}")
	public ResponseEntity<Events> joinCommunityEvent(@PathVariable("id")String id, @RequestBody User user) {
//		System.out.println(id);
//		System.out.println(user.getId());

		Optional<Events> communityEventData = communityEventsRepo.findById(id);
		//Optional<User> userData = userRepo.findById(user.getId());

		//if user not null, create new object and save it to attendee database
		if (user.getId() != null) {
			EventsAttended attendedEvent = new EventsAttended(id, user.getId(), communityEventData.get().getEventName() );
			System.out.println(attendedEvent.toString());
			eventAttendees.save(attendedEvent);
			return new ResponseEntity<>( HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method used to retrieve all Community Events the current user is attending
	@GetMapping("/myCommunityEvents/{id}")
	public ResponseEntity<List<Events>> myCommunityEvents(@PathVariable("id")String id) {
		try{
			//create a List variable to store all community events retrieved
			List<Events> communityEventList = new ArrayList<Events>();
			
			eventAttendees.findByuserId(id).forEach(communityEventList::add);
			
		    if (communityEventList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(communityEventList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	//method that edits a Community Event
	@PutMapping("/editCommunityEvent/{id}")
	public ResponseEntity<Events> editCommunityEvent(@PathVariable("id")String id, @RequestBody Events event) {
		Optional<Events> communityEventData = communityEventsRepo.findById(id);
		//if event was found update that event with the new information passed
		if (communityEventData.isPresent()) {
			Events updatedCommunityEvent = communityEventData.get();
			updatedCommunityEvent.setEventName(event.getEventName());
			updatedCommunityEvent.setEventDescription(event.getEventDescription());
			updatedCommunityEvent.setEventPicture(event.getEventPicture());
			updatedCommunityEvent.setEventPrice(event.getEventPrice());
			updatedCommunityEvent.setEventDate(event.getEventDate());
			updatedCommunityEvent.setEventPresenters(event.getEventPresenters());
			
			return new ResponseEntity<>(communityEventsRepo.save(updatedCommunityEvent), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method that deletes a Community Event previously attended
	@DeleteMapping("/leaveCommunityEvent/{id}")
	public ResponseEntity<HttpStatus> leaveCommunityEvent(@PathVariable("id") String id) {
		try {
			//delete the attended event that matches given id
			eventAttendees.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method that deletes a Community Event
	@DeleteMapping("/deleteCommunityEvent/{id}")
	public ResponseEntity<HttpStatus> deleteCommunityEvent(@PathVariable("id") String id) {
		try {
			//delete the event that matches given id
			communityEventsRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	

}

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
import com.manzanita.spring.models.LifeMovies;
import com.manzanita.spring.models.MoviesAttended;
import com.manzanita.spring.models.User;
import com.manzanita.spring.repository.CommunityLifeMoviesRepository;
import com.manzanita.spring.repository.MovieAttendees;
import com.manzanita.spring.repository.UserRepository;

//testing controller used to display correct message depending on what is being accessed.
//all can be accessed by anyone while other 3 require one correct role
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lifeMovie")
public class CommunityLifeMoviesController {

	@Autowired
	CommunityLifeMoviesRepository lifeMoviesRepo;
	@Autowired
	MovieAttendees movieAttendees;
	@Autowired
	UserRepository userRepo;
	
	//method to create a new community life movie from parameters passed.
	@PostMapping("/createLifeMovie")
	public ResponseEntity<LifeMovies> createCommunityLifeMovie(@RequestBody LifeMovies lifeMovie){
		try{
			//save the new community life movie along with all given information to database
			LifeMovies newLifeMovie = lifeMoviesRepo.save(new LifeMovies(
					lifeMovie.getMovieName(),
					lifeMovie.getMovieDate(),
					lifeMovie.getMovieDescription()));
			return new ResponseEntity<>(newLifeMovie, HttpStatus.CREATED);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method used to retrieve all Community life movies
	@GetMapping("/allComLifeMovies")
	public ResponseEntity<List<LifeMovies>> getAllLifeMovies(@RequestParam(required = false)  String movieName) {
		try{
			//create a List variable to store all community life movies retrieved
			List<LifeMovies> lifeMoviesList = new ArrayList<LifeMovies>();
			
			if(movieName == null ) {  //if nothing was passed then return all
				lifeMoviesRepo.findAll().forEach(lifeMoviesList::add);
			}else { //if life movies with given name are found then return all matching
				lifeMoviesRepo.findBymovieName(movieName).forEach(lifeMoviesList::add);
			}
		    if (lifeMoviesList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(lifeMoviesList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method used to retrieve a single community life movie
	@GetMapping("/getComLifeMovie/{id}")
	public ResponseEntity<?> getComLifeMovie(@PathVariable("id")String id) {
		try{
			//create a variable to store the movie retrieved matching id passed
			Optional<LifeMovies> getMovie = lifeMoviesRepo.findById(id);

			//if the life movie is empty then return no content was found status
			if(getMovie.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getMovie, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	//method for attending a Community life movie
	@PutMapping("/attendCommunityLifeMovie/{id}")
	public ResponseEntity<LifeMovies> attendCommunityLifeMovie(@PathVariable("id")String id, @RequestBody User user) {
//		System.out.println(id);
//		System.out.println(user.getId());

		Optional<LifeMovies> communityLifeMovieData = lifeMoviesRepo.findById(id);
		//Optional<User> userData = userRepo.findById(user.getId());

		//if user not null, create new object and save it to attendee database
		if (user.getId() != null) {
			MoviesAttended attendedMovie = new MoviesAttended(id, user.getId(), communityLifeMovieData.get().getMovieName() );
			System.out.println(attendedMovie.toString());
			movieAttendees.save(attendedMovie);
			return new ResponseEntity<>( HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method used to retrieve all Community life movies the current user is attending
	@GetMapping("/getMyComLifeMovies/{id}")
	public ResponseEntity<List<LifeMovies>> getMyComLifeMovies(@PathVariable("id")String id) {
		try{
			//create a List variable to store all community life movies retrieved
			List<LifeMovies> communityLifeMovieList = new ArrayList<LifeMovies>();
			
			movieAttendees.findByuserId(id).forEach(communityLifeMovieList::add);
			
		    if (communityLifeMovieList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(communityLifeMovieList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	//method that edits a community life movie
	@PutMapping("/editComLifeMovie/{id}")
	public ResponseEntity<LifeMovies> editComLifeMovie(@PathVariable("id")String id, @RequestBody LifeMovies lifeMovie) {
		Optional<LifeMovies> comLifMovieData = lifeMoviesRepo.findById(id);
		//if movie was found update that movie with the new information passed
		if (comLifMovieData.isPresent()) {
			LifeMovies updatedComLifeMovie = comLifMovieData.get();
			updatedComLifeMovie.setMovieName(lifeMovie.getMovieName());
			updatedComLifeMovie.setMovieDate(lifeMovie.getMovieDate());
			updatedComLifeMovie.setMovieDescription(lifeMovie.getMovieDescription());
			
			return new ResponseEntity<>(lifeMoviesRepo.save(updatedComLifeMovie), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method that deletes a Community life movie previously attended
	@DeleteMapping("/leaveCommunityLifeMovie/{id}")
	public ResponseEntity<HttpStatus> leaveCommunityLifeMovie(@PathVariable("id") String id) {
		try {
			//delete the attended event that matches given id
			movieAttendees.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method that deletes a community life movie
	@DeleteMapping("/deleteComLifeMovie/{id}")
	public ResponseEntity<HttpStatus> deleteComLifeMovie(@PathVariable("id") String id) {
		try {
			//delete the life movie that matches given id
			lifeMoviesRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}

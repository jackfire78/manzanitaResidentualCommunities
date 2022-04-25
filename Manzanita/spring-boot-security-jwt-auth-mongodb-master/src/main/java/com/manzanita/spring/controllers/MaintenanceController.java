package com.manzanita.spring.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.manzanita.spring.models.LifeMovies;
import com.manzanita.spring.models.WorkOrders;
import com.manzanita.spring.repository.MaintenanceRepository;

//testing controller used to display correct message depending on what is being accessed.
//all can be accessed by anyone while other 3 require one correct role
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

	@Autowired
	MaintenanceRepository maintenanceRepo;

	//method to create a new work order from parameter passed.
	@PostMapping("/createWorkOrder")
	public ResponseEntity<WorkOrders> createWorkOrder(@RequestBody WorkOrders workOrder){
		try{
			//save the new work order along with all given information to database
			WorkOrders newWorkOrder = maintenanceRepo.save(new WorkOrders(workOrder.getResidentName(),
					workOrder.getResidentUnit(), workOrder.getResidentPhoneNum(), workOrder.getIssue(),
					workOrder.getSeverity(), workOrder.getHomeEnterance(), workOrder.getStatus()));
			return new ResponseEntity<>(newWorkOrder, HttpStatus.CREATED);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//method used to retrieve all work orders
	@GetMapping("/allWorkOrders")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<WorkOrders>> getAllWorkOrders(@RequestParam(required = false)  String residentName) {
		try{
			//create a List variable to store all work orders retrieved
			List<WorkOrders> workOrderList = new ArrayList<WorkOrders>();
			
			if(residentName == null ) {  //if nothing was passed then return all
				maintenanceRepo.findAll().forEach(workOrderList::add);
			}else { //if workOrders with given username are found then return all matching
				maintenanceRepo.existsByresidentName(residentName).forEach(workOrderList::add);
			}
		    if (workOrderList.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		    }
			
			return new ResponseEntity<>(workOrderList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//method used to retrieve a single work order
	@GetMapping("/getWorkOrder/{id}")
	public ResponseEntity<?> getWorkOrder(@PathVariable("id")String id) {
		try{
			//create a variable to store the work order retrieved matching id passed
			Optional<WorkOrders> getWorkOrder = maintenanceRepo.findById(id);

			//if the work order is empty then return no content was found status
			if(getWorkOrder.equals(null)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}			
			
			return new ResponseEntity<>(getWorkOrder, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	//method used to retrieve all work orders or current logged in user
	@GetMapping("/myWorkOrders")
	public ResponseEntity<List<WorkOrders>> getMyWorkOrders() {
		try{
			//create a List variable to store all work orders retrieved
			List<WorkOrders> workOrderList = new ArrayList<WorkOrders>();
			//use information saved to contextHolder to retrieve username of current logged in user
			UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//if workOrders with given username are found then return all matching
			maintenanceRepo.existsByresidentName(userDetails.getUsername()).forEach(workOrderList::add);
			//if the list is empty then return no content was found status
			if(workOrderList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(workOrderList, HttpStatus.OK);
		}catch (Exception e) { //if exception caught then return null with HTTP error status
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	//method that edits a work order
	@PutMapping("/editWorkOrder/{id}")
	public ResponseEntity<WorkOrders> editWorkOrder(@PathVariable("id")String id, @RequestBody WorkOrders workOrder) {
		Optional<WorkOrders> workOrderData = maintenanceRepo.findById(id);

		if (workOrderData.isPresent()) {
			WorkOrders updatedWorkOrder = workOrderData.get();
			updatedWorkOrder.setResidentName(workOrder.getResidentName());
			updatedWorkOrder.setResidentUnit(workOrder.getResidentUnit());
			updatedWorkOrder.setResidentPhoneNum(workOrder.getResidentPhoneNum());
			updatedWorkOrder.setIssue(workOrder.getIssue());
			updatedWorkOrder.setSeverity(workOrder.getSeverity());
			updatedWorkOrder.setHomeEnterance(workOrder.getHomeEnterance());
			updatedWorkOrder.setStatus(workOrder.getStatus());
			
			return new ResponseEntity<>(maintenanceRepo.save(updatedWorkOrder), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//method that deletes a work order
	@DeleteMapping("/deleteWorkOrder/{id}")
	public ResponseEntity<HttpStatus> deleteWorkOrder(@PathVariable("id") String id) {
		try {
			maintenanceRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}

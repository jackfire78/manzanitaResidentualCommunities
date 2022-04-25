package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.EventsAttended;
//DAO that accesses monogoDB database and retrieves requested data from attending community events repo
public interface EventAttendees extends MongoRepository<EventsAttended, String> {
  List<Events> findByuserId(String userId);
  //boolean search to find if event already exists with given name
  Boolean existsByeventName(String eventName);
  //find specific event by Id for edit and delete

}
package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
//DAO that accesses monogoDB database and retrieves requested data from community events repo
public interface CommunityEventsRepository extends MongoRepository<Events, String> {
  List<Events> findByeventName(String eventName);
  //boolean search to find if event already exists with given name
  Boolean existsByeventName(String eventName);
  //find specific event by Id for edit and delete
  //Optional<Events> findById(String id);

}
package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.LifeActivities;
//DAO that accesses monogoDB database and retrieves requested data from community life activities repo
public interface CommunityLifeActivitesRepository extends MongoRepository<LifeActivities, String> {
  List<LifeActivities> findByactivityName(String activityName);
  //boolean search to find if life activity already exists with given name
  Boolean existsByactivityName(String activityName);
  //find specific life activity by Id for edit and delete
  //Optional<LifeActivities> findById(String id);

}
package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.LifeActivities;
import com.manzanita.spring.models.LifeActivitiesAttended;
//DAO that accesses monogoDB database and retrieves requested data from attending community events repo
public interface ActivityAttendees extends MongoRepository<LifeActivitiesAttended, String> {
  List<LifeActivities> findByuserId(String userId);
  //boolean search to find if activity already exists with given name
  Boolean existsByactivityName(String activityName);

}
package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.LifeActivities;
import com.manzanita.spring.models.LifeRestaurants;
//DAO that accesses monogoDB database and retrieves requested data from community life restaurants repo
public interface LifeRestaurantsRepository extends MongoRepository<LifeRestaurants, String> {
  List<LifeRestaurants> findByrestaurantName(String restaurantName);
  //boolean search to find if life restaurant already exists with given name
  Boolean existsByrestaurantName(String restaurantName);
  //find specific life restaurant by Id for edit and delete
  //Optional<LifeRestaurants> findById(String id);

}
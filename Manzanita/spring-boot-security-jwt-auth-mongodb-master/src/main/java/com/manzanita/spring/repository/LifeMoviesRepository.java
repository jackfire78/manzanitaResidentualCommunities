package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.LifeActivities;
import com.manzanita.spring.models.LifeMovies;
//DAO that accesses monogoDB database and retrieves requested data from community life movies repo
public interface LifeMoviesRepository extends MongoRepository<LifeMovies, String> {
  List<LifeMovies> findBymovieName(String movieName);
  //boolean search to find if life movie already exists with given name
  Boolean existsBymovieName(String movieName);
  //find specific life movie by Id for edit and delete
  //Optional<LifeMovies> findById(String id);

}
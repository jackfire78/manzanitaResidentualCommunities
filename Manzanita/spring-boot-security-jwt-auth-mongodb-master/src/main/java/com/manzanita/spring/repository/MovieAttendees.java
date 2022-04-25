package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.Events;
import com.manzanita.spring.models.EventsAttended;
import com.manzanita.spring.models.LifeMovies;
import com.manzanita.spring.models.MoviesAttended;
//DAO that accesses monogoDB database and retrieves requested data from attending community life movies repo
public interface MovieAttendees extends MongoRepository<MoviesAttended, String> {
  List<LifeMovies> findByuserId(String userId);
  //boolean search to find if life movie already exists with given name
  Boolean existsBymovieName(String movieName);
  //find specific life movie by Id for edit and delete

}
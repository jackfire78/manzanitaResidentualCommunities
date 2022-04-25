package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.LifeClubs;
//DAO that accesses monogoDB database and retrieves requested data from community life club repo
public interface CommunityLifeClubsRepository extends MongoRepository<LifeClubs, String> {
  List<LifeClubs> findByclubName(String clubName);
  //boolean search to find if life club already exists with given name
  Boolean existsByclubName(String clubName);
  //find specific life club by Id for edit and delete
  //Optional<LifeClubs> findById(String id);

}
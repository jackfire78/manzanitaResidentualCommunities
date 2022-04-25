package com.manzanita.spring.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.JoinedLifeClubs;
import com.manzanita.spring.models.LifeClubs;
//DAO that accesses monogoDB database and retrieves requested data from attending community life clubs repo
public interface ClubMembers extends MongoRepository<JoinedLifeClubs, String> {
  List<LifeClubs> findByuserId(String userId);
  //boolean search to find if club already exists with given name
  Boolean existsByclubName(String clubName);
  //find specific club by Id for edit and delete

}
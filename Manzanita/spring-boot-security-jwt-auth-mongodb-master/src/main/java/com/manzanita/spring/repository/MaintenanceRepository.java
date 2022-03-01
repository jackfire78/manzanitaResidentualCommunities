package com.manzanita.spring.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.WorkOrders;

//DAO that accesses monogoDB database and retrieves requested data from maintenance repo
public interface MaintenanceRepository extends MongoRepository<WorkOrders, String> {
  //boolean search to find all work orders that already exists with given username
  List<WorkOrders> existsByresidentName(String username);

}

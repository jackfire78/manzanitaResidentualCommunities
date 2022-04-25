package com.manzanita.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.User;
//DAO that accesses monogoDB database and retrieves requested data from users repo
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  List<User> findByRole(String role);

  //boolean search to find if account already exists with given username
  Boolean existsByUsername(String username);
  //boolean search to find if account already exists with given email
  Boolean existsByEmail(String email);
}

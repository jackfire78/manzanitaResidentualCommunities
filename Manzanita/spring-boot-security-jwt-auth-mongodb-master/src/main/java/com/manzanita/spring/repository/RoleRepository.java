package com.manzanita.spring.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.manzanita.spring.models.ERole;
import com.manzanita.spring.models.Role;
//DAO that accesses monogoDB database and retrieves requested data from role repo
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}

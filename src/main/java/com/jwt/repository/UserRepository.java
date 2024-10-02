package com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jwt.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("from User u where u.userName=:name")
	Optional<User> getUserByUsername(String name);
}

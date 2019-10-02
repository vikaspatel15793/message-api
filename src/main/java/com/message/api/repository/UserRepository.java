package com.message.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	
}

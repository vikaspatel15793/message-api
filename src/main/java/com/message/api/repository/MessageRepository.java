package com.message.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.message.api.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("select message from Message message where message.creator.username = ?1")
	Page<Message> findByCreatorUsername(String username, Pageable pageable);

}

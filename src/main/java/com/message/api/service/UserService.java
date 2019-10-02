package com.message.api.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.message.api.dto.UserDTO;
import com.message.api.model.User;
import com.message.api.repository.UserRepository;

/**
 * Service Implementation for managing User.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
    	User user1 = new User();
    	user1.setUsername("user1");
    	user1.setPassword(passwordEncoder.encode("user1"));
    	user1.setRole("ROLE_USER");
    	userRepository.save(user1);
    	
    	User user2 = new User();
    	user2.setUsername("user2");
    	user2.setPassword(passwordEncoder.encode("user2"));
    	user2.setRole("ROLE_USER");
    	userRepository.save(user2);
    }
    
    /**
     * Save a user.
     *
     * @param user the entity to save
     * @return the persisted entity
     */
    public UserDTO save(User user) {
        log.debug("Request to save User : {}", user);
        
        userRepository.save(user);
        
        UserDTO dto = new UserDTO();
        UserDTO.toDTO(dto, user);
        return dto;
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return userRepository.findAll(pageable).map(user -> UserDTO.toDTO(new UserDTO(), user));
    }


    /**
     * Get one user by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(user -> UserDTO.toDTO(new UserDTO(), user));
    }

    /**
     * Delete the user by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }
}

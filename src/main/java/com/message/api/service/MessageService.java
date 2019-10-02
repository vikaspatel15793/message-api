package com.message.api.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.message.api.dto.MessageDTO;
import com.message.api.model.Message;
import com.message.api.repository.MessageRepository;
import com.message.api.security.SecurityUtils;

/**
 * Service Implementation for managing Message.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private MessageRepository messageRepository;
    
    public MessageService(MessageRepository messageRepository) {
    	this.messageRepository = messageRepository;
    }

    /**
     * Save a message.
     *
     * @param message the entity to save
     * @return the persisted entity
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        
        Message message = null;
        if(messageDTO.getId() != null) {
			message = messageRepository.findById(messageDTO.getId()).get();
        }
        
        if(message == null) {
        	message = new Message();
        }
        
        MessageDTO.toEntity(message, messageDTO);
        
        
        messageRepository.save(message);
        return MessageDTO.toDTO(new MessageDTO(), message);
    }

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        String username = SecurityUtils.getLoginUser();
        return messageRepository.findByCreatorUsername(username, pageable).map(message -> MessageDTO.toDTO(new MessageDTO(), message));
    }


    /**
     * Get one message by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MessageDTO> findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(message -> MessageDTO.toDTO(new MessageDTO(), message));
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }
}

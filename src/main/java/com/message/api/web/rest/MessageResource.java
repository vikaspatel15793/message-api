package com.message.api.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.message.api.dto.MessageDTO;
import com.message.api.service.MessageService;
import com.message.api.web.rest.errors.BadRequestAlertException;
import com.message.api.web.rest.util.HeaderUtil;
import com.message.api.web.rest.util.PaginationUtil;
import com.message.api.web.rest.util.ResponseUtil;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

	private final Logger log = LoggerFactory.getLogger(MessageResource.class);

	private static final String ENTITY_NAME = "message";

	private MessageService messageService;

	public MessageResource(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * POST /messages : Create a new message.
	 *
	 * @param message the message to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         message, or with status 400 (Bad Request) if the message has already
	 *         an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/messages")
	public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO message) throws URISyntaxException {
		log.debug("REST request to save Message : {}", message);
		if (message.getId() != null) {
			throw new BadRequestAlertException("A new message cannot already have an ID");
		}
		MessageDTO result = messageService.save(message);
		return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /messages : Updates an existing message.
	 *
	 * @param message the message to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         message, or with status 400 (Bad Request) if the message is not
	 *         valid, or with status 500 (Internal Server Error) if the message
	 *         couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/messages")
	public ResponseEntity<MessageDTO> updateMessage(@Valid @RequestBody MessageDTO message) throws URISyntaxException {
		log.debug("REST request to update Message : {}", message);
		if (message.getId() == null) {
			throw new BadRequestAlertException("Invalid message id");
		}
		MessageDTO result = messageService.save(message);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, message.getId().toString()))
				.body(result);
	}

	/**
	 * GET /messages : get all the messages.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of messages in
	 *         body
	 */
	@GetMapping("/messages")
	public ResponseEntity<List<MessageDTO>> getAllMessages(Pageable pageable) {
		log.debug("REST request to get a page of Messages");
		Page<MessageDTO> page = messageService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messages");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /messages/:id : get the "id" message.
	 *
	 * @param id the id of the message to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the message, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/messages/{id}")
	public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id) {
		log.debug("REST request to get Message : {}", id);
		Optional<MessageDTO> message = messageService.findOne(id);
		return ResponseUtil.wrapOrNotFound(message);
	}

	/**
	 * DELETE /messages/:id : delete the "id" message.
	 *
	 * @param id the id of the message to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/messages/{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
		log.debug("REST request to delete Message : {}", id);
		messageService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}

package com.message.api.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.message.api.config.ApplicationContextProvider;
import com.message.api.model.Message;
import com.message.api.repository.UserRepository;
import com.message.api.security.SecurityUtils;

public class MessageDTO {

	private Long id;

	private UserDTO creator;

	@NotNull
	private String text;

	private LocalDateTime createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDTO getCreator() {
		return creator;
	}

	public void setCreator(UserDTO creator) {
		this.creator = creator;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public static MessageDTO toDTO(MessageDTO dto, Message entity) {
		dto.setId(entity.getId());
		dto.setText(entity.getText());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setCreator(UserDTO.toDTO(new UserDTO(), entity.getCreator()));
		return dto;
	}

	public static Message toEntity(Message entity, MessageDTO dto) {
		if(entity.getId() == null) {
			UserRepository userRepository = ApplicationContextProvider.getApplicationContext()
					.getBean(UserRepository.class);
			userRepository.findByUsername(SecurityUtils.getLoginUser()).ifPresent(creator -> {
				entity.setCreator(creator);
				creator.getMessages().add(entity);
			});
			entity.setCreatedDate(LocalDateTime.now());
		}

		entity.setText(dto.getText());
		return entity;
	}

}

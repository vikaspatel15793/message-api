package com.message.api.dto;

import com.message.api.model.User;

public class UserDTO {

	private Long id;

	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static UserDTO toDTO(UserDTO dto, User entity) {
		dto.setId(entity.getId());
		dto.setUsername(entity.getUsername());
		return dto;
	}
	
	public static User toEntity(User entity, UserDTO dto) {
		if(dto.getId() != null) {
			entity.setId(dto.getId());
		}

		entity.setUsername(dto.getUsername());
		return entity;
	}

}

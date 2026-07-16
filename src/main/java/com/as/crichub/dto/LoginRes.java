package com.as.crichub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {

	private String token;

	private String tokenType;

	private Long userId;

	private String email;

}

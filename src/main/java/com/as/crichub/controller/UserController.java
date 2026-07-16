package com.as.crichub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.crichub.dto.LoginReq;
import com.as.crichub.dto.UserRegisterReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<ResponseBean> registeruser(@Valid @RequestBody UserRegisterReq userRequest) {
		log.info("Inside UserController :: Inside registerUser()");
		ResponseBean response = userService.registerUser(userRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseBean> login(@Valid @RequestBody LoginReq loginRequest) {
		log.info("Inside UserController :: Inside login()");
		ResponseBean response = userService.login(loginRequest);
		return ResponseEntity.ok(response);
	}

}

package com.as.crichub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.crichub.dto.AddSquadPlayerReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.security.CustomUserDetails;
import com.as.crichub.service.SquadService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/teams/{teamId}/players")
public class SquadController {

	private final SquadService squadService;

	public SquadController(SquadService squadService) {
		this.squadService = squadService;
	}

	@GetMapping
	public ResponseEntity<ResponseBean> squad(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId) {
		log.info("Inside SquadController :: squad() teamId : {}", teamId);
		return ResponseEntity.ok(squadService.getSquad(principal.getUserId(), teamId));
	}

	@PostMapping
	public ResponseEntity<ResponseBean> addPlayer(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId, @Valid @RequestBody AddSquadPlayerReq request) {
		log.info("Inside SquadController :: addPlayer() teamId : {}", teamId);
		ResponseBean response = squadService.addPlayer(principal.getUserId(), teamId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{teamPlayerId}")
	public ResponseEntity<ResponseBean> removePlayer(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId, @PathVariable Long teamPlayerId) {
		log.info("Inside SquadController :: removePlayer() teamId : {} teamPlayerId : {}", teamId, teamPlayerId);
		return ResponseEntity.ok(squadService.removePlayer(principal.getUserId(), teamId, teamPlayerId));
	}
}

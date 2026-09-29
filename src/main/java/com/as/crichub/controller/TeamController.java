package com.as.crichub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.crichub.dto.TeamReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.security.CustomUserDetails;
import com.as.crichub.service.TeamService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/teams")
public class TeamController {

	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@GetMapping
	public ResponseEntity<ResponseBean> myTeams(@AuthenticationPrincipal CustomUserDetails principal) {
		log.info("Inside TeamController :: myTeams()");
		return ResponseEntity.ok(teamService.getMyTeams(principal.getUserId()));
	}

	@GetMapping("/{teamId}")
	public ResponseEntity<ResponseBean> getTeam(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId) {
		log.info("Inside TeamController :: getTeam() teamId : {}", teamId);
		return ResponseEntity.ok(teamService.getTeam(principal.getUserId(), teamId));
	}

	@PostMapping
	public ResponseEntity<ResponseBean> createTeam(@AuthenticationPrincipal CustomUserDetails principal,
			@Valid @RequestBody TeamReq request) {
		log.info("Inside TeamController :: createTeam()");
		ResponseBean response = teamService.createTeam(principal.getUserId(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{teamId}")
	public ResponseEntity<ResponseBean> updateTeam(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId, @Valid @RequestBody TeamReq request) {
		log.info("Inside TeamController :: updateTeam() teamId : {}", teamId);
		return ResponseEntity.ok(teamService.updateTeam(principal.getUserId(), teamId, request));
	}

	@DeleteMapping("/{teamId}")
	public ResponseEntity<ResponseBean> deleteTeam(@AuthenticationPrincipal CustomUserDetails principal,
			@PathVariable Long teamId) {
		log.info("Inside TeamController :: deleteTeam() teamId : {}", teamId);
		return ResponseEntity.ok(teamService.deleteTeam(principal.getUserId(), teamId));
	}
}

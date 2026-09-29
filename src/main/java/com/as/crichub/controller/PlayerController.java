package com.as.crichub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.crichub.dto.PlayerUpdateReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.security.CustomUserDetails;
import com.as.crichub.service.PlayerService;
import com.as.crichub.service.StatsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;
	private final StatsService statsService;

	public PlayerController(PlayerService playerService, StatsService statsService) {
		this.playerService = playerService;
		this.statsService = statsService;
	}

	/**
	 * GET /api/players/me — the profile of the currently authenticated user.
	 * The principal is set by {@code JwtAuthenticationFilter}; security config
	 * guarantees it is non-null here.
	 */
	@GetMapping("/me")
	public ResponseEntity<ResponseBean> myProfile(@AuthenticationPrincipal CustomUserDetails principal) {
		log.info("Inside PlayerController :: myProfile()");
		ResponseBean response = playerService.getMyProfile(principal.getUserId());
		return ResponseEntity.ok(response);
	}

	/**
	 * PUT /api/players/me — update the current user's player profile.
	 */
	@PutMapping("/me")
	public ResponseEntity<ResponseBean> updateMyProfile(@AuthenticationPrincipal CustomUserDetails principal,
			@Valid @RequestBody PlayerUpdateReq request) {
		log.info("Inside PlayerController :: updateMyProfile()");
		ResponseBean response = playerService.updateMyProfile(principal.getUserId(), request);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET /api/players/{playerId}/stats — a player's aggregated career stats.
	 */
	@GetMapping("/{playerId}/stats")
	public ResponseEntity<ResponseBean> playerStats(@PathVariable Long playerId) {
		log.info("Inside PlayerController :: playerStats() playerId : {}", playerId);
		return ResponseEntity.ok(statsService.getPlayerStats(playerId));
	}
}

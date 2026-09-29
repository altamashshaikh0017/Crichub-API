package com.as.crichub.controller;

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

import com.as.crichub.dto.RegisterTeamReq;
import com.as.crichub.dto.TournamentReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.MatchService;
import com.as.crichub.service.TournamentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/tournaments")
public class TournamentController {

	private final TournamentService tournamentService;
	private final MatchService matchService;

	public TournamentController(TournamentService tournamentService, MatchService matchService) {
		this.tournamentService = tournamentService;
		this.matchService = matchService;
	}

	@GetMapping
	public ResponseEntity<ResponseBean> getAllTournaments() {
		log.info("Inside TournamentController :: getAllTournaments()");
		return ResponseEntity.ok(tournamentService.getAllTournaments());
	}

	@GetMapping("/{tournamentId}")
	public ResponseEntity<ResponseBean> getTournament(@PathVariable Long tournamentId) {
		log.info("Inside TournamentController :: getTournament() tournamentId : {}", tournamentId);
		return ResponseEntity.ok(tournamentService.getTournament(tournamentId));
	}

	@PostMapping
	public ResponseEntity<ResponseBean> createTournament(@Valid @RequestBody TournamentReq request) {
		log.info("Inside TournamentController :: createTournament()");
		ResponseBean response = tournamentService.createTournament(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{tournamentId}")
	public ResponseEntity<ResponseBean> updateTournament(@PathVariable Long tournamentId,
			@Valid @RequestBody TournamentReq request) {
		log.info("Inside TournamentController :: updateTournament() tournamentId : {}", tournamentId);
		return ResponseEntity.ok(tournamentService.updateTournament(tournamentId, request));
	}

	@DeleteMapping("/{tournamentId}")
	public ResponseEntity<ResponseBean> deleteTournament(@PathVariable Long tournamentId) {
		log.info("Inside TournamentController :: deleteTournament() tournamentId : {}", tournamentId);
		return ResponseEntity.ok(tournamentService.deleteTournament(tournamentId));
	}

	@GetMapping("/{tournamentId}/teams")
	public ResponseEntity<ResponseBean> getRegisteredTeams(@PathVariable Long tournamentId) {
		log.info("Inside TournamentController :: getRegisteredTeams() tournamentId : {}", tournamentId);
		return ResponseEntity.ok(tournamentService.getRegisteredTeams(tournamentId));
	}

	@PostMapping("/{tournamentId}/teams")
	public ResponseEntity<ResponseBean> registerTeam(@PathVariable Long tournamentId,
			@Valid @RequestBody RegisterTeamReq request) {
		log.info("Inside TournamentController :: registerTeam() tournamentId : {}", tournamentId);
		ResponseBean response = tournamentService.registerTeam(tournamentId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{tournamentId}/teams/{registrationId}")
	public ResponseEntity<ResponseBean> unregisterTeam(@PathVariable Long tournamentId,
			@PathVariable Long registrationId) {
		log.info("Inside TournamentController :: unregisterTeam() tournamentId : {} registrationId : {}",
				tournamentId, registrationId);
		return ResponseEntity.ok(tournamentService.unregisterTeam(tournamentId, registrationId));
	}

	@GetMapping("/{tournamentId}/matches")
	public ResponseEntity<ResponseBean> getTournamentMatches(@PathVariable Long tournamentId) {
		log.info("Inside TournamentController :: getTournamentMatches() tournamentId : {}", tournamentId);
		return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
	}
}

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

import com.as.crichub.dto.MatchReq;
import com.as.crichub.dto.MatchResultReq;
import com.as.crichub.dto.ScorecardReq;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.MatchService;
import com.as.crichub.service.ScorecardService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/matches")
public class MatchController {

	private final MatchService matchService;
	private final ScorecardService scorecardService;

	public MatchController(MatchService matchService, ScorecardService scorecardService) {
		this.matchService = matchService;
		this.scorecardService = scorecardService;
	}

	@GetMapping
	public ResponseEntity<ResponseBean> getAllMatches() {
		log.info("Inside MatchController :: getAllMatches()");
		return ResponseEntity.ok(matchService.getAllMatches());
	}

	@GetMapping("/{matchId}")
	public ResponseEntity<ResponseBean> getMatch(@PathVariable Long matchId) {
		log.info("Inside MatchController :: getMatch() matchId : {}", matchId);
		return ResponseEntity.ok(matchService.getMatch(matchId));
	}

	@PostMapping
	public ResponseEntity<ResponseBean> createMatch(@Valid @RequestBody MatchReq request) {
		log.info("Inside MatchController :: createMatch()");
		ResponseBean response = matchService.createMatch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{matchId}")
	public ResponseEntity<ResponseBean> updateMatch(@PathVariable Long matchId,
			@Valid @RequestBody MatchReq request) {
		log.info("Inside MatchController :: updateMatch() matchId : {}", matchId);
		return ResponseEntity.ok(matchService.updateMatch(matchId, request));
	}

	@PostMapping("/{matchId}/result")
	public ResponseEntity<ResponseBean> recordResult(@PathVariable Long matchId,
			@Valid @RequestBody MatchResultReq request) {
		log.info("Inside MatchController :: recordResult() matchId : {}", matchId);
		return ResponseEntity.ok(matchService.recordResult(matchId, request));
	}

	@DeleteMapping("/{matchId}")
	public ResponseEntity<ResponseBean> deleteMatch(@PathVariable Long matchId) {
		log.info("Inside MatchController :: deleteMatch() matchId : {}", matchId);
		return ResponseEntity.ok(matchService.deleteMatch(matchId));
	}

	@GetMapping("/{matchId}/scorecard")
	public ResponseEntity<ResponseBean> getScorecard(@PathVariable Long matchId) {
		log.info("Inside MatchController :: getScorecard() matchId : {}", matchId);
		return ResponseEntity.ok(scorecardService.getScorecard(matchId));
	}

	@PutMapping("/{matchId}/scorecard")
	public ResponseEntity<ResponseBean> saveScorecard(@PathVariable Long matchId,
			@Valid @RequestBody ScorecardReq request) {
		log.info("Inside MatchController :: saveScorecard() matchId : {}", matchId);
		return ResponseEntity.ok(scorecardService.saveScorecard(matchId, request));
	}
}

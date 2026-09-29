package com.as.crichub.serviceImpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.MatchReq;
import com.as.crichub.dto.MatchResultReq;
import com.as.crichub.dto.MatchRes;
import com.as.crichub.entity.Match;
import com.as.crichub.entity.Team;
import com.as.crichub.entity.Tournament;
import com.as.crichub.enums.MatchStatus;
import com.as.crichub.enums.ResultType;
import com.as.crichub.exception.BadRequestException;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.MatchRepository;
import com.as.crichub.repository.TeamRepository;
import com.as.crichub.repository.TournamentRegistrationRepository;
import com.as.crichub.repository.TournamentRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.MatchService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService {

	private final MatchRepository matchRepository;
	private final TeamRepository teamRepository;
	private final TournamentRepository tournamentRepository;
	private final TournamentRegistrationRepository registrationRepository;

	public MatchServiceImpl(MatchRepository matchRepository, TeamRepository teamRepository,
			TournamentRepository tournamentRepository,
			TournamentRegistrationRepository registrationRepository) {
		this.matchRepository = matchRepository;
		this.teamRepository = teamRepository;
		this.tournamentRepository = tournamentRepository;
		this.registrationRepository = registrationRepository;
	}

	@Override
	@Transactional
	public ResponseBean createMatch(MatchReq request) {
		log.info("Inside MatchServiceImpl :: createMatch()");

		Match match = new Match();
		applySchedule(match, request);
		match.setStatus(MatchStatus.SCHEDULED);

		Match saved = matchRepository.save(match);
		log.info("Match created with id : {}", saved.getMatchId());

		return new ResponseBean(true, "Match created successfully", HttpStatus.CREATED.value(),
				MatchRes.from(saved));
	}

	@Override
	public ResponseBean getAllMatches() {
		log.info("Inside MatchServiceImpl :: getAllMatches()");

		List<MatchRes> matches = matchRepository.findAllByOrderByMatchIdDesc().stream()
				.map(MatchRes::from).toList();

		return new ResponseBean(true, "Matches fetched successfully", HttpStatus.OK.value(), matches);
	}

	@Override
	public ResponseBean getMatch(Long matchId) {
		log.info("Inside MatchServiceImpl :: getMatch() matchId : {}", matchId);
		Match match = requireMatch(matchId);
		return new ResponseBean(true, "Match fetched successfully", HttpStatus.OK.value(), MatchRes.from(match));
	}

	@Override
	public ResponseBean getMatchesByTournament(Long tournamentId) {
		log.info("Inside MatchServiceImpl :: getMatchesByTournament() tournamentId : {}", tournamentId);
		requireTournament(tournamentId);

		List<MatchRes> matches = matchRepository.findByTournamentTournamentIdOrderByMatchIdDesc(tournamentId)
				.stream().map(MatchRes::from).toList();

		return new ResponseBean(true, "Matches fetched successfully", HttpStatus.OK.value(), matches);
	}

	@Override
	@Transactional
	public ResponseBean updateMatch(Long matchId, MatchReq request) {
		log.info("Inside MatchServiceImpl :: updateMatch() matchId : {}", matchId);

		Match match = requireMatch(matchId);
		applySchedule(match, request);

		Match saved = matchRepository.save(match);
		return new ResponseBean(true, "Match updated successfully", HttpStatus.OK.value(), MatchRes.from(saved));
	}

	@Override
	@Transactional
	public ResponseBean recordResult(Long matchId, MatchResultReq request) {
		log.info("Inside MatchServiceImpl :: recordResult() matchId : {}", matchId);

		Match match = requireMatch(matchId);

		applyToss(match, request);
		applyOutcome(match, request);

		match.setTeamARuns(request.getTeamARuns());
		match.setTeamAWickets(request.getTeamAWickets());
		match.setTeamAOvers(request.getTeamAOvers());
		match.setTeamBRuns(request.getTeamBRuns());
		match.setTeamBWickets(request.getTeamBWickets());
		match.setTeamBOvers(request.getTeamBOvers());
		match.setResultSummary(request.getResultSummary());
		match.setStatus(request.getResultType() == ResultType.ABANDONED ? MatchStatus.ABANDONED
				: MatchStatus.COMPLETED);

		Match saved = matchRepository.save(match);
		return new ResponseBean(true, "Result recorded successfully", HttpStatus.OK.value(), MatchRes.from(saved));
	}

	@Override
	@Transactional
	public ResponseBean deleteMatch(Long matchId) {
		log.info("Inside MatchServiceImpl :: deleteMatch() matchId : {}", matchId);
		Match match = requireMatch(matchId);
		matchRepository.delete(match);
		return new ResponseBean(true, "Match deleted successfully", HttpStatus.OK.value(), null);
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	/** Sets teams, tournament, and schedule fields for both create and update. */
	private void applySchedule(Match match, MatchReq request) {
		if (request.getTeamAId().equals(request.getTeamBId())) {
			throw new BadRequestException("A match needs two different teams");
		}

		Team teamA = teamRepository.findById(request.getTeamAId())
				.orElseThrow(() -> new DataNotFoundException("Team A not found"));
		Team teamB = teamRepository.findById(request.getTeamBId())
				.orElseThrow(() -> new DataNotFoundException("Team B not found"));

		Integer overs = request.getOvers();
		Tournament tournament = null;
		if (request.getTournamentId() != null) {
			tournament = requireTournament(request.getTournamentId());
			requireRegistered(tournament.getTournamentId(), teamA.getTeamId(), "Team A");
			requireRegistered(tournament.getTournamentId(), teamB.getTeamId(), "Team B");
			if (overs == null) {
				overs = tournament.getOvers();
			}
		}
		if (overs == null) {
			throw new BadRequestException("Overs is required for a standalone match");
		}

		match.setTeamA(teamA);
		match.setTeamB(teamB);
		match.setTournament(tournament);
		match.setMatchDate(request.getMatchDate());
		match.setVenue(request.getVenue().trim());
		match.setOvers(overs);
	}

	/** Applies toss details, validating the toss winner is one of the two teams. */
	private void applyToss(Match match, MatchResultReq request) {
		if (request.getTossWinnerId() == null) {
			match.setTossWinner(null);
			match.setTossDecision(null);
			return;
		}
		if (request.getTossDecision() == null) {
			throw new BadRequestException("Toss decision is required when a toss winner is set");
		}
		match.setTossWinner(teamInMatch(match, request.getTossWinnerId(), "Toss winner"));
		match.setTossDecision(request.getTossDecision());
	}

	/** Applies the result type and winner, enforcing the WON-needs-a-winner rule. */
	private void applyOutcome(Match match, MatchResultReq request) {
		match.setResultType(request.getResultType());

		if (request.getResultType() == ResultType.WON) {
			if (request.getWinnerId() == null) {
				throw new BadRequestException("A winner is required when the result is WON");
			}
			match.setWinner(teamInMatch(match, request.getWinnerId(), "Winner"));
		} else {
			if (request.getWinnerId() != null) {
				throw new BadRequestException("A winner must not be set unless the result is WON");
			}
			match.setWinner(null);
		}
	}

	/** Resolves a team id to teamA or teamB of the match, or fails. */
	private Team teamInMatch(Match match, Long teamId, String label) {
		if (match.getTeamA().getTeamId().equals(teamId)) {
			return match.getTeamA();
		}
		if (match.getTeamB().getTeamId().equals(teamId)) {
			return match.getTeamB();
		}
		throw new BadRequestException(label + " must be one of the two teams in the match");
	}

	private Match requireMatch(Long matchId) {
		return matchRepository.findById(matchId)
				.orElseThrow(() -> new DataNotFoundException("Match not found"));
	}

	private Tournament requireTournament(Long tournamentId) {
		return tournamentRepository.findById(tournamentId)
				.orElseThrow(() -> new DataNotFoundException("Tournament not found"));
	}

	private void requireRegistered(Long tournamentId, Long teamId, String label) {
		if (!registrationRepository.existsByTournamentTournamentIdAndTeamTeamId(tournamentId, teamId)) {
			throw new BadRequestException(label + " is not registered for this tournament");
		}
	}
}

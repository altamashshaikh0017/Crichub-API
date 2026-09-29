package com.as.crichub.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.PerformanceReq;
import com.as.crichub.dto.PerformanceRes;
import com.as.crichub.dto.ScorecardReq;
import com.as.crichub.dto.ScorecardRes;
import com.as.crichub.dto.ScorecardRes.TeamScorecard;
import com.as.crichub.entity.Match;
import com.as.crichub.entity.Player;
import com.as.crichub.entity.PlayerMatchPerformance;
import com.as.crichub.entity.Team;
import com.as.crichub.exception.BadRequestException;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.MatchRepository;
import com.as.crichub.repository.PlayerMatchPerformanceRepository;
import com.as.crichub.repository.PlayerRepository;
import com.as.crichub.repository.TeamPlayerRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.ScorecardService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScorecardServiceImpl implements ScorecardService {

	private final MatchRepository matchRepository;
	private final PlayerMatchPerformanceRepository performanceRepository;
	private final PlayerRepository playerRepository;
	private final TeamPlayerRepository teamPlayerRepository;

	public ScorecardServiceImpl(MatchRepository matchRepository,
			PlayerMatchPerformanceRepository performanceRepository, PlayerRepository playerRepository,
			TeamPlayerRepository teamPlayerRepository) {
		this.matchRepository = matchRepository;
		this.performanceRepository = performanceRepository;
		this.playerRepository = playerRepository;
		this.teamPlayerRepository = teamPlayerRepository;
	}

	@Override
	public ResponseBean getScorecard(Long matchId) {
		log.info("Inside ScorecardServiceImpl :: getScorecard() matchId : {}", matchId);
		Match match = requireMatch(matchId);
		List<PlayerMatchPerformance> lines = performanceRepository
				.findByMatchMatchIdOrderByPerformanceIdAsc(matchId);
		return new ResponseBean(true, "Scorecard fetched successfully", HttpStatus.OK.value(),
				buildScorecard(match, lines));
	}

	@Override
	@Transactional
	public ResponseBean saveScorecard(Long matchId, ScorecardReq request) {
		log.info("Inside ScorecardServiceImpl :: saveScorecard() matchId : {}", matchId);
		Match match = requireMatch(matchId);

		Long teamAId = match.getTeamA().getTeamId();
		Long teamBId = match.getTeamB().getTeamId();
		Set<Long> seenPlayers = new HashSet<>();

		List<PlayerMatchPerformance> toSave = new ArrayList<>();
		for (PerformanceReq req : request.getPerformances()) {
			Team team = teamForId(match, req.getTeamId(), teamAId, teamBId);
			Player player = playerRepository.findById(req.getPlayerId())
					.orElseThrow(() -> new DataNotFoundException("Player not found"));

			if (!teamPlayerRepository.existsByTeamTeamIdAndPlayerPlayerId(team.getTeamId(),
					player.getPlayerId())) {
				throw new BadRequestException(
						player.getPlayerName() + " is not in " + team.getTeamName() + "'s squad");
			}
			if (!seenPlayers.add(player.getPlayerId())) {
				throw new BadRequestException(player.getPlayerName() + " has more than one scorecard entry");
			}

			toSave.add(toEntity(match, team, player, req));
		}

		// Replace the whole card: clear the old lines, then persist the new set.
		performanceRepository.deleteByMatchMatchId(matchId);
		performanceRepository.flush();
		List<PlayerMatchPerformance> saved = performanceRepository.saveAll(toSave);
		log.info("Saved {} scorecard lines for match {}", saved.size(), matchId);

		return new ResponseBean(true, "Scorecard saved successfully", HttpStatus.OK.value(),
				buildScorecard(match, saved));
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private ScorecardRes buildScorecard(Match match, List<PlayerMatchPerformance> lines) {
		Team teamA = match.getTeamA();
		Team teamB = match.getTeamB();

		List<PerformanceRes> aLines = new ArrayList<>();
		List<PerformanceRes> bLines = new ArrayList<>();
		for (PlayerMatchPerformance line : lines) {
			if (line.getTeam().getTeamId().equals(teamA.getTeamId())) {
				aLines.add(PerformanceRes.from(line));
			} else if (line.getTeam().getTeamId().equals(teamB.getTeamId())) {
				bLines.add(PerformanceRes.from(line));
			}
		}

		return new ScorecardRes(match.getMatchId(),
				new TeamScorecard(teamA.getTeamId(), teamA.getTeamName(), aLines),
				new TeamScorecard(teamB.getTeamId(), teamB.getTeamName(), bLines));
	}

	private Team teamForId(Match match, Long teamId, Long teamAId, Long teamBId) {
		if (teamId.equals(teamAId)) {
			return match.getTeamA();
		}
		if (teamId.equals(teamBId)) {
			return match.getTeamB();
		}
		throw new BadRequestException("A scorecard team must be one of the two teams in the match");
	}

	private PlayerMatchPerformance toEntity(Match match, Team team, Player player, PerformanceReq req) {
		PlayerMatchPerformance p = new PlayerMatchPerformance();
		p.setMatch(match);
		p.setTeam(team);
		p.setPlayer(player);
		p.setRuns(req.getRuns());
		p.setBallsFaced(req.getBallsFaced());
		p.setFours(req.getFours());
		p.setSixes(req.getSixes());
		p.setNotOut(req.getNotOut());
		p.setOversBowled(req.getOversBowled());
		p.setRunsConceded(req.getRunsConceded());
		p.setWickets(req.getWickets());
		p.setMaidens(req.getMaidens());
		return p;
	}

	private Match requireMatch(Long matchId) {
		return matchRepository.findById(matchId)
				.orElseThrow(() -> new DataNotFoundException("Match not found"));
	}
}

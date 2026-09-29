package com.as.crichub.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.as.crichub.dto.StandingRes;
import com.as.crichub.entity.Match;
import com.as.crichub.entity.Team;
import com.as.crichub.enums.MatchStatus;
import com.as.crichub.enums.ResultType;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.MatchRepository;
import com.as.crichub.repository.TournamentRegistrationRepository;
import com.as.crichub.repository.TournamentRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.StandingsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StandingsServiceImpl implements StandingsService {

	private static final int POINTS_WIN = 2;
	private static final int POINTS_DRAW = 1; // tie or no-result

	private final TournamentRepository tournamentRepository;
	private final TournamentRegistrationRepository registrationRepository;
	private final MatchRepository matchRepository;

	public StandingsServiceImpl(TournamentRepository tournamentRepository,
			TournamentRegistrationRepository registrationRepository, MatchRepository matchRepository) {
		this.tournamentRepository = tournamentRepository;
		this.registrationRepository = registrationRepository;
		this.matchRepository = matchRepository;
	}

	@Override
	public ResponseBean getStandings(Long tournamentId) {
		log.info("Inside StandingsServiceImpl :: getStandings() tournamentId : {}", tournamentId);

		if (!tournamentRepository.existsById(tournamentId)) {
			throw new DataNotFoundException("Tournament not found");
		}

		// Seed a row for every registered team so teams with no matches still appear.
		Map<Long, Row> rows = new LinkedHashMap<>();
		registrationRepository.findByTournamentTournamentId(tournamentId).forEach(reg -> {
			Team team = reg.getTeam();
			rows.putIfAbsent(team.getTeamId(), new Row(team.getTeamId(), team.getTeamName()));
		});

		for (Match match : matchRepository.findByTournamentTournamentIdOrderByMatchIdDesc(tournamentId)) {
			if (match.getStatus() != MatchStatus.COMPLETED) {
				continue;
			}
			tally(rows, match);
		}

		List<StandingRes> table = rank(rows.values());
		return new ResponseBean(true, "Standings fetched successfully", HttpStatus.OK.value(), table);
	}

	/** Applies one completed match to the two teams' rows. */
	private void tally(Map<Long, Row> rows, Match match) {
		Row a = rows.get(match.getTeamA().getTeamId());
		Row b = rows.get(match.getTeamB().getTeamId());
		if (a == null || b == null) {
			// A participant is no longer registered; leave it out of the table.
			return;
		}

		ResultType result = match.getResultType();
		if (result == ResultType.WON && match.getWinner() != null) {
			boolean aWon = match.getWinner().getTeamId().equals(a.teamId);
			Row winner = aWon ? a : b;
			Row loser = aWon ? b : a;
			winner.won++;
			winner.points += POINTS_WIN;
			loser.lost++;
			addNrr(a, b, match);
		} else if (result == ResultType.TIE) {
			a.tied++;
			b.tied++;
			a.points += POINTS_DRAW;
			b.points += POINTS_DRAW;
			addNrr(a, b, match);
		} else if (result == ResultType.NO_RESULT) {
			a.noResult++;
			b.noResult++;
			a.points += POINTS_DRAW;
			b.points += POINTS_DRAW;
		}
	}

	/** Adds a decided match's scores to both teams' run-rate accumulators, when present. */
	private void addNrr(Row a, Row b, Match match) {
		Integer ar = match.getTeamARuns();
		Integer br = match.getTeamBRuns();
		Double ao = match.getTeamAOvers();
		Double bo = match.getTeamBOvers();
		if (ar == null || br == null || ao == null || bo == null || ao <= 0 || bo <= 0) {
			return;
		}
		a.addInnings(ar, ao, br, bo);
		b.addInnings(br, bo, ar, ao);
	}

	/** Sorts by points then NRR (nulls last) and assigns 1-based ranks. */
	private List<StandingRes> rank(java.util.Collection<Row> values) {
		List<Row> sorted = new ArrayList<>(values);
		sorted.sort(Comparator
				.comparingInt((Row r) -> r.points).reversed()
				.thenComparing(Comparator.comparingDouble(
						(Row r) -> r.netRunRate() == null ? Double.NEGATIVE_INFINITY : r.netRunRate()).reversed()));

		List<StandingRes> table = new ArrayList<>(sorted.size());
		int rank = 1;
		for (Row r : sorted) {
			table.add(new StandingRes(rank++, r.teamId, r.teamName,
					r.won + r.lost + r.tied + r.noResult, r.won, r.lost, r.tied, r.noResult, r.points,
					r.netRunRate()));
		}
		return table;
	}

	/** Mutable per-team accumulator used while building the table. */
	private static final class Row {
		private final Long teamId;
		private final String teamName;
		private int won;
		private int lost;
		private int tied;
		private int noResult;
		private int points;

		private int runsFor;
		private double oversFor;
		private int runsAgainst;
		private double oversAgainst;

		Row(Long teamId, String teamName) {
			this.teamId = teamId;
			this.teamName = teamName;
		}

		void addInnings(int runsScored, double oversFaced, int runsConceded, double oversBowled) {
			runsFor += runsScored;
			oversFor += oversFaced;
			runsAgainst += runsConceded;
			oversAgainst += oversBowled;
		}

		Double netRunRate() {
			if (oversFor <= 0 || oversAgainst <= 0) {
				return null;
			}
			double nrr = (runsFor / oversFor) - (runsAgainst / oversAgainst);
			return Math.round(nrr * 1000.0) / 1000.0;
		}
	}
}

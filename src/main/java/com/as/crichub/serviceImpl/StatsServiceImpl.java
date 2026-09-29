package com.as.crichub.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.as.crichub.dto.LeaderboardRes;
import com.as.crichub.dto.LeaderboardRes.RunScorer;
import com.as.crichub.dto.LeaderboardRes.WicketTaker;
import com.as.crichub.dto.PlayerStatsRes;
import com.as.crichub.entity.Player;
import com.as.crichub.entity.PlayerMatchPerformance;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.PlayerMatchPerformanceRepository;
import com.as.crichub.repository.PlayerRepository;
import com.as.crichub.repository.TournamentRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.StatsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

	private static final int LEADERBOARD_SIZE = 10;

	private final PlayerRepository playerRepository;
	private final TournamentRepository tournamentRepository;
	private final PlayerMatchPerformanceRepository performanceRepository;

	public StatsServiceImpl(PlayerRepository playerRepository, TournamentRepository tournamentRepository,
			PlayerMatchPerformanceRepository performanceRepository) {
		this.playerRepository = playerRepository;
		this.tournamentRepository = tournamentRepository;
		this.performanceRepository = performanceRepository;
	}

	@Override
	public ResponseBean getPlayerStats(Long playerId) {
		log.info("Inside StatsServiceImpl :: getPlayerStats() playerId : {}", playerId);
		Player player = playerRepository.findById(playerId)
				.orElseThrow(() -> new DataNotFoundException("Player not found"));

		Agg agg = new Agg();
		performanceRepository.findByPlayerPlayerId(playerId).forEach(agg::add);

		PlayerStatsRes stats = new PlayerStatsRes(player.getPlayerId(), player.getPlayerName(),
				player.getPlayingRole(), agg.matchIds.size(), agg.toBatting(), agg.toBowling());
		return new ResponseBean(true, "Player stats fetched successfully", HttpStatus.OK.value(), stats);
	}

	@Override
	public ResponseBean getTournamentLeaderboard(Long tournamentId) {
		log.info("Inside StatsServiceImpl :: getTournamentLeaderboard() tournamentId : {}", tournamentId);
		if (!tournamentRepository.existsById(tournamentId)) {
			throw new DataNotFoundException("Tournament not found");
		}

		Map<Long, Agg> byPlayer = new LinkedHashMap<>();
		for (PlayerMatchPerformance p : performanceRepository.findByMatchTournamentTournamentId(tournamentId)) {
			byPlayer.computeIfAbsent(p.getPlayer().getPlayerId(), k -> new Agg()).add(p);
		}

		LeaderboardRes board = new LeaderboardRes(topRunScorers(byPlayer.values()),
				topWicketTakers(byPlayer.values()));
		return new ResponseBean(true, "Leaderboard fetched successfully", HttpStatus.OK.value(), board);
	}

	private List<RunScorer> topRunScorers(java.util.Collection<Agg> aggs) {
		List<Agg> batters = new ArrayList<>();
		for (Agg a : aggs) {
			if (a.batInnings > 0) {
				batters.add(a);
			}
		}
		batters.sort(Comparator.comparingInt((Agg a) -> a.runs).reversed()
				.thenComparing(Comparator.comparingDouble((Agg a) -> a.strikeRate() == null ? 0 : a.strikeRate())
						.reversed()));

		List<RunScorer> rows = new ArrayList<>();
		int rank = 1;
		for (Agg a : batters.subList(0, Math.min(LEADERBOARD_SIZE, batters.size()))) {
			rows.add(new RunScorer(rank++, a.playerId, a.playerName, a.teamName, a.batInnings, a.runs,
					a.battingAverage(), a.strikeRate()));
		}
		return rows;
	}

	private List<WicketTaker> topWicketTakers(java.util.Collection<Agg> aggs) {
		List<Agg> bowlers = new ArrayList<>();
		for (Agg a : aggs) {
			if (a.bowlInnings > 0 && a.wickets > 0) {
				bowlers.add(a);
			}
		}
		bowlers.sort(Comparator.comparingInt((Agg a) -> a.wickets).reversed()
				.thenComparing(Comparator.comparingDouble(
						(Agg a) -> a.economy() == null ? Double.MAX_VALUE : a.economy())));

		List<WicketTaker> rows = new ArrayList<>();
		int rank = 1;
		for (Agg a : bowlers.subList(0, Math.min(LEADERBOARD_SIZE, bowlers.size()))) {
			rows.add(new WicketTaker(rank++, a.playerId, a.playerName, a.teamName, a.bowlInnings, a.wickets,
					a.economy()));
		}
		return rows;
	}

	private static int nz(Integer v) {
		return v == null ? 0 : v;
	}

	/** Overs like 3.4 (3 overs, 4 balls) → 22 balls. */
	private static int ballsFromOvers(Double overs) {
		if (overs == null) {
			return 0;
		}
		int whole = (int) Math.floor(overs);
		int balls = (int) Math.round((overs - whole) * 10);
		return whole * 6 + balls;
	}

	private static double round2(double v) {
		return Math.round(v * 100.0) / 100.0;
	}

	/** Mutable per-player accumulator used for both career stats and leaderboards. */
	private static final class Agg {
		private Long playerId;
		private String playerName;
		private String teamName;
		private final Set<Long> matchIds = new HashSet<>();

		private int batInnings, runs, balls, fours, sixes, notOuts, fifties, hundreds;
		private int highest = -1;

		private int bowlInnings, ballsBowled, runsConceded, wickets, maidens;
		private int bestW = -1, bestR = Integer.MAX_VALUE;

		void add(PlayerMatchPerformance p) {
			if (playerId == null) {
				playerId = p.getPlayer().getPlayerId();
				playerName = p.getPlayer().getPlayerName();
			}
			if (teamName == null && p.getTeam() != null) {
				teamName = p.getTeam().getTeamName();
			}
			matchIds.add(p.getMatch().getMatchId());

			boolean batted = p.getRuns() != null || p.getBallsFaced() != null
					|| Boolean.TRUE.equals(p.getNotOut());
			if (batted) {
				batInnings++;
				int r = nz(p.getRuns());
				runs += r;
				balls += nz(p.getBallsFaced());
				fours += nz(p.getFours());
				sixes += nz(p.getSixes());
				if (Boolean.TRUE.equals(p.getNotOut())) {
					notOuts++;
				}
				if (r >= 100) {
					hundreds++;
				} else if (r >= 50) {
					fifties++;
				}
				highest = Math.max(highest, r);
			}

			boolean bowled = p.getOversBowled() != null || p.getWickets() != null
					|| p.getRunsConceded() != null;
			if (bowled) {
				bowlInnings++;
				ballsBowled += ballsFromOvers(p.getOversBowled());
				int rc = nz(p.getRunsConceded());
				int w = nz(p.getWickets());
				runsConceded += rc;
				wickets += w;
				maidens += nz(p.getMaidens());
				if (w > bestW || (w == bestW && rc < bestR)) {
					bestW = w;
					bestR = rc;
				}
			}
		}

		Double battingAverage() {
			int dismissals = batInnings - notOuts;
			return dismissals > 0 ? round2((double) runs / dismissals) : null;
		}

		Double strikeRate() {
			return balls > 0 ? round2((double) runs * 100 / balls) : null;
		}

		Double bowlingAverage() {
			return wickets > 0 ? round2((double) runsConceded / wickets) : null;
		}

		Double economy() {
			return ballsBowled > 0 ? round2(runsConceded / (ballsBowled / 6.0)) : null;
		}

		String oversText() {
			return (ballsBowled / 6) + "." + (ballsBowled % 6);
		}

		PlayerStatsRes.Batting toBatting() {
			return new PlayerStatsRes.Batting(batInnings, runs, balls, notOuts, fours, sixes, fifties,
					hundreds, batInnings > 0 ? highest : null, battingAverage(), strikeRate());
		}

		PlayerStatsRes.Bowling toBowling() {
			String best = bowlInnings > 0 && bestW >= 0 ? bestW + "/" + bestR : null;
			return new PlayerStatsRes.Bowling(bowlInnings, oversText(), runsConceded, wickets, maidens,
					bowlingAverage(), economy(), best);
		}
	}
}

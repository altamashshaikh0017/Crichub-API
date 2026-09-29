package com.as.crichub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.PlayerMatchPerformance;

@Repository
public interface PlayerMatchPerformanceRepository extends JpaRepository<PlayerMatchPerformance, Long> {

	/** All performance lines for a match, in insertion order. */
	List<PlayerMatchPerformance> findByMatchMatchIdOrderByPerformanceIdAsc(Long matchId);

	/** Removes every performance line for a match — used when replacing a scorecard. */
	void deleteByMatchMatchId(Long matchId);

	/** Every performance line for a player, for aggregating career stats. */
	List<PlayerMatchPerformance> findByPlayerPlayerId(Long playerId);

	/** Every performance line across a tournament's matches, for leaderboards. */
	List<PlayerMatchPerformance> findByMatchTournamentTournamentId(Long tournamentId);
}

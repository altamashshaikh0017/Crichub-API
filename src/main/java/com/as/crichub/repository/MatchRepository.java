package com.as.crichub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

	/** All matches, newest first. */
	List<Match> findAllByOrderByMatchIdDesc();

	/** Matches belonging to a tournament, newest first. */
	List<Match> findByTournamentTournamentIdOrderByMatchIdDesc(Long tournamentId);
}

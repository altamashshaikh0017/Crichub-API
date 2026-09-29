package com.as.crichub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

	/** All tournaments, newest first. */
	List<Tournament> findAllByOrderByTournamentIdDesc();
}

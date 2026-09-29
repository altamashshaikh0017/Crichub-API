package com.as.crichub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	/** The teams owned by a user, newest first. */
	List<Team> findByOwnerUserIdOrderByTeamIdDesc(Long userId);

	/**
	 * A single team, scoped to its owner. Returning empty for a team the user
	 * doesn't own keeps update/delete from touching other people's teams.
	 */
	Optional<Team> findByTeamIdAndOwnerUserId(Long teamId, Long userId);
}

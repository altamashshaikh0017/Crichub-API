package com.as.crichub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Team;
import com.as.crichub.entity.Tournament;
import com.as.crichub.entity.TournamentRegistration;

@Repository
public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {

	List<TournamentRegistration> findByTournament(Tournament tournament);

	List<TournamentRegistration> findByTeam(Team team);

	List<TournamentRegistration> findByTournamentTournamentId(Long tournamentId);

	List<TournamentRegistration> findByTeamTeamId(Long teamId);

	boolean existsByTournamentAndTeam(Tournament tournament, Team team);

	boolean existsByTournamentTournamentIdAndTeamTeamId(Long tournamentId, Long teamId);

}

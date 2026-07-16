package com.as.crichub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Player;
import com.as.crichub.entity.Team;
import com.as.crichub.entity.TeamPlayer;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {

	List<TeamPlayer> findByTeam(Team team);

	List<TeamPlayer> findByPlayer(Player player);

	List<TeamPlayer> findByTeamTeamId(Long teamId);

	List<TeamPlayer> findByPlayerPlayerId(Long playerId);

	boolean existsByTeamAndPlayer(Team team, Player player);

	boolean existsByTeamTeamIdAndPlayerPlayerId(Long teamId, Long playerId);

}

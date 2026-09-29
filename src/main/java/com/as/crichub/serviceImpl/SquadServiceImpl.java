package com.as.crichub.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.AddSquadPlayerReq;
import com.as.crichub.dto.SquadPlayerRes;
import com.as.crichub.entity.Player;
import com.as.crichub.entity.Team;
import com.as.crichub.entity.TeamPlayer;
import com.as.crichub.exception.DataAlreadyExistsException;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.PlayerRepository;
import com.as.crichub.repository.TeamPlayerRepository;
import com.as.crichub.repository.TeamRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.SquadService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SquadServiceImpl implements SquadService {

	private final TeamRepository teamRepository;
	private final PlayerRepository playerRepository;
	private final TeamPlayerRepository teamPlayerRepository;

	public SquadServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository,
			TeamPlayerRepository teamPlayerRepository) {
		this.teamRepository = teamRepository;
		this.playerRepository = playerRepository;
		this.teamPlayerRepository = teamPlayerRepository;
	}

	@Override
	public ResponseBean getSquad(Long userId, Long teamId) {
		log.info("Inside SquadServiceImpl :: getSquad() teamId : {} userId : {}", teamId, userId);
		requireOwnedTeam(userId, teamId);

		List<SquadPlayerRes> squad = teamPlayerRepository.findByTeamTeamIdOrderByTeamPlayerIdAsc(teamId).stream()
				.map(SquadPlayerRes::from).toList();

		return new ResponseBean(true, "Squad fetched successfully", HttpStatus.OK.value(), squad);
	}

	@Override
	@Transactional
	public ResponseBean addPlayer(Long userId, Long teamId, AddSquadPlayerReq request) {
		log.info("Inside SquadServiceImpl :: addPlayer() teamId : {} userId : {}", teamId, userId);
		Team team = requireOwnedTeam(userId, teamId);

		Player player = resolvePlayer(request);

		if (teamPlayerRepository.existsByTeamTeamIdAndPlayerPlayerId(teamId, player.getPlayerId())) {
			throw new DataAlreadyExistsException("Player is already in this squad");
		}

		TeamPlayer teamPlayer = new TeamPlayer();
		teamPlayer.setTeam(team);
		teamPlayer.setPlayer(player);
		teamPlayer.setJerseyNumber(request.getJerseyNumber());

		TeamPlayer saved = teamPlayerRepository.save(teamPlayer);
		log.info("Player {} added to team {} as teamPlayer {}", player.getPlayerId(), teamId,
				saved.getTeamPlayerId());

		return new ResponseBean(true, "Player added to squad", HttpStatus.CREATED.value(),
				SquadPlayerRes.from(saved));
	}

	@Override
	@Transactional
	public ResponseBean removePlayer(Long userId, Long teamId, Long teamPlayerId) {
		log.info("Inside SquadServiceImpl :: removePlayer() teamId : {} teamPlayerId : {}", teamId, teamPlayerId);
		requireOwnedTeam(userId, teamId);

		TeamPlayer teamPlayer = teamPlayerRepository.findByTeamPlayerIdAndTeamTeamId(teamPlayerId, teamId)
				.orElseThrow(() -> new DataNotFoundException("Squad member not found"));

		// Only the roster link is removed; the player record itself stays.
		teamPlayerRepository.delete(teamPlayer);
		return new ResponseBean(true, "Player removed from squad", HttpStatus.OK.value(), null);
	}

	/** Ensures the team exists and belongs to the user, returning it. */
	private Team requireOwnedTeam(Long userId, Long teamId) {
		return teamRepository.findByTeamIdAndOwnerUserId(teamId, userId)
				.orElseThrow(() -> new DataNotFoundException("Team not found"));
	}

	/**
	 * Reuses an existing player when the mobile matches one, otherwise creates a
	 * guest player (no login) from the supplied details.
	 */
	private Player resolvePlayer(AddSquadPlayerReq request) {
		String mobile = blankToNull(request.getMobileNumber());

		if (mobile != null) {
			Optional<Player> existing = playerRepository.findByMobileNumber(mobile);
			if (existing.isPresent()) {
				return existing.get();
			}
		}

		Player player = new Player();
		player.setPlayerName(request.getPlayerName().trim());
		player.setPlayingRole(request.getPlayingRole());
		player.setBattingStyle(blankToNull(request.getBattingStyle()));
		player.setBowlingStyle(blankToNull(request.getBowlingStyle()));
		player.setMobileNumber(mobile);
		return playerRepository.save(player);
	}

	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value.trim();
	}
}

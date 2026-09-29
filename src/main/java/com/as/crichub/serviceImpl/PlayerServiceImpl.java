package com.as.crichub.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.PlayerProfileRes;
import com.as.crichub.dto.PlayerUpdateReq;
import com.as.crichub.entity.Player;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.PlayerRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {

	private final PlayerRepository playerRepository;

	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	@Override
	public ResponseBean getMyProfile(Long userId) {
		log.info("Inside PlayerServiceImpl :: getMyProfile() for userId : {}", userId);

		Player player = playerRepository.findByUserUserId(userId)
				.orElseThrow(() -> new DataNotFoundException("No player profile found for the current user"));

		PlayerProfileRes profile = PlayerProfileRes.from(player);
		return new ResponseBean(true, "Profile fetched successfully", HttpStatus.OK.value(), profile);
	}

	@Override
	@Transactional
	public ResponseBean updateMyProfile(Long userId, PlayerUpdateReq request) {
		log.info("Inside PlayerServiceImpl :: updateMyProfile() for userId : {}", userId);

		Player player = playerRepository.findByUserUserId(userId)
				.orElseThrow(() -> new DataNotFoundException("No player profile found for the current user"));

		player.setPlayerName(request.getPlayerName().trim());
		player.setPlayingRole(request.getPlayingRole());
		player.setBattingStyle(blankToNull(request.getBattingStyle()));
		player.setBowlingStyle(blankToNull(request.getBowlingStyle()));

		Player saved = playerRepository.save(player);
		log.info("Player profile updated for playerId : {}", saved.getPlayerId());

		PlayerProfileRes profile = PlayerProfileRes.from(saved);
		return new ResponseBean(true, "Profile updated successfully", HttpStatus.OK.value(), profile);
	}

	/** Treats an empty / whitespace-only style as "not set". */
	private static String blankToNull(String value) {
		return (value == null || value.isBlank()) ? null : value.trim();
	}
}

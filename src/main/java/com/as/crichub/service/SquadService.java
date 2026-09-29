package com.as.crichub.service;

import com.as.crichub.dto.AddSquadPlayerReq;
import com.as.crichub.response.ResponseBean;

public interface SquadService {

	/** The players in one of the user's teams. */
	ResponseBean getSquad(Long userId, Long teamId);

	/** Adds a player to the squad of one of the user's teams. */
	ResponseBean addPlayer(Long userId, Long teamId, AddSquadPlayerReq request);

	/** Removes a roster entry from one of the user's teams. */
	ResponseBean removePlayer(Long userId, Long teamId, Long teamPlayerId);
}

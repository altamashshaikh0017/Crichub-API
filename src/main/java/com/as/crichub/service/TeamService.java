package com.as.crichub.service;

import com.as.crichub.dto.TeamReq;
import com.as.crichub.response.ResponseBean;

public interface TeamService {

	/** Creates a team owned by the given user. */
	ResponseBean createTeam(Long userId, TeamReq request);

	/** All teams owned by the given user. */
	ResponseBean getMyTeams(Long userId);

	/** A single team owned by the given user. */
	ResponseBean getTeam(Long userId, Long teamId);

	/** Updates one of the user's own teams. */
	ResponseBean updateTeam(Long userId, Long teamId, TeamReq request);

	/** Deletes one of the user's own teams. */
	ResponseBean deleteTeam(Long userId, Long teamId);
}

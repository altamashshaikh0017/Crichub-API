package com.as.crichub.service;

import com.as.crichub.response.ResponseBean;

public interface StatsService {

	/** A player's aggregated career stats. */
	ResponseBean getPlayerStats(Long playerId);

	/** A tournament's top run-scorers and wicket-takers. */
	ResponseBean getTournamentLeaderboard(Long tournamentId);
}

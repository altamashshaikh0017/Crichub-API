package com.as.crichub.service;

import com.as.crichub.response.ResponseBean;

public interface StandingsService {

	/** The points table for a tournament, computed from its completed matches. */
	ResponseBean getStandings(Long tournamentId);
}

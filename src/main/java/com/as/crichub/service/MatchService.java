package com.as.crichub.service;

import com.as.crichub.dto.MatchReq;
import com.as.crichub.dto.MatchResultReq;
import com.as.crichub.response.ResponseBean;

public interface MatchService {

	/** Creates a match (tournament fixture or standalone). */
	ResponseBean createMatch(MatchReq request);

	/** All matches, newest first. */
	ResponseBean getAllMatches();

	/** A single match. */
	ResponseBean getMatch(Long matchId);

	/** The matches belonging to a tournament. */
	ResponseBean getMatchesByTournament(Long tournamentId);

	/** Updates a match's schedule details. */
	ResponseBean updateMatch(Long matchId, MatchReq request);

	/** Records (or updates) a match's toss and result, marking it completed. */
	ResponseBean recordResult(Long matchId, MatchResultReq request);

	/** Deletes a match. */
	ResponseBean deleteMatch(Long matchId);
}

package com.as.crichub.service;

import com.as.crichub.dto.ScorecardReq;
import com.as.crichub.response.ResponseBean;

public interface ScorecardService {

	/** The scorecard for a match, grouped by team. */
	ResponseBean getScorecard(Long matchId);

	/** Replaces a match's scorecard with the supplied performance lines. */
	ResponseBean saveScorecard(Long matchId, ScorecardReq request);
}

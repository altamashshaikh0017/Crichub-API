package com.as.crichub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A match scorecard: the two teams, each with its players' performance lines.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScorecardRes {

	private Long matchId;

	private TeamScorecard teamA;
	private TeamScorecard teamB;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TeamScorecard {
		private Long teamId;
		private String teamName;
		private List<PerformanceRes> performances;
	}
}

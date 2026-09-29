package com.as.crichub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A tournament's leaderboards: top run-scorers and top wicket-takers. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardRes {

	private List<RunScorer> topRunScorers;
	private List<WicketTaker> topWicketTakers;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RunScorer {
		private int rank;
		private Long playerId;
		private String playerName;
		private String teamName;
		private int innings;
		private int runs;
		private Double average;
		private Double strikeRate;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class WicketTaker {
		private int rank;
		private Long playerId;
		private String playerName;
		private String teamName;
		private int innings;
		private int wickets;
		private Double economy;
	}
}

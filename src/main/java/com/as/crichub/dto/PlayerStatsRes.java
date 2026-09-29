package com.as.crichub.dto;

import com.as.crichub.enums.PlayingRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A player's career stats, aggregated from all their match performances. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsRes {

	private Long playerId;
	private String playerName;
	private PlayingRole playingRole;
	private int matches;

	private Batting batting;
	private Bowling bowling;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Batting {
		private int innings;
		private int runs;
		private int ballsFaced;
		private int notOuts;
		private int fours;
		private int sixes;
		private int fifties;
		private int hundreds;
		private Integer highestScore;
		private Double average;
		private Double strikeRate;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Bowling {
		private int innings;
		private String overs; // e.g. "12.4"
		private int runsConceded;
		private int wickets;
		private int maidens;
		private Double average;
		private Double economy;
		private String bestBowling; // e.g. "3/22"
	}
}

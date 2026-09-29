package com.as.crichub.dto;

import com.as.crichub.entity.Player;
import com.as.crichub.entity.PlayerMatchPerformance;
import com.as.crichub.enums.PlayingRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** One player's scorecard line as sent to the client. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRes {

	private Long performanceId;
	private Long playerId;
	private String playerName;
	private PlayingRole playingRole;

	private Integer runs;
	private Integer ballsFaced;
	private Integer fours;
	private Integer sixes;
	private Boolean notOut;

	private Double oversBowled;
	private Integer runsConceded;
	private Integer wickets;
	private Integer maidens;

	public static PerformanceRes from(PlayerMatchPerformance p) {
		Player player = p.getPlayer();
		return new PerformanceRes(
				p.getPerformanceId(),
				player.getPlayerId(),
				player.getPlayerName(),
				player.getPlayingRole(),
				p.getRuns(),
				p.getBallsFaced(),
				p.getFours(),
				p.getSixes(),
				p.getNotOut(),
				p.getOversBowled(),
				p.getRunsConceded(),
				p.getWickets(),
				p.getMaidens());
	}
}

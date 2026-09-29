package com.as.crichub.dto;

import com.as.crichub.entity.Player;
import com.as.crichub.entity.TeamPlayer;
import com.as.crichub.enums.PlayingRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One member of a team's squad — the roster entry ({@code teamPlayerId}) plus a
 * flat view of the player. {@code guest} marks players with no login account.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SquadPlayerRes {

	private Long teamPlayerId;

	private Long playerId;

	private String playerName;

	private PlayingRole playingRole;

	private String battingStyle;

	private String bowlingStyle;

	private String mobileNumber;

	private Integer jerseyNumber;

	private boolean guest;

	public static SquadPlayerRes from(TeamPlayer teamPlayer) {
		Player player = teamPlayer.getPlayer();
		return new SquadPlayerRes(teamPlayer.getTeamPlayerId(), player.getPlayerId(), player.getPlayerName(),
				player.getPlayingRole(), player.getBattingStyle(), player.getBowlingStyle(),
				player.getMobileNumber(), teamPlayer.getJerseyNumber(), player.getUser() == null);
	}
}

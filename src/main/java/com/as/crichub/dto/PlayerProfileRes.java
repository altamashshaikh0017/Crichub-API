package com.as.crichub.dto;

import com.as.crichub.entity.Player;
import com.as.crichub.entity.User;
import com.as.crichub.enums.PlayingRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The player profile as sent to the client. Deliberately a DTO rather than the
 * {@link Player} entity: the entity links back to a {@link User} that carries
 * the password hash, which must never be serialised.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfileRes {

	private Long playerId;

	private String playerName;

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNumber;

	private PlayingRole playingRole;

	private String battingStyle;

	private String bowlingStyle;

	/** Maps the entity graph to a safe, flat profile. */
	public static PlayerProfileRes from(Player player) {
		PlayerProfileRes res = new PlayerProfileRes();
		res.setPlayerId(player.getPlayerId());
		res.setPlayerName(player.getPlayerName());
		res.setMobileNumber(player.getMobileNumber());
		res.setPlayingRole(player.getPlayingRole());
		res.setBattingStyle(player.getBattingStyle());
		res.setBowlingStyle(player.getBowlingStyle());

		User user = player.getUser();
		if (user != null) {
			res.setFirstName(user.getFirstName());
			res.setLastName(user.getLastName());
			res.setEmail(user.getEmail());
		}
		return res;
	}
}

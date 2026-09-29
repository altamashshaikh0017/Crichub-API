package com.as.crichub.dto;

import com.as.crichub.enums.PlayingRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Editable fields of a player profile. Scoped to the {@code Player} entity only
 * — account details (email, name on the login, password) are not touched here.
 */
@Data
public class PlayerUpdateReq {

	@NotBlank(message = "Player name is required")
	private String playerName;

	@NotNull(message = "Playing role is required")
	private PlayingRole playingRole;

	private String battingStyle;

	private String bowlingStyle;
}

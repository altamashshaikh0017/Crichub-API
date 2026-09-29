package com.as.crichub.dto;

import com.as.crichub.enums.PlayingRole;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Adds a player to a squad. If {@code mobileNumber} matches an existing player,
 * that player is linked; otherwise a guest player is created from these details.
 */
@Data
public class AddSquadPlayerReq {

	@NotBlank(message = "Player name is required")
	private String playerName;

	@NotNull(message = "Playing role is required")
	private PlayingRole playingRole;

	private String battingStyle;

	private String bowlingStyle;

	// Optional. @Pattern allows null; the client sends null (not "") when blank.
	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	@Min(value = 0, message = "Jersey number cannot be negative")
	@Max(value = 999, message = "Jersey number is too large")
	private Integer jerseyNumber;
}

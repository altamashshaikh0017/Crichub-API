package com.as.crichub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Create/update payload for a team. Contact number follows the same 10-digit
 * rule as a player's mobile.
 */
@Data
public class TeamReq {

	@NotBlank(message = "Team name is required")
	private String teamName;

	@NotBlank(message = "Captain name is required")
	private String captainName;

	@NotBlank(message = "Contact number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
	private String contactNumber;
}

package com.as.crichub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Payload for registering an existing team into a tournament. */
@Data
public class RegisterTeamReq {

	@NotNull(message = "Team id is required")
	private Long teamId;
}

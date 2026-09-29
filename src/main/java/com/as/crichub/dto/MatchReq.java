package com.as.crichub.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Create/update payload for a match's schedule. {@code tournamentId} is optional
 * (a standalone match has none); {@code overs} may be omitted for a tournament
 * match, in which case the tournament's overs are used.
 */
@Data
public class MatchReq {

	private Long tournamentId;

	@NotNull(message = "Team A is required")
	private Long teamAId;

	@NotNull(message = "Team B is required")
	private Long teamBId;

	@NotNull(message = "Match date is required")
	private LocalDate matchDate;

	@NotBlank(message = "Venue is required")
	private String venue;

	@Positive(message = "Overs must be positive")
	private Integer overs;
}

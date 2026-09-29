package com.as.crichub.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Create/update payload for a tournament. The {@code endDate}-not-before-
 * {@code startDate} rule spans two fields, so it is checked in the service
 * rather than with a single-field annotation.
 */
@Data
public class TournamentReq {

	@NotBlank(message = "Tournament name is required")
	private String tournamentName;

	@NotBlank(message = "Location is required")
	private String location;

	@NotNull(message = "Overs is required")
	@Positive(message = "Overs must be positive")
	private Integer overs;

	@NotNull(message = "Start date is required")
	private LocalDate startDate;

	@NotNull(message = "End date is required")
	private LocalDate endDate;

	@NotBlank(message = "Ball type is required")
	private String ballType;

	@NotNull(message = "Max teams is required")
	@Positive(message = "Max teams must be positive")
	private Integer maxTeams;
}

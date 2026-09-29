package com.as.crichub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/** One player's line on a scorecard being saved. Stat fields are all optional. */
@Data
public class PerformanceReq {

	@NotNull(message = "Player is required")
	private Long playerId;

	@NotNull(message = "Team is required")
	private Long teamId;

	// ── Batting ─────────────────────────────────────────────────────────────
	@PositiveOrZero(message = "Runs cannot be negative")
	private Integer runs;

	@PositiveOrZero(message = "Balls faced cannot be negative")
	private Integer ballsFaced;

	@PositiveOrZero(message = "Fours cannot be negative")
	private Integer fours;

	@PositiveOrZero(message = "Sixes cannot be negative")
	private Integer sixes;

	private Boolean notOut;

	// ── Bowling ─────────────────────────────────────────────────────────────
	@PositiveOrZero(message = "Overs bowled cannot be negative")
	private Double oversBowled;

	@PositiveOrZero(message = "Runs conceded cannot be negative")
	private Integer runsConceded;

	@Min(value = 0, message = "Wickets must be between 0 and 10")
	@Max(value = 10, message = "Wickets must be between 0 and 10")
	private Integer wickets;

	@PositiveOrZero(message = "Maidens cannot be negative")
	private Integer maidens;
}

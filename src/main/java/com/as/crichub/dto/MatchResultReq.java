package com.as.crichub.dto;

import com.as.crichub.enums.ResultType;
import com.as.crichub.enums.TossDecision;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * Payload for recording a match result. Cross-field rules (winner required only
 * when {@code resultType} is WON, toss decision required when a toss winner is
 * set, etc.) are enforced in the service.
 */
@Data
public class MatchResultReq {

	// ── Toss (optional) ───────────────────────────────────────────────────────
	private Long tossWinnerId;

	private TossDecision tossDecision;

	// ── Scores ────────────────────────────────────────────────────────────────
	@PositiveOrZero(message = "Team A runs cannot be negative")
	private Integer teamARuns;

	@Min(value = 0, message = "Team A wickets must be between 0 and 10")
	@Max(value = 10, message = "Team A wickets must be between 0 and 10")
	private Integer teamAWickets;

	@PositiveOrZero(message = "Team A overs cannot be negative")
	private Double teamAOvers;

	@PositiveOrZero(message = "Team B runs cannot be negative")
	private Integer teamBRuns;

	@Min(value = 0, message = "Team B wickets must be between 0 and 10")
	@Max(value = 10, message = "Team B wickets must be between 0 and 10")
	private Integer teamBWickets;

	@PositiveOrZero(message = "Team B overs cannot be negative")
	private Double teamBOvers;

	// ── Outcome ───────────────────────────────────────────────────────────────
	@NotNull(message = "Result type is required")
	private ResultType resultType;

	/** Required only when resultType is WON; must be one of the two teams. */
	private Long winnerId;

	private String resultSummary;
}

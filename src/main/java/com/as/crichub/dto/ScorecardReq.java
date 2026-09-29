package com.as.crichub.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** The full set of scorecard lines for a match, saved in one request. */
@Data
public class ScorecardReq {

	@NotNull(message = "Performances are required")
	@Valid
	private List<PerformanceReq> performances;
}

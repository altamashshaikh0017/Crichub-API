package com.as.crichub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One row of a tournament points table, computed from completed matches. Net
 * run rate is null when a team has no matches with score data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandingRes {

	private int rank;

	private Long teamId;
	private String teamName;

	private int played;
	private int won;
	private int lost;
	private int tied;
	private int noResult;
	private int points;

	private Double netRunRate;
}

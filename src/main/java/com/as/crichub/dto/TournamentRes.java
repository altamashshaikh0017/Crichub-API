package com.as.crichub.dto;

import java.time.LocalDate;

import com.as.crichub.entity.Tournament;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A tournament as sent to the client, including how many teams are currently
 * registered so callers can show capacity without a second request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRes {

	private Long tournamentId;

	private String tournamentName;

	private String location;

	private Integer overs;

	private LocalDate startDate;

	private LocalDate endDate;

	private String ballType;

	private Integer maxTeams;

	private long registeredTeamsCount;

	public static TournamentRes from(Tournament tournament, long registeredTeamsCount) {
		return new TournamentRes(tournament.getTournamentId(), tournament.getTournamentName(),
				tournament.getLocation(), tournament.getOvers(), tournament.getStartDate(),
				tournament.getEndDate(), tournament.getBallType(), tournament.getMaxTeams(),
				registeredTeamsCount);
	}
}

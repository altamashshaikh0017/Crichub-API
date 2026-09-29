package com.as.crichub.dto;

import java.time.LocalDate;

import com.as.crichub.entity.TournamentRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A team's registration in a tournament as sent to the client. Carries the
 * {@code registrationId} (needed to unregister) alongside the team summary.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentTeamRes {

	private Long registrationId;

	private Long teamId;

	private String teamName;

	private LocalDate registeredDate;

	public static TournamentTeamRes from(TournamentRegistration registration) {
		return new TournamentTeamRes(registration.getRegistrationId(), registration.getTeam().getTeamId(),
				registration.getTeam().getTeamName(), registration.getRegisteredDate());
	}
}

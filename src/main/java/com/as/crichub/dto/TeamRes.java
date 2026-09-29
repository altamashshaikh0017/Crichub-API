package com.as.crichub.dto;

import com.as.crichub.entity.Team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A team as sent to the client. A DTO rather than the entity so the owning
 * {@code User} (and its password hash) is never serialised.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRes {

	private Long teamId;

	private String teamName;

	private String captainName;

	private String contactNumber;

	public static TeamRes from(Team team) {
		return new TeamRes(team.getTeamId(), team.getTeamName(), team.getCaptainName(),
				team.getContactNumber());
	}
}

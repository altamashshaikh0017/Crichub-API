package com.as.crichub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Squad/roster join between a {@link Team} and a {@link Player}. A player can
 * belong to many teams (across tournaments/seasons) and a team has many players.
 */
@Entity
@Table(name = "team_players", uniqueConstraints = @UniqueConstraint(columnNames = { "team_id", "player_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long teamPlayerId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_id")
	private Team team;

	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;

	private Integer jerseyNumber;
}

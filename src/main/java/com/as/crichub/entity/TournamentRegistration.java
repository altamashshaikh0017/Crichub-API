package com.as.crichub.entity;

import java.time.LocalDate;

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
 * Join between a {@link Tournament} and a {@link Team}. A team can be reused
 * across many tournaments, and a tournament has many registered teams.
 */
@Entity
@Table(name = "tournament_registrations", uniqueConstraints = @UniqueConstraint(columnNames = { "tournament_id",
		"team_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long registrationId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "tournament_id")
	private Tournament tournament;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_id")
	private Team team;

	private LocalDate registeredDate;
}

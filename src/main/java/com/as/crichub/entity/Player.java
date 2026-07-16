package com.as.crichub.entity;

import com.as.crichub.enums.PlayingRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long playerId;

	private String playerName;

	private String mobileNumber;

	@Enumerated(EnumType.STRING)
	private PlayingRole playingRole;

	private String battingStyle;

	private String bowlingStyle;

	/**
	 * Optional link to a login account. Null for guest players who have no
	 * registered {@link User}.
	 */
	@OneToOne
	@JoinColumn(name = "user_id", unique = true)
	private User user;
}

package com.as.crichub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One player's batting and bowling figures in a single match — a line on the
 * scorecard. Batting fields are null for a player who only bowled and vice
 * versa. {@link #team} records which side the player turned out for.
 */
@Entity
@Table(name = "player_match_performances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerMatchPerformance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long performanceId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "match_id")
	private Match match;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_id")
	private Team team;

	@ManyToOne(optional = false)
	@JoinColumn(name = "player_id")
	private Player player;

	// ── Batting ─────────────────────────────────────────────────────────────
	private Integer runs;
	private Integer ballsFaced;
	private Integer fours;
	private Integer sixes;
	private Boolean notOut;

	// ── Bowling ─────────────────────────────────────────────────────────────
	private Double oversBowled;
	private Integer runsConceded;
	private Integer wickets;
	private Integer maidens;
}

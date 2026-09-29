package com.as.crichub.entity;

import java.time.LocalDate;

import com.as.crichub.enums.MatchStatus;
import com.as.crichub.enums.ResultType;
import com.as.crichub.enums.TossDecision;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * A single game between two teams. The tournament is optional so a match can be
 * either a tournament fixture or a standalone/friendly game. Schedule fields are
 * set on creation; toss and result fields are filled in when the result is
 * recorded.
 */
@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long matchId;

	/** Null for a standalone match; otherwise the tournament this fixture belongs to. */
	@ManyToOne
	@JoinColumn(name = "tournament_id")
	private Tournament tournament;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_a_id")
	private Team teamA;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_b_id")
	private Team teamB;

	private LocalDate matchDate;

	private String venue;

	/** Overs per innings for this match. */
	private Integer overs;

	@Enumerated(EnumType.STRING)
	private MatchStatus status;

	// ── Toss ────────────────────────────────────────────────────────────────
	@ManyToOne
	@JoinColumn(name = "toss_winner_id")
	private Team tossWinner;

	@Enumerated(EnumType.STRING)
	private TossDecision tossDecision;

	// ── Result ──────────────────────────────────────────────────────────────
	private Integer teamARuns;
	private Integer teamAWickets;
	private Double teamAOvers;

	private Integer teamBRuns;
	private Integer teamBWickets;
	private Double teamBOvers;

	@Enumerated(EnumType.STRING)
	private ResultType resultType;

	/** The winning team; null for a tie, no-result or abandoned match. */
	@ManyToOne
	@JoinColumn(name = "winner_id")
	private Team winner;

	private String resultSummary;
}

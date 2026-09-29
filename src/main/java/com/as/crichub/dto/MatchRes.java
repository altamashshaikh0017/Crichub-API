package com.as.crichub.dto;

import java.time.LocalDate;

import com.as.crichub.entity.Match;
import com.as.crichub.entity.Team;
import com.as.crichub.entity.Tournament;
import com.as.crichub.enums.MatchStatus;
import com.as.crichub.enums.ResultType;
import com.as.crichub.enums.TossDecision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A match as sent to the client. Flattened (id + name pairs for related teams
 * and the optional tournament) so the entity graph — and any lazy relations —
 * is never serialised directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRes {

	private Long matchId;

	private Long tournamentId;
	private String tournamentName;

	private Long teamAId;
	private String teamAName;
	private Long teamBId;
	private String teamBName;

	private LocalDate matchDate;
	private String venue;
	private Integer overs;
	private MatchStatus status;

	private Long tossWinnerId;
	private String tossWinnerName;
	private TossDecision tossDecision;

	private Integer teamARuns;
	private Integer teamAWickets;
	private Double teamAOvers;
	private Integer teamBRuns;
	private Integer teamBWickets;
	private Double teamBOvers;

	private ResultType resultType;
	private Long winnerId;
	private String winnerName;
	private String resultSummary;

	public static MatchRes from(Match match) {
		Tournament tournament = match.getTournament();
		Team tossWinner = match.getTossWinner();
		Team winner = match.getWinner();

		return new MatchRes(
				match.getMatchId(),
				tournament != null ? tournament.getTournamentId() : null,
				tournament != null ? tournament.getTournamentName() : null,
				match.getTeamA().getTeamId(),
				match.getTeamA().getTeamName(),
				match.getTeamB().getTeamId(),
				match.getTeamB().getTeamName(),
				match.getMatchDate(),
				match.getVenue(),
				match.getOvers(),
				match.getStatus(),
				tossWinner != null ? tossWinner.getTeamId() : null,
				tossWinner != null ? tossWinner.getTeamName() : null,
				match.getTossDecision(),
				match.getTeamARuns(),
				match.getTeamAWickets(),
				match.getTeamAOvers(),
				match.getTeamBRuns(),
				match.getTeamBWickets(),
				match.getTeamBOvers(),
				match.getResultType(),
				winner != null ? winner.getTeamId() : null,
				winner != null ? winner.getTeamName() : null,
				match.getResultSummary());
	}
}

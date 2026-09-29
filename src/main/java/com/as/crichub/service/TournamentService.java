package com.as.crichub.service;

import com.as.crichub.dto.RegisterTeamReq;
import com.as.crichub.dto.TournamentReq;
import com.as.crichub.response.ResponseBean;

public interface TournamentService {

	/** Creates a tournament. */
	ResponseBean createTournament(TournamentReq request);

	/** All tournaments, newest first. */
	ResponseBean getAllTournaments();

	/** A single tournament. */
	ResponseBean getTournament(Long tournamentId);

	/** Updates a tournament. */
	ResponseBean updateTournament(Long tournamentId, TournamentReq request);

	/** Deletes a tournament along with its team registrations. */
	ResponseBean deleteTournament(Long tournamentId);

	/** The teams registered for a tournament. */
	ResponseBean getRegisteredTeams(Long tournamentId);

	/** Registers an existing team into a tournament. */
	ResponseBean registerTeam(Long tournamentId, RegisterTeamReq request);

	/** Removes a team's registration from a tournament. */
	ResponseBean unregisterTeam(Long tournamentId, Long registrationId);
}

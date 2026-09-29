package com.as.crichub.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.RegisterTeamReq;
import com.as.crichub.dto.TournamentReq;
import com.as.crichub.dto.TournamentRes;
import com.as.crichub.dto.TournamentTeamRes;
import com.as.crichub.entity.Team;
import com.as.crichub.entity.Tournament;
import com.as.crichub.entity.TournamentRegistration;
import com.as.crichub.exception.BadRequestException;
import com.as.crichub.exception.DataAlreadyExistsException;
import com.as.crichub.exception.DataConflictException;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.TeamRepository;
import com.as.crichub.repository.TournamentRegistrationRepository;
import com.as.crichub.repository.TournamentRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.TournamentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TournamentServiceImpl implements TournamentService {

	private final TournamentRepository tournamentRepository;
	private final TournamentRegistrationRepository registrationRepository;
	private final TeamRepository teamRepository;

	public TournamentServiceImpl(TournamentRepository tournamentRepository,
			TournamentRegistrationRepository registrationRepository, TeamRepository teamRepository) {
		this.tournamentRepository = tournamentRepository;
		this.registrationRepository = registrationRepository;
		this.teamRepository = teamRepository;
	}

	@Override
	@Transactional
	public ResponseBean createTournament(TournamentReq request) {
		log.info("Inside TournamentServiceImpl :: createTournament()");

		Tournament tournament = new Tournament();
		applyFields(tournament, request);

		Tournament saved = tournamentRepository.save(tournament);
		log.info("Tournament created with id : {}", saved.getTournamentId());

		return new ResponseBean(true, "Tournament created successfully", HttpStatus.CREATED.value(),
				TournamentRes.from(saved, 0));
	}

	@Override
	public ResponseBean getAllTournaments() {
		log.info("Inside TournamentServiceImpl :: getAllTournaments()");

		List<TournamentRes> tournaments = tournamentRepository.findAllByOrderByTournamentIdDesc().stream()
				.map(t -> TournamentRes.from(t,
						registrationRepository.countByTournamentTournamentId(t.getTournamentId())))
				.toList();

		return new ResponseBean(true, "Tournaments fetched successfully", HttpStatus.OK.value(), tournaments);
	}

	@Override
	public ResponseBean getTournament(Long tournamentId) {
		log.info("Inside TournamentServiceImpl :: getTournament() tournamentId : {}", tournamentId);

		Tournament tournament = requireTournament(tournamentId);
		long count = registrationRepository.countByTournamentTournamentId(tournamentId);

		return new ResponseBean(true, "Tournament fetched successfully", HttpStatus.OK.value(),
				TournamentRes.from(tournament, count));
	}

	@Override
	@Transactional
	public ResponseBean updateTournament(Long tournamentId, TournamentReq request) {
		log.info("Inside TournamentServiceImpl :: updateTournament() tournamentId : {}", tournamentId);

		Tournament tournament = requireTournament(tournamentId);
		applyFields(tournament, request);

		Tournament saved = tournamentRepository.save(tournament);
		long count = registrationRepository.countByTournamentTournamentId(tournamentId);

		return new ResponseBean(true, "Tournament updated successfully", HttpStatus.OK.value(),
				TournamentRes.from(saved, count));
	}

	@Override
	@Transactional
	public ResponseBean deleteTournament(Long tournamentId) {
		log.info("Inside TournamentServiceImpl :: deleteTournament() tournamentId : {}", tournamentId);

		Tournament tournament = requireTournament(tournamentId);

		// Remove the join rows first so the tournament delete doesn't hit the FK.
		List<TournamentRegistration> registrations = registrationRepository
				.findByTournamentTournamentId(tournamentId);
		if (!registrations.isEmpty()) {
			registrationRepository.deleteAll(registrations);
		}

		tournamentRepository.delete(tournament);
		return new ResponseBean(true, "Tournament deleted successfully", HttpStatus.OK.value(), null);
	}

	@Override
	public ResponseBean getRegisteredTeams(Long tournamentId) {
		log.info("Inside TournamentServiceImpl :: getRegisteredTeams() tournamentId : {}", tournamentId);
		requireTournament(tournamentId);

		List<TournamentTeamRes> teams = registrationRepository.findByTournamentTournamentId(tournamentId).stream()
				.map(TournamentTeamRes::from).toList();

		return new ResponseBean(true, "Registered teams fetched successfully", HttpStatus.OK.value(), teams);
	}

	@Override
	@Transactional
	public ResponseBean registerTeam(Long tournamentId, RegisterTeamReq request) {
		log.info("Inside TournamentServiceImpl :: registerTeam() tournamentId : {} teamId : {}", tournamentId,
				request.getTeamId());

		Tournament tournament = requireTournament(tournamentId);
		Team team = teamRepository.findById(request.getTeamId())
				.orElseThrow(() -> new DataNotFoundException("Team not found"));

		if (registrationRepository.existsByTournamentTournamentIdAndTeamTeamId(tournamentId, team.getTeamId())) {
			throw new DataAlreadyExistsException("Team is already registered for this tournament");
		}

		long registered = registrationRepository.countByTournamentTournamentId(tournamentId);
		if (registered >= tournament.getMaxTeams()) {
			throw new DataConflictException("Tournament is full");
		}

		TournamentRegistration registration = new TournamentRegistration();
		registration.setTournament(tournament);
		registration.setTeam(team);
		registration.setRegisteredDate(LocalDate.now());

		TournamentRegistration saved = registrationRepository.save(registration);
		log.info("Team {} registered for tournament {} as registration {}", team.getTeamId(), tournamentId,
				saved.getRegistrationId());

		return new ResponseBean(true, "Team registered successfully", HttpStatus.CREATED.value(),
				TournamentTeamRes.from(saved));
	}

	@Override
	@Transactional
	public ResponseBean unregisterTeam(Long tournamentId, Long registrationId) {
		log.info("Inside TournamentServiceImpl :: unregisterTeam() tournamentId : {} registrationId : {}",
				tournamentId, registrationId);
		requireTournament(tournamentId);

		TournamentRegistration registration = registrationRepository
				.findByRegistrationIdAndTournamentTournamentId(registrationId, tournamentId)
				.orElseThrow(() -> new DataNotFoundException("Registration not found"));

		// Only the registration is removed; the team itself stays.
		registrationRepository.delete(registration);
		return new ResponseBean(true, "Team unregistered successfully", HttpStatus.OK.value(), null);
	}

	/** Fetches a tournament or fails with a 404-mapped exception. */
	private Tournament requireTournament(Long tournamentId) {
		return tournamentRepository.findById(tournamentId)
				.orElseThrow(() -> new DataNotFoundException("Tournament not found"));
	}

	/**
	 * Copies request fields onto the entity for both create and update, after
	 * checking the two-field date rule.
	 */
	private void applyFields(Tournament tournament, TournamentReq request) {
		if (request.getEndDate().isBefore(request.getStartDate())) {
			throw new BadRequestException("End date cannot be before start date");
		}

		tournament.setTournamentName(request.getTournamentName().trim());
		tournament.setLocation(request.getLocation().trim());
		tournament.setOvers(request.getOvers());
		tournament.setStartDate(request.getStartDate());
		tournament.setEndDate(request.getEndDate());
		tournament.setBallType(request.getBallType().trim());
		tournament.setMaxTeams(request.getMaxTeams());
	}
}

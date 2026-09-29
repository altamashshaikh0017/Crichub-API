package com.as.crichub.serviceImpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.TeamReq;
import com.as.crichub.dto.TeamRes;
import com.as.crichub.entity.Team;
import com.as.crichub.exception.DataNotFoundException;
import com.as.crichub.repository.TeamRepository;
import com.as.crichub.repository.UserRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.service.TeamService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

	private final TeamRepository teamRepository;
	private final UserRepository userRepository;

	public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public ResponseBean createTeam(Long userId, TeamReq request) {
		log.info("Inside TeamServiceImpl :: createTeam() for userId : {}", userId);

		Team team = new Team();
		team.setTeamName(request.getTeamName().trim());
		team.setCaptainName(request.getCaptainName().trim());
		team.setContactNumber(request.getContactNumber().trim());
		// A managed proxy is enough to set the owner FK without loading the user.
		team.setOwner(userRepository.getReferenceById(userId));

		Team saved = teamRepository.save(team);
		log.info("Team created with id : {}", saved.getTeamId());

		return new ResponseBean(true, "Team created successfully", HttpStatus.CREATED.value(), TeamRes.from(saved));
	}

	@Override
	public ResponseBean getMyTeams(Long userId) {
		log.info("Inside TeamServiceImpl :: getMyTeams() for userId : {}", userId);

		List<TeamRes> teams = teamRepository.findByOwnerUserIdOrderByTeamIdDesc(userId).stream()
				.map(TeamRes::from).toList();

		return new ResponseBean(true, "Teams fetched successfully", HttpStatus.OK.value(), teams);
	}

	@Override
	public ResponseBean getTeam(Long userId, Long teamId) {
		log.info("Inside TeamServiceImpl :: getTeam() teamId : {} userId : {}", teamId, userId);

		Team team = teamRepository.findByTeamIdAndOwnerUserId(teamId, userId)
				.orElseThrow(() -> new DataNotFoundException("Team not found"));

		return new ResponseBean(true, "Team fetched successfully", HttpStatus.OK.value(), TeamRes.from(team));
	}

	@Override
	@Transactional
	public ResponseBean updateTeam(Long userId, Long teamId, TeamReq request) {
		log.info("Inside TeamServiceImpl :: updateTeam() teamId : {} userId : {}", teamId, userId);

		Team team = teamRepository.findByTeamIdAndOwnerUserId(teamId, userId)
				.orElseThrow(() -> new DataNotFoundException("Team not found"));

		team.setTeamName(request.getTeamName().trim());
		team.setCaptainName(request.getCaptainName().trim());
		team.setContactNumber(request.getContactNumber().trim());

		Team saved = teamRepository.save(team);
		return new ResponseBean(true, "Team updated successfully", HttpStatus.OK.value(), TeamRes.from(saved));
	}

	@Override
	@Transactional
	public ResponseBean deleteTeam(Long userId, Long teamId) {
		log.info("Inside TeamServiceImpl :: deleteTeam() teamId : {} userId : {}", teamId, userId);

		Team team = teamRepository.findByTeamIdAndOwnerUserId(teamId, userId)
				.orElseThrow(() -> new DataNotFoundException("Team not found"));

		teamRepository.delete(team);
		return new ResponseBean(true, "Team deleted successfully", HttpStatus.OK.value(), null);
	}
}

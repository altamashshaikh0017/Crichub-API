package com.as.crichub.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.as.crichub.dto.LoginReq;
import com.as.crichub.dto.LoginRes;
import com.as.crichub.dto.UserRegisterReq;
import com.as.crichub.entity.Player;
import com.as.crichub.entity.Role;
import com.as.crichub.entity.User;
import com.as.crichub.exception.DataAlreadyExistsException;
import com.as.crichub.exception.InvalidCredentialsException;
import com.as.crichub.exception.ResourceNotFoundException;
import com.as.crichub.repository.PlayerRepository;
import com.as.crichub.repository.RoleRepository;
import com.as.crichub.repository.UserRepository;
import com.as.crichub.response.ResponseBean;
import com.as.crichub.security.JwtUtil;
import com.as.crichub.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private static final String DEFAULT_ROLE = "USER";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	@Transactional
	public ResponseBean registerUser(UserRegisterReq request) {
		log.info("Inside UserServiceImpl :: registerUser()");

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new DataAlreadyExistsException("User already exists with email : " + request.getEmail());
		}

		Role role = roleRepository.findByRoleName(DEFAULT_ROLE)
				.orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", DEFAULT_ROLE));

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setMobileNumber(request.getMobileNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(role);

		User savedUser = userRepository.save(user);
		log.info("User registered successfully with id : {}", savedUser.getUserId());

		Player player = new Player();
		player.setUser(savedUser);
		player.setPlayerName(request.getFirstName() + " " + request.getLastName());
		player.setMobileNumber(request.getMobileNumber());
		player.setPlayingRole(request.getPlayingRole());
		player.setBattingStyle(request.getBattingStyle());
		player.setBowlingStyle(request.getBowlingStyle());

		Player savedPlayer = playerRepository.save(player);
		log.info("Player profile created successfully with id : {}", savedPlayer.getPlayerId());

		return new ResponseBean(true, "User registered successfully", HttpStatus.CREATED.value(),
				savedUser.getUserId());
	}

	@Override
	public ResponseBean login(LoginReq request) {
		log.info("Inside UserServiceImpl :: login()");

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

		String token = jwtUtil.generateTokenUsingId(user.getEmail(), String.valueOf(user.getUserId()));
		log.info("User logged in successfully with id : {}", user.getUserId());

		LoginRes loginRes = new LoginRes(token, "Bearer", user.getUserId(), user.getEmail());
		return new ResponseBean(true, "Login successful", HttpStatus.OK.value(), loginRes);
	}

}

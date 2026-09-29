package com.as.crichub.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.as.crichub.entity.Role;
import com.as.crichub.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Seeds baseline reference data the app needs to function. Runs on every
 * startup and is idempotent, so a fresh database (e.g. a new Railway MySQL)
 * gets the default role signup depends on without a manual SQL step.
 */
@Configuration
@Slf4j
public class DataSeeder {

	/** Must match {@code UserServiceImpl.DEFAULT_ROLE}. */
	private static final String DEFAULT_ROLE = "USER";

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByRoleName(DEFAULT_ROLE).isEmpty()) {
				Role role = new Role();
				role.setRoleName(DEFAULT_ROLE);
				roleRepository.save(role);
				log.info("Seeded default role '{}'", DEFAULT_ROLE);
			}
		};
	}
}

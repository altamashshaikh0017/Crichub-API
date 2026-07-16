package com.as.crichub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.as.crichub.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	Optional<Player> findByUserUserId(Long userId);

	Optional<Player> findByMobileNumber(String mobileNumber);

}

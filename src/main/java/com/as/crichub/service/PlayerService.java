package com.as.crichub.service;

import com.as.crichub.dto.PlayerUpdateReq;
import com.as.crichub.response.ResponseBean;

public interface PlayerService {

	/**
	 * The player profile belonging to the given user id (taken from the JWT).
	 */
	ResponseBean getMyProfile(Long userId);

	/**
	 * Updates the editable fields of the given user's player profile.
	 */
	ResponseBean updateMyProfile(Long userId, PlayerUpdateReq request);
}

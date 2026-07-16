package com.as.crichub.service;

import com.as.crichub.dto.LoginReq;
import com.as.crichub.dto.UserRegisterReq;
import com.as.crichub.response.ResponseBean;

public interface UserService {

	ResponseBean registerUser(UserRegisterReq request);

	ResponseBean login(LoginReq request);
}

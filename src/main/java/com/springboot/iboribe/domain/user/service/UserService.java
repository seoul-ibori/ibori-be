package com.springboot.iboribe.domain.user.service;

import com.springboot.iboribe.domain.user.dto.response.UserInfoResponse;

public interface UserService {

  UserInfoResponse getUserInfo(Long userId);
}

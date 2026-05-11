package com.springboot.iboribe.domain.user.service;

import java.util.List;

import com.springboot.iboribe.domain.user.dto.response.UserInfoResponse;

public interface UserService {

  UserInfoResponse getUserInfo(Long userId);

  List<UserInfoResponse> getAllUsers();

  void deleteUser(Long userId);
}

package com.springboot.iboribe.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.user.dto.response.UserInfoResponse;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserInfoResponse getUserInfo(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    return UserInfoResponse.from(user);
  }
}

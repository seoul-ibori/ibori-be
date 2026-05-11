package com.springboot.iboribe.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.repository.FamilyRepository;
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
  private final FamilyRepository familyRepository;

  @Override
  public UserInfoResponse getUserInfo(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    return UserInfoResponse.from(user);
  }

  @Override
  public List<UserInfoResponse> getAllUsers() {
    return userRepository.findAll().stream().map(UserInfoResponse::from).toList();
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    Family family = user.getFamily();
    userRepository.delete(user);

    boolean familyEmpty = userRepository.findAllByFamily(family).isEmpty();
    if (familyEmpty) {
      familyRepository.delete(family);
    }
  }
}

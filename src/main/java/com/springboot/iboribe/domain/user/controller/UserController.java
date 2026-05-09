package com.springboot.iboribe.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.user.dto.response.UserInfoResponse;
import com.springboot.iboribe.domain.user.service.UserService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.exception.CustomException;
import com.springboot.iboribe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "[토큰 O] 내 정보 조회",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Returns**  \n
          userId  \n
          name  \n
          username  \n
          familyId  \n
          familyCode  \n
          familyRole  \n
          """)
  @GetMapping("/users/me")
  public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    if (userDetails == null) {
      throw new CustomException(AuthErrorCode.AUTHENTICATION_REQUIRED);
    }
    UserInfoResponse response = userService.getUserInfo(userDetails.getUserId());
    return ResponseEntity.ok(BaseResponse.success(200, "사용자 정보 조회 성공", response));
  }
}

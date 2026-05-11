package com.springboot.iboribe.domain.user.controller;

import java.util.List;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

  @SecurityRequirements
  @Operation(summary = "[토큰 X] 전체 사용자 조회", description = "관리자용 전체 사용자 목록 조회")
  @GetMapping("/admin/users")
  public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getAllUsers() {
    List<UserInfoResponse> response = userService.getAllUsers();
    return ResponseEntity.ok(BaseResponse.success(200, "전체 사용자 조회 성공", response));
  }

  @SecurityRequirements
  @Operation(summary = "[토큰 X] 사용자 삭제", description = "관리자용 특정 사용자 삭제")
  @DeleteMapping("/admin/users/{userId}")
  public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(BaseResponse.success(200, "사용자 삭제 성공", null));
  }
}

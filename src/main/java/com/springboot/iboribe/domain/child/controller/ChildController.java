package com.springboot.iboribe.domain.child.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.child.dto.request.ChildUpdateRequest;
import com.springboot.iboribe.domain.child.dto.response.ChildResponse;
import com.springboot.iboribe.domain.child.dto.response.ChildUpdateResponse;
import com.springboot.iboribe.domain.child.service.ChildService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/children")
@Tag(name = "Child", description = "자녀 관리 API")
public class ChildController {

  private final ChildService childService;

  @Operation(
      summary = "[토큰 O] 자녀 목록 조회",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Process**  \n
          - 로그인 사용자의 가족 기준으로 자녀 목록 조회  \n
          - 진료 일정 생성 시 선택할 자녀 목록 제공  \n

          **Returns**  \n
          childId  \n
          childName  \n
          profileColor  \n
          """)
  @GetMapping
  public ResponseEntity<BaseResponse<List<ChildResponse>>> getChildren(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<ChildResponse> response = childService.getChildren(userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "자녀 목록 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 아이 정보 수정",
      description =
          """
          **Description**  \n
          아이의 프로필 정보를 수정합니다.  \n
          보내지 않은 필드(null)는 기존 값이 유지됩니다.  \n

          **수정 가능 필드**  \n
          - profileColor: 프로필 색상 (RED / ORANGE / YELLOW / GREEN / SKY_BLUE / BLUE / PURPLE / PINK)  \n
          - birthDate: 생년월일 yyyyMMdd  \n
          - nickname: 닉네임 (최대 30자)  \n
          - height: 키 (cm)  \n
          - weight: 몸무게 (kg)  \n
          - memo: 메모  \n
          """)
  @PatchMapping("/{childId}")
  public ResponseEntity<BaseResponse<ChildUpdateResponse>> updateChild(
      @PathVariable Long childId, @Valid @RequestBody ChildUpdateRequest request) {

    ChildUpdateResponse response = childService.updateChild(childId, request);

    return ResponseEntity.ok(BaseResponse.success(200, "아이 정보 수정 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 아이 삭제",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Process**  \n
          - 로그인 사용자의 가족 내 자녀 삭제  \n
          - 해당 자녀의 진료 기록, AI 요약, 알림 데이터 함께 삭제  \n
          """)
  @DeleteMapping("/{childId}")
  public ResponseEntity<BaseResponse<Void>> deleteChild(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long childId) {

    childService.deleteChild(userDetails.getUserId(), childId);

    return ResponseEntity.ok(BaseResponse.success(200, "아이 삭제 성공", null));
  }
}

package com.springboot.iboribe.domain.family.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.family.dto.request.UpdateFamilyCodeRequest;
import com.springboot.iboribe.domain.family.dto.response.FamilyMemberResponse;
import com.springboot.iboribe.domain.family.service.FamilyService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/families")
@Tag(name = "Family", description = "가족 관리 API")
public class FamilyController {

  private final FamilyService familyService;

  @Operation(
      summary = "[토큰 O] 가족 구성원 조회",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Process**  \n
          - 로그인 사용자의 가족 기준으로 구성원 조회  \n

          **Returns**  \n
          userId  \n
          name  \n
          username  \n
          familyRole  \n
          """)
  @GetMapping("/members")
  public ResponseEntity<BaseResponse<List<FamilyMemberResponse>>> getFamilyMembers(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<FamilyMemberResponse> response = familyService.getFamilyMembers(userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "가족 구성원 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 가족 구성원 삭제",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Process**  \n
          - 로그인 사용자와 삭제 대상 사용자의 동일 가족 여부 검증  \n
          - 특정 가족 구성원 삭제  \n

          **Returns**  \n
          가족 구성원 삭제 성공 여부  \n
          """)
  @DeleteMapping("/members/{memberId}")
  public ResponseEntity<BaseResponse<Void>> deleteFamilyMember(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long memberId) {
    familyService.deleteFamilyMember(userDetails.getUserId(), memberId);

    return ResponseEntity.ok(BaseResponse.success(200, "가족 구성원 삭제 성공", null));
  }

  @Operation(
      summary = "[토큰 O] 가족고유번호 수정",
      description =
          """
          **Authentication**  \n
          - JWT 인증 필요  \n
          - ACCESS_TOKEN 사용  \n

          **Parameters**  \n
          familyCode: 수정할 가족고유번호  \n

          **Process**  \n
          - 로그인 사용자의 가족고유번호 수정  \n
          - 가족고유번호 중복 검증  \n

          **Returns**  \n
          가족고유번호 수정 성공 여부  \n
          """)
  @PatchMapping
  public ResponseEntity<BaseResponse<Void>> updateFamilyCode(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody UpdateFamilyCodeRequest request) {
    familyService.updateFamilyCode(userDetails.getUserId(), request);

    return ResponseEntity.ok(BaseResponse.success(200, "가족고유번호 수정 성공", null));
  }
}

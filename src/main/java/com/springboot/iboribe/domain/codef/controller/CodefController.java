package com.springboot.iboribe.domain.codef.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegisterRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.service.CodefService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/codef")
@Tag(name = "Codef", description = "CODEF 외부 API 연동")
public class CodefController {

  private final CodefService codefService;

  @Operation(
      summary = "[토큰 O] CODEF 자녀 등록 테스트",
      description =
          """
          **Parameters**  \n
          CODEF 건강iN 자녀 등록 요청값  \n

          **Returns**  \n
          CODEF 자녀 등록 API 응답 결과  \n
          """)
  @PostMapping("/children/register")
  public ResponseEntity<BaseResponse<CodefChildRegisterResponse>> registerChild(
      @Valid @RequestBody CodefChildRegisterRequest request) {

    CodefChildRegisterResponse response = codefService.registerChild(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 등록 성공", response));
  }

  @Operation(
      summary = "[토큰 O] CODEF 자녀 등록 2차 요청",
      description =
          """
          **Process**  \n
          - 1차 요청 후 카카오/간편인증 완료  \n
          - 1차 응답의 twoWayInfo 전달  \n
          - simpleAuth=1 전달  \n

          **Returns**  \n
          CODEF 자녀 등록 최종 응답  \n
          """)
  @PostMapping("/children/register/2way")
  public ResponseEntity<BaseResponse<CodefChildRegisterResponse>> registerChild2Way(
      @Valid @RequestBody CodefChildRegister2WayRequest request) {

    CodefChildRegisterResponse response = codefService.registerChild2Way(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 등록 2차 요청 성공", response));
  }
}

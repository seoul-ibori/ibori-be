package com.springboot.iboribe.domain.codef.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.codef.dto.request.CodefChildAuthRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildList2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatment2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatmentRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildListResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;
import com.springboot.iboribe.domain.codef.service.CodefService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.CustomUserDetails;

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
      summary = "[토큰 X] CODEF 자녀 등록 1차 요청",
      description =
          """
          **Description**  \n
          CODEF 건강보험공단 자녀영유아수첩 자녀 등록 1차 요청 API입니다.  \n
          보호자 인증 정보를 기반으로 자녀 등록 요청을 수행합니다.  \n

          **간편인증 기준 요청값**  \n
          - organization: 건강보험공단 고정값 `0002`  \n
          - loginType: 간편인증 사용 시 `5`  \n
          - loginTypeLevel: `1` 카카오톡, `3` 삼성패스, `4` KB모바일, `5` 통신사 PASS, `6` 네이버, `8` toss, `10` NH인증서  \n
          - userName: 보호자 이름  \n
          - identity: 보호자 생년월일 `YYYYMMDD`  \n
          - phoneNo: 보호자 휴대폰 번호  \n
          - telecom: loginTypeLevel=`5` 통신사 PASS 사용 시 필수 (`0`: SKT, `1`: KT, `2`: LG U+)  \n
          - id: 사용자 요청 식별값, 다건 요청/세션 구분용  \n

          **Returns**  \n
          - continue2Way=true 인 경우 카카오/간편인증 완료 후 2차 요청 필요  \n
          - twoWayInfo는 2차 요청 시 그대로 사용  \n
          """)
  @PostMapping("/children/register")
  public ResponseEntity<BaseResponse<CodefChildRegisterResponse>> registerChild(
      @Valid @RequestBody CodefChildAuthRequest request) {

    CodefChildRegisterResponse response = codefService.registerChild(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 등록 성공", response));
  }

  @Operation(
      summary = "[토큰 X] CODEF 자녀 등록 2차 요청",
      description =
          """
          **Description**  \n
          CODEF 자녀 등록 1차 요청 이후 추가 인증을 완료하고 최종 등록을 수행하는 API입니다.  \n

          **요청 방법**  \n
          - 카카오/간편인증 완료 후 호출  \n
          - simpleAuth: 인증 완료 시 `1`  \n
          - childName: 등록할 영유아 이름  \n
          - twoWayInfo: 1차 응답에서 받은 `jobIndex`, `threadIndex`, `jti`, `twoWayTimestamp` 값을 그대로 전달  \n
          - organization, loginType, loginTypeLevel, userName, identity, phoneNo, id 등 1차 요청값도 함께 전달  \n

          **Returns**  \n
          CODEF 자녀 등록 최종 결과  \n
          """)
  @PostMapping("/children/register/2way")
  public ResponseEntity<BaseResponse<CodefChildRegisterResponse>> registerChild2Way(
      @Valid @RequestBody CodefChildRegister2WayRequest request) {

    CodefChildRegisterResponse response = codefService.registerChild2Way(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 등록 2차 요청 성공", response));
  }

  @Operation(
      summary = "[토큰 X] CODEF 자녀 목록 조회 1차 요청",
      description =
          """
          **Description**  \n
          CODEF 건강보험공단 자녀영유아수첩 자녀 목록 조회 1차 요청 API입니다.  \n
          보호자 인증 정보를 기반으로 등록된 자녀 목록 조회를 요청합니다.  \n

          **간편인증 기준 요청값**  \n
          - organization: 건강보험공단 고정값 `0002`  \n
          - loginType: 간편인증 사용 시 `5`  \n
          - loginTypeLevel: `1` 카카오톡, `3` 삼성패스, `4` KB모바일, `5` 통신사 PASS, `6` 네이버, `8` toss, `10` NH인증서  \n
          - userName: 보호자 이름  \n
          - identity: 보호자 생년월일 `YYYYMMDD`  \n
          - phoneNo: 보호자 휴대폰 번호  \n
          - telecom: loginTypeLevel=`5` 통신사 PASS 사용 시 필수 (`0`: SKT, `1`: KT, `2`: LG U+)  \n
          - id: 사용자 요청 식별값, 다건 요청/세션 구분용  \n

          **Returns**  \n
          - continue2Way=true 인 경우 카카오/간편인증 완료 후 2차 요청 필요  \n
          - twoWayInfo는 2차 요청 시 그대로 사용  \n
          """)
  @PostMapping("/children/list")
  public ResponseEntity<BaseResponse<CodefChildListResponse>> getChildList(
      @Valid @RequestBody CodefChildAuthRequest request) {

    CodefChildListResponse response = codefService.getChildList(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 목록 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 X] CODEF 자녀 목록 조회 2차 요청",
      description =
          """
          **Description**  \n
          CODEF 자녀 목록 조회 1차 요청 이후 추가 인증을 완료하고 최종 자녀 목록을 조회하는 API입니다.  \n

          **요청 방법**  \n
          - 카카오/간편인증 완료 후 호출  \n
          - simpleAuth: 인증 완료 시 `1`  \n
          - twoWayInfo: 1차 응답에서 받은 `jobIndex`, `threadIndex`, `jti`, `twoWayTimestamp` 값을 그대로 전달  \n
          - organization, loginType, loginTypeLevel, userName, identity, phoneNo, id 등 1차 요청값도 함께 전달  \n

          **Returns**  \n
          CODEF 자녀 목록 최종 조회 결과  \n
          """)
  @PostMapping("/children/list/2way")
  public ResponseEntity<BaseResponse<CodefChildListResponse>> getChildList2Way(
      @Valid @RequestBody CodefChildList2WayRequest request) {

    CodefChildListResponse response = codefService.getChildList2Way(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 자녀 목록 조회 2차 요청 성공", response));
  }

  @Operation(
      summary = "[토큰 X] CODEF 진료 및 투약정보 조회 1차 요청",
      description =
          """
          **Description**  \n
          CODEF 건강보험공단 진료 및 투약정보 조회 1차 요청 API입니다.  \n
          본인 또는 영유아의 진료/투약정보 조회를 요청합니다.  \n

          **간편인증 기준 요청값**  \n
          - organization: 건강보험공단 고정값 `0002`  \n
          - loginType: 간편인증 사용 시 `5`  \n
          - loginTypeLevel: `1` 카카오톡, `3` 삼성패스, `4` KB모바일, `5` 통신사 PASS, `6` 네이버, `8` toss, `10` NH인증서  \n
          - userName: 사용자 이름  \n
          - identity: 생년월일 `YYYYMMDD`  \n
          - phoneNo: 휴대폰 번호  \n
          - telecom: loginTypeLevel=`5` 통신사 PASS 사용 시 필수 (`0`: SKT, `1`: KT, `2`: LG U+)  \n
          - startDate: 조회 시작일 `yyyyMMdd`  \n
          - endDate: 조회 종료일 `yyyyMMdd`  \n
          - type: 조회 대상 (`0`: 전체, `1`: 본인, `2`: 영유아)  \n
          - drugImageYN: 약품 이미지 포함 여부 (`0`: 미포함, `1`: 포함)  \n
          - medicationDirectionYN: 복약지도 포함 여부 (`0`: 미포함, `1`: 포함)  \n
          - detailYN: 의약품 상세정보 포함 여부 (`0`: 미포함, `1`: 포함)  \n
          - timeOut: 보안숫자 제한시간, 기본 `170`  \n

          **Returns**  \n
          - continue2Way=true 인 경우 카카오/간편인증 완료 후 2차 요청 필요  \n
          - twoWayInfo는 2차 요청 시 그대로 사용  \n
          """)
  @PostMapping("/medical-records")
  public ResponseEntity<BaseResponse<CodefTreatmentResponse>> getTreatmentInfo(
      @Valid @RequestBody CodefTreatmentRequest request) {

    CodefTreatmentResponse response = codefService.getTreatmentInfo(request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 진료 및 투약정보 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 X] CODEF 진료 및 투약정보 조회 2차 요청",
      description =
          """
          **Description**  \n
          CODEF 진료 및 투약정보 조회 1차 요청 이후 추가 인증을 완료하고 최종 진료/투약정보를 조회하는 API입니다.  \n

          **요청 방법**  \n
          - 카카오/간편인증 완료 후 호출  \n
          - simpleAuth: 인증 완료 시 `1`  \n
          - secureNo: 보안숫자 자동인식 실패 시 입력, 일반적으로 빈 문자열 가능  \n
          - secureNoRefresh: 기본값 `0`, 새로고침 `1`, 입력취소 `2`  \n
          - twoWayInfo: 1차 응답에서 받은 `jobIndex`, `threadIndex`, `jti`, `twoWayTimestamp` 값을 그대로 전달  \n
          - organization, loginType, loginTypeLevel, userName, identity, phoneNo, startDate, endDate, type 등 1차 요청값도 함께 전달  \n

          **Returns**  \n
          CODEF 진료 및 투약정보 최종 조회 결과  \n
          """)
  @PostMapping("/medical-records/2way")
  public ResponseEntity<BaseResponse<CodefTreatmentResponse>> getTreatmentInfo2Way(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody CodefTreatment2WayRequest request) {

    CodefTreatmentResponse response =
        codefService.getTreatmentInfo2Way(userDetails.getUserId(), request);

    return ResponseEntity.ok(BaseResponse.success(200, "CODEF 의료 기록 조회 및 저장 성공", response));
  }
}

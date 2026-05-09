package com.springboot.iboribe.domain.codef.service;

import com.springboot.iboribe.domain.codef.dto.request.CodefChildAuthRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildList2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatment2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatmentRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildListResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;

public interface CodefService {

  /**
   * CODEF 자녀 등록 (1차 요청) <br>
   * - 보호자 정보를 기반으로 자녀 등록 요청 수행 <br>
   * - 간편인증/인증서 인증 방식 사용 <br>
   * - 추가 인증 필요 시 continue2Way = true 반환 <br>
   *
   * @param request CODEF 자녀 등록 1차 요청 DTO
   * @return CODEF 자녀 등록 1차 응답 (2Way 정보 포함 가능)
   */
  CodefChildRegisterResponse registerChild(CodefChildAuthRequest request);

  /**
   * CODEF 자녀 등록 (2차 요청 - 추가 인증) <br>
   * - 1차 요청에서 continue2Way = true 인 경우 호출 <br>
   * - 사용자가 간편인증(카카오 등) 완료 후 호출 <br>
   * - simpleAuth, childName, twoWayInfo를 포함하여 최종 등록 수행 <br>
   *
   * @param request CODEF 자녀 등록 2차 요청 DTO
   * @return CODEF 자녀 등록 최종 결과
   */
  CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request);

  /**
   * CODEF 자녀 목록 조회 (1차 요청) <br>
   * - 보호자 정보를 기반으로 자녀 목록 조회 요청 <br>
   * - 추가 인증 필요 시 continue2Way = true 반환 <br>
   *
   * @param request CODEF 자녀 목록 조회 1차 요청 DTO
   * @return CODEF 자녀 목록 조회 1차 응답 (2Way 정보 포함 가능)
   */
  CodefChildListResponse getChildList(CodefChildAuthRequest request);

  /**
   * CODEF 자녀 목록 조회 (2차 요청 - 추가 인증) <br>
   * - 1차 요청에서 continue2Way = true 인 경우 호출 <br>
   * - 간편인증 완료 후 simpleAuth=1로 요청 <br>
   * - twoWayInfo를 포함하여 최종 자녀 목록 조회 수행 <br>
   *
   * @param request CODEF 자녀 목록 조회 2차 요청 DTO
   * @return CODEF 자녀 목록 조회 최종 결과
   */
  CodefChildListResponse getChildList2Way(CodefChildList2WayRequest request);

  /**
   * CODEF 진료 및 투약정보 조회 (1차 요청) - 보호자 인증 정보로 진료/투약 정보 조회 요청 - type 값으로 조회 대상 선택 (0: 전체, 1: 본인, 2:
   * 영유아) - 추가 인증 필요 시 continue2Way = true 반환
   *
   * @param request 진료/투약 조회 1차 요청 DTO
   * @return 진료/투약 조회 1차 응답 (2Way 인증 정보 포함 가능)
   */
  CodefTreatmentResponse getTreatmentInfo(CodefTreatmentRequest request);

  /**
   * CODEF 진료 및 투약정보 조회 (2차 요청) - 1차 요청에서 continue2Way = true 인 경우 호출 - 간편인증 완료 후 simpleAuth=1 전달 -
   * twoWayInfo 포함하여 최종 진료/투약 정보 조회 수행
   *
   * @param request 진료/투약 조회 2차 요청 DTO
   * @return 진료/투약 최종 조회 결과
   */
  CodefTreatmentResponse getTreatmentInfo2Way(Long userId, CodefTreatment2WayRequest request);
}

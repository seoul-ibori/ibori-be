package com.springboot.iboribe.domain.codef.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.codef.client.CodefClient;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildAuthRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildList2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatment2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatmentRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildListResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;
import com.springboot.iboribe.domain.medicalrecord.service.MedicalRecordSaveService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodefServiceImpl implements CodefService {

  private final CodefClient codefClient;
  private final MedicalRecordSaveService medicalRecordSaveService;

  /** CODEF 자녀 등록 1차 요청 - 인증 요청 및 2Way 여부 확인 */
  @Override
  public CodefChildRegisterResponse registerChild(CodefChildAuthRequest request) {
    log.info("[CODEF] 자녀 등록 API 호출 시작 - userName: {}", request.getUserName());

    CodefChildRegisterResponse response = codefClient.registerChild(request);

    log.info("[CODEF] 자녀 등록 API 호출 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** CODEF 자녀 등록 2차 요청 - 간편인증 완료 후 최종 등록 */
  @Override
  @Transactional
  public CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request) {
    log.info("[CODEF] 자녀 등록 2차 요청 시작 - childName: {}", request.getChildName());

    CodefChildRegisterResponse response = codefClient.registerChild2Way(request);

    log.info("[CODEF] 자녀 등록 2차 요청 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** CODEF 자녀 목록 조회 1차 요청 - 인증 요청 및 2Way 여부 확인 */
  @Override
  public CodefChildListResponse getChildList(CodefChildAuthRequest request) {
    log.info("[CODEF] 자녀 목록 조회 API 호출 시작 - userName: {}", request.getUserName());

    CodefChildListResponse response = codefClient.getChildList(request);

    log.info("[CODEF] 자녀 목록 조회 API 호출 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** CODEF 자녀 목록 조회 2차 요청 - 간편인증 완료 후 목록 조회 */
  @Override
  public CodefChildListResponse getChildList2Way(CodefChildList2WayRequest request) {
    log.info("[CODEF] 자녀 목록 조회 2차 요청 시작 - userName: {}", request.getUserName());

    CodefChildListResponse response = codefClient.getChildList2Way(request);

    log.info("[CODEF] 자녀 목록 조회 2차 요청 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** CODEF 진료 및 투약정보 조회 1차 요청 - 인증 요청 및 조회 대상(type) 설정 */
  @Override
  public CodefTreatmentResponse getTreatmentInfo(CodefTreatmentRequest request) {
    log.info(
        "[CODEF] 진료 및 투약정보 조회 1차 요청 시작 - userName: {}, type: {}",
        request.getUserName(),
        request.getType());

    CodefTreatmentResponse response = codefClient.getTreatmentInfo(request);

    log.info("[CODEF] 진료 및 투약정보 조회 1차 요청 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** CODEF 진료 및 투약정보 조회 2차 요청 - 간편인증 완료 후 최종 데이터 조회 */
  @Override
  @Transactional
  public CodefTreatmentResponse getTreatmentInfo2Way(
      Long userId, CodefTreatment2WayRequest request) {
    CodefTreatmentResponse response = codefClient.getTreatmentInfo2Way(request);

    medicalRecordSaveService.saveFromCodefResponse(userId, response);

    return response;
  }
}

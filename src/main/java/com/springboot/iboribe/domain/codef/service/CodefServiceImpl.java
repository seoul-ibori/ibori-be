package com.springboot.iboribe.domain.codef.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.codef.client.CodefClient;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegisterRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodefServiceImpl implements CodefService {

  private final CodefClient codefClient;

  /** 1차 요청 */
  @Override
  public CodefChildRegisterResponse registerChild(CodefChildRegisterRequest request) {
    log.info("[CODEF] 자녀 등록 API 호출 시작 - userName: {}", request.getUserName());

    CodefChildRegisterResponse response = codefClient.registerChild(request);

    log.info("[CODEF] 자녀 등록 API 호출 완료 - resultCode: {}", response.getResultCode());

    return response;
  }

  /** 2차 요청 (간편인증 이후) */
  @Override
  @Transactional
  public CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request) {
    log.info("[CODEF] 자녀 등록 2차 요청 시작 - childName: {}", request.getChildName());

    CodefChildRegisterResponse response = codefClient.registerChild2Way(request);

    log.info("[CODEF] 자녀 등록 2차 요청 완료 - resultCode: {}", response.getResultCode());

    return response;
  }
}

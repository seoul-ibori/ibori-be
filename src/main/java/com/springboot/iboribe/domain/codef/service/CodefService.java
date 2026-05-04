package com.springboot.iboribe.domain.codef.service;

import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegisterRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;

public interface CodefService {

  /**
   * CODEF 자녀 등록 (1차 요청) <br>
   * - 간편인증 / 인증서 기반 자녀 등록 요청 <br>
   * - 추가 인증 필요 시 continue2Way = true 반환 <br>
   *
   * @param request CODEF 자녀 등록 요청 DTO
   * @return CODEF 자녀 등록 응답
   */
  CodefChildRegisterResponse registerChild(CodefChildRegisterRequest request);

  /**
   * CODEF 자녀 등록 (2차 요청 - 추가 인증) <br>
   * - 1차 요청에서 continue2Way = true 인 경우 호출 <br>
   * - simpleAuth, childName, twoWayInfo 포함하여 최종 등록 수행 <br>
   *
   * @param request CODEF 자녀 등록 2차 요청 DTO
   * @return CODEF 자녀 등록 최종 결과
   */
  CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request);
}

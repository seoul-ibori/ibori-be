package com.springboot.iboribe.domain.predict.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.iboribe.domain.predict.dto.request.PredictRequest;
import com.springboot.iboribe.domain.predict.dto.response.PredictResponse;
import com.springboot.iboribe.domain.predict.service.PredictService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/predict")
@Tag(name = "Predict", description = "혼잡도 예측 API")
public class PredictController {

  private final PredictService predictService;

  @Operation(
      summary = "[토큰 X] 소아청소년과 혼잡도 예측",
      description =
          """
          **Parameters**  \n
          dong: 동 이름 (예: 역삼동)  \n

          **Note**  \n
          월과 요일은 서버의 오늘 날짜 기준으로 자동 적용됩니다.  \n

          **Returns**  \n
          gu: 구 이름  \n
          dong: 동 이름  \n
          congestionLevel: 혼잡도 (여유 / 보통 / 혼잡 / 매우혼잡)  \n
          """)
  @PostMapping("/dong")
  public ResponseEntity<BaseResponse<PredictResponse>> predict(
      @Valid @RequestBody PredictRequest request) {

    return ResponseEntity.ok(BaseResponse.success(predictService.predict(request)));
  }
}

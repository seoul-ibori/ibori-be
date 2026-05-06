package com.springboot.iboribe.domain.predict.service;

import org.springframework.stereotype.Service;

import com.springboot.iboribe.domain.predict.client.PredictClient;
import com.springboot.iboribe.domain.predict.client.PredictClient.ExternalPredictResponse;
import com.springboot.iboribe.domain.predict.dto.request.PredictRequest;
import com.springboot.iboribe.domain.predict.dto.response.PredictResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PredictService {

  private final PredictClient predictClient;

  public PredictResponse predict(PredictRequest request) {
    ExternalPredictResponse external = predictClient.predict(request.getDong());

    return PredictResponse.builder()
        .gu(external.getGu())
        .dong(external.getDong())
        .congestionLevel(external.getCongestionLevel())
        .build();
  }
}

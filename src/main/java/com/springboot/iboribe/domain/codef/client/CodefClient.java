package com.springboot.iboribe.domain.codef.client;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildAuthRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildList2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatment2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatmentRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildListResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;
import com.springboot.iboribe.domain.codef.exception.CodefErrorCode;
import com.springboot.iboribe.domain.codef.mapper.CodefRequestBodyMapper;
import com.springboot.iboribe.domain.codef.mapper.CodefResponseMapper;
import com.springboot.iboribe.global.exception.CustomException;

@Component
public class CodefClient {

  private static final String CHILD_REGISTER_URI =
      "/v1/kr/public/pp/nhis-family-health/child-register";

  private static final String CHILD_LIST_URI = "/v1/kr/public/pp/nhis-family-health/child-list";

  private static final String TREATMENT_INFO_URI = "/v1/kr/public/pp/nhis-treatment/information";

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final CodefResponseMapper responseMapper;
  private final CodefRequestBodyMapper requestBodyMapper;
  private final String accessToken;

  public CodefClient(
      WebClient.Builder webClientBuilder,
      ObjectMapper objectMapper,
      CodefResponseMapper responseMapper,
      CodefRequestBodyMapper requestBodyMapper,
      @Value("${codef.base-url}") String baseUrl,
      @Value("${codef.access-token}") String accessToken) {

    this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    this.objectMapper = objectMapper;
    this.responseMapper = responseMapper;
    this.requestBodyMapper = requestBodyMapper;
    this.accessToken = accessToken;
  }

  public CodefChildRegisterResponse registerChild(CodefChildAuthRequest request) {
    return responseMapper.toChildRegisterResponse(
        parseResponse(callCodef(CHILD_REGISTER_URI, request)));
  }

  public CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request) {
    return responseMapper.toChildRegisterResponse(
        parseResponse(
            callCodef(CHILD_REGISTER_URI, requestBodyMapper.toChildRegister2WayBody(request))));
  }

  public CodefChildListResponse getChildList(CodefChildAuthRequest request) {
    return responseMapper.toChildListResponse(parseResponse(callCodef(CHILD_LIST_URI, request)));
  }

  public CodefChildListResponse getChildList2Way(CodefChildList2WayRequest request) {
    return responseMapper.toChildListResponse(
        parseResponse(callCodef(CHILD_LIST_URI, requestBodyMapper.toChildList2WayBody(request))));
  }

  public CodefTreatmentResponse getTreatmentInfo(CodefTreatmentRequest request) {
    return responseMapper.toTreatmentResponse(
        parseResponse(callCodef(TREATMENT_INFO_URI, request)));
  }

  public CodefTreatmentResponse getTreatmentInfo2Way(CodefTreatment2WayRequest request) {
    return responseMapper.toTreatmentResponse(
        parseResponse(
            callCodef(TREATMENT_INFO_URI, requestBodyMapper.toTreatment2WayBody(request))));
  }

  private String callCodef(String uri, Object requestBody) {
    return webClient
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + accessToken)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .timeout(Duration.ofSeconds(300))
        .onErrorMap(
            TimeoutException.class, e -> new CustomException(CodefErrorCode.CODEF_API_TIMEOUT))
        .block();
  }

  private Map<String, Object> parseResponse(String responseBody) {

    if (responseBody == null) {
      throw new CustomException(CodefErrorCode.CODEF_EMPTY_RESPONSE);
    }

    String decodedResponseBody = URLDecoder.decode(responseBody, StandardCharsets.UTF_8);

    try {
      return objectMapper.readValue(decodedResponseBody, new TypeReference<>() {});
    } catch (Exception e) {
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }
  }
}

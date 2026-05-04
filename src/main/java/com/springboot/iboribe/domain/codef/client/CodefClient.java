package com.springboot.iboribe.domain.codef.client;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegisterRequest;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.exception.CodefErrorCode;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodefClient {

  private static final String CHILD_REGISTER_URI =
      "/v1/kr/public/pp/nhis-family-health/child-register";

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final String accessToken;

  public CodefClient(
      WebClient.Builder webClientBuilder,
      ObjectMapper objectMapper,
      @Value("${codef.base-url}") String baseUrl,
      @Value("${codef.access-token}") String accessToken) {
    this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    this.objectMapper = objectMapper;
    this.accessToken = accessToken;
  }

  public CodefChildRegisterResponse registerChild(CodefChildRegisterRequest request) {
    String responseBody = callCodef(request);
    Map<String, Object> response = parseResponse(responseBody);

    return toChildRegisterResponse(response);
  }

  public CodefChildRegisterResponse registerChild2Way(CodefChildRegister2WayRequest request) {
    Map<String, Object> body = new HashMap<>();

    body.put("organization", request.getOrganization());
    body.put("loginType", request.getLoginType());
    body.put("loginTypeLevel", request.getLoginTypeLevel());
    body.put("userName", request.getUserName());
    body.put("identity", request.getIdentity());
    body.put("phoneNo", request.getPhoneNo());
    body.put("telecom", request.getTelecom());
    body.put("id", request.getId());

    body.put("simpleAuth", request.getSimpleAuth());
    body.put("childName", request.getChildName());
    body.put("is2Way", true);
    body.put("twoWayInfo", request.getTwoWayInfo());

    String responseBody = callCodef(body);
    Map<String, Object> response = parseResponse(responseBody);

    return toChildRegisterResponse(response);
  }

  private String callCodef(Object requestBody) {
    return webClient
        .post()
        .uri(CHILD_REGISTER_URI)
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
      log.error("[CODEF] 응답 JSON 파싱 실패 - body: {}", decodedResponseBody, e);
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }
  }

  private CodefChildRegisterResponse toChildRegisterResponse(Map<String, Object> response) {
    Object resultObject = response.get("result");

    if (!(resultObject instanceof Map<?, ?> result)) {
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }

    Object dataObject = response.get("data");

    Map<?, ?> data = null;
    if (dataObject instanceof Map<?, ?> dataMap) {
      data = dataMap;
    }

    Boolean continue2Way = data != null ? (Boolean) data.get("continue2Way") : null;

    Map<String, Object> twoWayInfo = null;
    if (data != null && Boolean.TRUE.equals(continue2Way)) {
      twoWayInfo = new HashMap<>();
      twoWayInfo.put("jobIndex", data.get("jobIndex"));
      twoWayInfo.put("threadIndex", data.get("threadIndex"));
      twoWayInfo.put("jti", data.get("jti"));
      twoWayInfo.put("twoWayTimestamp", data.get("twoWayTimestamp"));
    }

    return CodefChildRegisterResponse.builder()
        .resultCode(String.valueOf(result.get("code")))
        .resultMessage(String.valueOf(result.get("message")))
        .continue2Way(continue2Way)
        .method(data != null ? String.valueOf(data.get("method")) : null)
        .twoWayInfo(twoWayInfo)
        .resRegistrationStatus(
            data != null ? String.valueOf(data.get("resRegistrationStatus")) : null)
        .resResultDesc(data != null ? String.valueOf(data.get("resResultDesc")) : null)
        .rawData(response)
        .build();
  }
}

package com.springboot.iboribe.domain.predict.client;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.iboribe.domain.predict.entity.CongestionLevel;
import com.springboot.iboribe.domain.predict.exception.PredictErrorCode;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PredictClient {

  private static final String PREDICT_URI = "/predict/dong";
  private static final Map<DayOfWeek, String> DAY_OF_WEEK_MAP =
      Map.of(
          DayOfWeek.MONDAY, "월요일",
          DayOfWeek.TUESDAY, "화요일",
          DayOfWeek.WEDNESDAY, "수요일",
          DayOfWeek.THURSDAY, "목요일",
          DayOfWeek.FRIDAY, "금요일",
          DayOfWeek.SATURDAY, "토요일",
          DayOfWeek.SUNDAY, "일요일");

  private final WebClient webClient;

  public PredictClient(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://host.docker.internal:8000").build();
  }

  // 월, 요일은 서울 기준으로 자동 세팅
  public ExternalPredictResponse predict(String dong) {
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
    int month = today.getMonthValue();
    String dayOfWeek = DAY_OF_WEEK_MAP.get(today.getDayOfWeek());

    Map<String, Object> body =
        Map.of(
            "dong", dong,
            "month", month,
            "day_of_week", dayOfWeek);

    return webClient
        .post()
        .uri(PREDICT_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(ExternalPredictResponse.class)
        .timeout(Duration.ofSeconds(10))
        .onErrorMap(
            TimeoutException.class, e -> new CustomException(PredictErrorCode.PREDICT_API_TIMEOUT))
        .onErrorMap(
            WebClientResponseException.class,
            e -> {
              log.error(
                  "[Predict] 외부 API 호출 실패 - status: {}, body: {}",
                  e.getStatusCode(),
                  e.getResponseBodyAsString());
              return new CustomException(PredictErrorCode.PREDICT_API_ERROR);
            })
        .block();
  }

  @Getter
  @NoArgsConstructor
  public static class ExternalPredictResponse {
    private String dong;
    private String gu;

    @JsonProperty("congestion_level")
    private CongestionLevel congestionLevel;
  }
}

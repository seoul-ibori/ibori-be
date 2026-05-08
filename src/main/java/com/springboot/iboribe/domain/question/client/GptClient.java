package com.springboot.iboribe.domain.question.client;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.iboribe.domain.question.dto.request.QuestionRequest;
import com.springboot.iboribe.domain.question.dto.response.QuestionResponse;
import com.springboot.iboribe.domain.question.exception.QuestionErrorCode;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GptClient {

  private static final String GPT_URI = "/v1/chat/completions";
  private static final String MODEL = "gpt-4o-mini";
  private static final String SYSTEM_PROMPT =
      "당신은 소아과 진료를 돕는 전문 어시스턴트입니다. "
          + "부모가 입력한 아이의 상태 정보를 바탕으로, 보호자가 진료실에서 의사에게 직접 물어볼 질문 목록을 생성합니다. "
          + "질문의 주체는 반드시 보호자이며, 의사가 보호자에게 묻는 형식이 절대 되어서는 안 됩니다. "
          + "예를 들어 '열이 언제부터 났나요?'가 아니라 '열이 언제부터 위험한 수준인가요?' 와 같이 보호자가 의사에게 묻는 형식으로 작성하세요. "
          + "질문은 한국어로 작성하며 보호자가 진료실에서 실제로 사용할 수 있는 실용적인 내용이어야 합니다. "
          + "이 질문지는 진료 보조 목적이며 의사의 진단을 대체하지 않습니다.";

  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  public GptClient(
      WebClient.Builder webClientBuilder,
      ObjectMapper objectMapper,
      @Value("${openai.api-key}") String apiKey) {
    this.webClient =
        webClientBuilder
            .baseUrl("https://api.openai.com")
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
    this.objectMapper = objectMapper;
  }

  public QuestionResponse generateQuestions(QuestionRequest request) {
    String userPrompt = buildUserPrompt(request);

    Map<String, Object> body =
        Map.of(
            "model",
            MODEL,
            "messages",
            List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userPrompt)),
            "response_format",
            Map.of("type", "json_object"),
            "temperature",
            0.7,
            "max_tokens",
            600);

    String responseBody =
        webClient
            .post()
            .uri(GPT_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(30))
            .onErrorMap(
                TimeoutException.class, e -> new CustomException(QuestionErrorCode.GPT_API_TIMEOUT))
            .onErrorMap(
                WebClientResponseException.class,
                e -> {
                  log.error(
                      "[GPT] 외부 API 호출 실패 - status: {}, body: {}",
                      e.getStatusCode(),
                      e.getResponseBodyAsString());
                  return new CustomException(QuestionErrorCode.GPT_API_ERROR);
                })
            .block();

    return parseResponse(responseBody);
  }

  private String buildUserPrompt(QuestionRequest request) {
    String symptomText = String.join(", ", request.getSymptoms());

    List<String> lines = new ArrayList<>();
    lines.add("아이 상태 정보:");
    lines.add("- 증상: " + symptomText);

    lines.add("- 증상 시작 기간: " + request.getSymptomDuration());
    if (request.getTemperature() != null) {
      lines.add("- 체온: " + request.getTemperature() + "°C");
    }
    if (request.getAppetiteChange() != null) {
      lines.add("- 식욕 변화: " + request.getAppetiteChange());
    }
    if (request.getSleepCondition() != null) {
      lines.add("- 수면 상태: " + request.getSleepCondition());
    }
    if (request.getMedicationNotes() != null && !request.getMedicationNotes().isEmpty()) {
      lines.add("- 약 복용 특이점: " + String.join(", ", request.getMedicationNotes()));
    }

    lines.add(
        "\n위 정보를 바탕으로 보호자가 의사에게 직접 물어볼 질문 5개를 JSON으로 반환해주세요. "
            + "반드시 보호자→의사 방향의 질문이어야 하며, 의사가 보호자에게 묻는 형식은 절대 안 됩니다. "
            + "형식: {\"questions\": [\"질문1\", \"질문2\", ...]}");

    return String.join("\n", lines);
  }

  @SuppressWarnings("unchecked")
  private QuestionResponse parseResponse(String responseBody) {
    try {
      Map<String, Object> response = objectMapper.readValue(responseBody, new TypeReference<>() {});
      List<?> choices = (List<?>) response.get("choices");
      Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
      Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
      String content = (String) message.get("content");

      Map<String, Object> parsed = objectMapper.readValue(content, new TypeReference<>() {});
      List<String> questions =
          objectMapper.convertValue(parsed.get("questions"), new TypeReference<>() {});

      return new QuestionResponse(questions);
    } catch (Exception e) {
      log.error("[GPT] 응답 파싱 실패 - body: {}", responseBody, e);
      throw new CustomException(QuestionErrorCode.GPT_INVALID_RESPONSE);
    }
  }
}

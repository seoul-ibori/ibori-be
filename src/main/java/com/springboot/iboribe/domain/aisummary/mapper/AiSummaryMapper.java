package com.springboot.iboribe.domain.aisummary.mapper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.iboribe.domain.aisummary.dto.response.OpenAiResponse;
import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.aisummary.exception.AiSummaryErrorCode;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiSummaryMapper {

  @Qualifier("openAiSummaryWebClient")
  private final WebClient openAiSummaryWebClient;

  private final ObjectMapper objectMapper;

  public String transcribeAudio(MultipartFile audioFile) {
    if (audioFile == null || audioFile.isEmpty()) {
      throw new CustomException(AiSummaryErrorCode.AUDIO_FILE_EMPTY);
    }

    String fileName = audioFile.getOriginalFilename();
    if (fileName == null || fileName.isBlank()) {
      fileName = "recording.m4a";
    }

    final String finalFileName = fileName;

    try {
      MultipartBodyBuilder builder = new MultipartBodyBuilder();
      builder.part("model", "whisper-1");
      builder.part("language", "ko");
      builder
          .part(
              "file",
              new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                  return finalFileName;
                }
              })
          .filename(finalFileName)
          .contentType(MediaType.APPLICATION_OCTET_STREAM);

      Map<String, Object> response =
          openAiSummaryWebClient
              .post()
              .uri("/audio/transcriptions")
              .contentType(MediaType.MULTIPART_FORM_DATA)
              .body(BodyInserters.fromMultipartData(builder.build()))
              .retrieve()
              .bodyToMono(
                  new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
              .block();

      if (response == null || response.get("text") == null) {
        throw new CustomException(AiSummaryErrorCode.TRANSCRIPTION_FAILED);
      }

      return String.valueOf(response.get("text"));
    } catch (CustomException e) {
      throw e;
    } catch (WebClientResponseException e) {
      log.error(
          "OpenAI 음성 텍스트 변환 실패 - status: {}, body: {}",
          e.getStatusCode(),
          e.getResponseBodyAsString(),
          e);
      throw new CustomException(AiSummaryErrorCode.TRANSCRIPTION_FAILED);
    } catch (Exception e) {
      log.error("음성 파일 처리 실패", e);
      throw new CustomException(AiSummaryErrorCode.TRANSCRIPTION_FAILED);
    }
  }

  public AiSummary toAiSummaryEntity(MedicalRecord medicalRecord, String transcript) {
    String prompt =
        """
        당신은 소아과 진료 내용을 보호자가 이해하기 쉽게 정리하는 AI입니다.

        다음 규칙을 반드시 지켜주세요.

        [규칙]
        - 보호자가 이해하기 쉬운 한국어 사용
        - 불필요한 반복 제거
        - 실제 진료 내용만 기반으로 작성
        - 추측 금지
        - 약 이름은 들린 내용 그대로 유지
        - 주의사항은 보호자가 실천해야 할 내용만 정리
        - 다음 방문 일정이 대화에 있으면 포함
        - 다음 방문 일정이 없으면 "특별히 안내된 다음 일정은 없어요."라고 작성
        - 각 항목은 1~2문장으로 짧게 작성
        - JSON 이외의 문장은 절대 포함하지 않기

        아래 JSON 형식으로만 응답하세요.

        {
          "oneLineSummary": "",
          "medicalSummary": "",
          "caution": "",
          "nextSchedule": ""
        }

        [진료 대화]
        """
            + transcript;

    try {
      Map<String, Object> requestBody =
          Map.of(
              "model",
              "gpt-4o-mini",
              "messages",
              new Object[] {
                Map.of("role", "system", "content", "너는 소아 진료 내용을 요약하는 의료 보조 AI야."),
                Map.of("role", "user", "content", prompt)
              },
              "response_format",
              Map.of("type", "json_object"));

      OpenAiResponse response =
          openAiSummaryWebClient
              .post()
              .uri("/chat/completions")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(requestBody)
              .retrieve()
              .bodyToMono(OpenAiResponse.class)
              .block();

      if (response == null || response.getChoices() == null || response.getChoices().length == 0) {
        throw new CustomException(AiSummaryErrorCode.SUMMARY_GENERATION_FAILED);
      }

      String content = response.getChoices()[0].getMessage().getContent();
      Map<String, String> result = objectMapper.readValue(content, new TypeReference<>() {});

      return AiSummary.builder()
          .medicalRecord(medicalRecord)
          .oneLineSummary(result.get("oneLineSummary"))
          .medicalSummary(result.get("medicalSummary"))
          .caution(result.get("caution"))
          .nextSchedule(result.get("nextSchedule"))
          .build();
    } catch (CustomException e) {
      throw e;
    } catch (WebClientResponseException e) {
      log.error(
          "OpenAI AI 요약 생성 실패 - status: {}, body: {}",
          e.getStatusCode(),
          e.getResponseBodyAsString(),
          e);
      throw new CustomException(AiSummaryErrorCode.SUMMARY_GENERATION_FAILED);
    } catch (Exception e) {
      log.error("AI 요약 생성 실패", e);
      throw new CustomException(AiSummaryErrorCode.SUMMARY_GENERATION_FAILED);
    }
  }
}

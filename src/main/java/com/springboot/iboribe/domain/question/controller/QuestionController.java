package com.springboot.iboribe.domain.question.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.iboribe.domain.question.dto.request.QuestionRequest;
import com.springboot.iboribe.domain.question.dto.response.QuestionResponse;
import com.springboot.iboribe.domain.question.service.QuestionService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
@Tag(name = "Question", description = "AI 질문지 생성 API")
public class QuestionController {

  private final QuestionService questionService;

  @PostMapping
  @Operation(
      summary = "AI 맞춤형 질문지 생성",
      description =
          """
          - symptoms: 아이가 보이는 증상 목록 (필수, 중복선택)
          - symptomDuration: 증상이 시작된 기간 (필수, 예: 3)
          - temperature: 측정한 체온 (°C)
          - appetiteChange: 식욕 상태
          - sleepCondition: 수면 상태
          - medicationNotes: 약 복용 시 주의사항 (중복 선택, 예: 알약 불가, 가루약 불가)
          """)
  public ResponseEntity<BaseResponse<QuestionResponse>> generateQuestions(
      @RequestBody @Valid QuestionRequest request) {
    QuestionResponse response = questionService.generateQuestions(request);
    return ResponseEntity.ok(BaseResponse.success(response));
  }
}

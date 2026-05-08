package com.springboot.iboribe.domain.question.service;

import org.springframework.stereotype.Service;

import com.springboot.iboribe.domain.question.client.GptClient;
import com.springboot.iboribe.domain.question.dto.request.QuestionRequest;
import com.springboot.iboribe.domain.question.dto.response.QuestionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final GptClient gptClient;

  public QuestionResponse generateQuestions(QuestionRequest request) {
    return gptClient.generateQuestions(request);
  }
}

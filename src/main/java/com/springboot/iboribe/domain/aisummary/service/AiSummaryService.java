package com.springboot.iboribe.domain.aisummary.service;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.iboribe.domain.aisummary.dto.response.AiSummaryResponse;

public interface AiSummaryService {

  AiSummaryResponse generateSummaryFromAudio(
      Long childId, Long recordId, MultipartFile audioFile, Long requesterId);

  AiSummaryResponse getAiSummary(Long recordId);

  void deleteAiSummary(Long recordId);
}

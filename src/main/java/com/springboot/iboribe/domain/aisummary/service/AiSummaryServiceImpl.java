package com.springboot.iboribe.domain.aisummary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.iboribe.domain.aisummary.dto.response.AiSummaryResponse;
import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.aisummary.exception.AiSummaryErrorCode;
import com.springboot.iboribe.domain.aisummary.mapper.AiSummaryMapper;
import com.springboot.iboribe.domain.aisummary.repository.AiSummaryRepository;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.exception.MedicalRecordErrorCode;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiSummaryServiceImpl implements AiSummaryService {

  private final MedicalRecordRepository medicalRecordRepository;
  private final AiSummaryRepository aiSummaryRepository;
  private final AiSummaryMapper aiSummaryMapper;

  @Override
  @Transactional
  public AiSummaryResponse generateSummaryFromAudio(Long recordId, MultipartFile audioFile) {
    MedicalRecord medicalRecord =
        medicalRecordRepository
            .findById(recordId)
            .orElseThrow(
                () -> new CustomException(MedicalRecordErrorCode.MEDICAL_RECORD_NOT_FOUND));

    if (aiSummaryRepository.existsByMedicalRecord(medicalRecord)) {
      throw new CustomException(AiSummaryErrorCode.AI_SUMMARY_ALREADY_EXISTS);
    }

    String transcript = aiSummaryMapper.transcribeAudio(audioFile);
    AiSummary aiSummary =
        aiSummaryRepository.save(aiSummaryMapper.toAiSummaryEntity(medicalRecord, transcript));

    return AiSummaryResponse.from(aiSummary);
  }

  @Override
  public AiSummaryResponse getAiSummary(Long recordId) {
    AiSummary aiSummary =
        aiSummaryRepository
            .findByMedicalRecordId(recordId)
            .orElseThrow(() -> new CustomException(AiSummaryErrorCode.AI_SUMMARY_NOT_FOUND));

    return AiSummaryResponse.from(aiSummary);
  }

  @Override
  @Transactional
  public void deleteAiSummary(Long recordId) {
    if (!aiSummaryRepository.existsByMedicalRecordId(recordId)) {
      throw new CustomException(AiSummaryErrorCode.AI_SUMMARY_NOT_FOUND);
    }

    aiSummaryRepository.deleteByMedicalRecordId(recordId);
  }
}

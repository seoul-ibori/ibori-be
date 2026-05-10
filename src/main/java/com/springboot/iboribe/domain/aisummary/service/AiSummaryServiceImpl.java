package com.springboot.iboribe.domain.aisummary.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.iboribe.domain.aisummary.dto.response.AiSummaryResponse;
import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.aisummary.exception.AiSummaryErrorCode;
import com.springboot.iboribe.domain.aisummary.mapper.AiSummaryMapper;
import com.springboot.iboribe.domain.aisummary.repository.AiSummaryRepository;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.exception.ChildErrorCode;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecordSource;
import com.springboot.iboribe.domain.medicalrecord.exception.MedicalRecordErrorCode;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.domain.notification.service.NotificationService;
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
  private final ChildRepository childRepository;
  private final AiSummaryMapper aiSummaryMapper;
  private final NotificationService notificationService;

  @Override
  @Transactional
  public AiSummaryResponse generateSummaryFromAudio(
      Long childId, Long recordId, MultipartFile audioFile, Long requesterId) {

    MedicalRecord medicalRecord = resolveOrCreateMedicalRecord(childId, recordId);

    if (aiSummaryRepository.existsByMedicalRecord(medicalRecord)) {
      throw new CustomException(AiSummaryErrorCode.AI_SUMMARY_ALREADY_EXISTS);
    }

    String transcript = aiSummaryMapper.transcribeAudio(audioFile);
    AiSummary aiSummary =
        aiSummaryRepository.save(aiSummaryMapper.toAiSummaryEntity(medicalRecord, transcript));

    notificationService.createFamilyNotifications(aiSummary, requesterId);

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

  private MedicalRecord resolveOrCreateMedicalRecord(Long childId, Long recordId) {
    if (recordId != null) {
      return medicalRecordRepository
          .findById(recordId)
          .orElseThrow(() -> new CustomException(MedicalRecordErrorCode.MEDICAL_RECORD_NOT_FOUND));
    }

    if (childId == null) {
      throw new CustomException(AiSummaryErrorCode.CHILD_ID_REQUIRED);
    }

    Child child =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));

    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    return medicalRecordRepository.save(
        MedicalRecord.builder()
            .child(child)
            .hospitalName("진료 녹음 요약")
            .treatDate(today)
            .source(MedicalRecordSource.AI)
            .medications(List.of())
            .build());
  }
}

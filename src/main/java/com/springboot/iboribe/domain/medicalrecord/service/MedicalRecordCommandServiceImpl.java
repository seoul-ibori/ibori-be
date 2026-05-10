package com.springboot.iboribe.domain.medicalrecord.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.aisummary.repository.AiSummaryRepository;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.exception.ChildErrorCode;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordCreateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordUpdateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordCreateResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordUpdateResponse;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecordSource;
import com.springboot.iboribe.domain.medicalrecord.exception.MedicalRecordErrorCode;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordCommandServiceImpl implements MedicalRecordCommandService {

  private final ChildRepository childRepository;
  private final MedicalRecordRepository medicalRecordRepository;
  private final AiSummaryRepository aiSummaryRepository;

  @Override
  public MedicalRecordCreateResponse createMedicalRecord(MedicalRecordCreateRequest request) {
    Child child =
        childRepository
            .findById(request.getChildId())
            .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));

    MedicalRecord medicalRecord =
        medicalRecordRepository.save(
            MedicalRecord.builder()
                .child(child)
                .title(request.getTitle())
                .hospitalName(request.getHospitalName())
                .treatDate(request.getTreatDate())
                .treatTime(request.getTreatTime())
                .memo(request.getMemo())
                .source(MedicalRecordSource.USER)
                .build());

    return MedicalRecordCreateResponse.from(medicalRecord);
  }

  @Override
  public MedicalRecordUpdateResponse updateMedicalRecord(
      Long recordId, MedicalRecordUpdateRequest request) {
    MedicalRecord record =
        medicalRecordRepository
            .findById(recordId)
            .orElseThrow(
                () -> new CustomException(MedicalRecordErrorCode.MEDICAL_RECORD_NOT_FOUND));

    if (record.getSource() != MedicalRecordSource.USER) {
      throw new CustomException(MedicalRecordErrorCode.CODEF_RECORD_NOT_MODIFIABLE);
    }

    Child child = null;
    if (request.getChildId() != null) {
      child =
          childRepository
              .findById(request.getChildId())
              .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));
    }

    record.update(
        child,
        request.getTitle(),
        request.getHospitalName(),
        request.getTreatTime(),
        request.getMemo());

    boolean hasAiSummary = aiSummaryRepository.existsByMedicalRecordId(recordId);
    return MedicalRecordUpdateResponse.from(record, hasAiSummary);
  }

  @Override
  public void deleteMedicalRecord(Long recordId) {
    MedicalRecord record =
        medicalRecordRepository
            .findById(recordId)
            .orElseThrow(
                () -> new CustomException(MedicalRecordErrorCode.MEDICAL_RECORD_NOT_FOUND));

    if (record.getSource() != MedicalRecordSource.USER) {
      throw new CustomException(MedicalRecordErrorCode.CODEF_RECORD_NOT_MODIFIABLE);
    }

    aiSummaryRepository.deleteByMedicalRecordId(recordId);
    medicalRecordRepository.delete(record);
  }
}

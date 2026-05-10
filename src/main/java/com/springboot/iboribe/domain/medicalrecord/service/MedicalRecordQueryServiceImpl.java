package com.springboot.iboribe.domain.medicalrecord.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.exception.ChildErrorCode;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordSummaryResponse;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.exception.MedicalRecordErrorCode;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordQueryServiceImpl implements MedicalRecordQueryService {

  private final ChildRepository childRepository;
  private final MedicalRecordRepository medicalRecordRepository;

  @Override
  public List<MedicalRecordSummaryResponse> getMedicalRecords(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);
    String startDate = yearMonth.atDay(1).format(DateTimeFormatter.BASIC_ISO_DATE);
    String endDate = yearMonth.atEndOfMonth().format(DateTimeFormatter.BASIC_ISO_DATE);

    return medicalRecordRepository
        .findAllByTreatDateBetweenOrderByTreatDateDesc(startDate, endDate)
        .stream()
        .map(MedicalRecordSummaryResponse::from)
        .toList();
  }

  @Override
  public List<MedicalRecordSummaryResponse> getMedicalRecordsByChild(
      Long childId, int year, int month) {
    Child child =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new CustomException(ChildErrorCode.CHILD_NOT_FOUND));

    YearMonth yearMonth = YearMonth.of(year, month);
    String startDate = yearMonth.atDay(1).format(DateTimeFormatter.BASIC_ISO_DATE);
    String endDate = yearMonth.atEndOfMonth().format(DateTimeFormatter.BASIC_ISO_DATE);

    return medicalRecordRepository
        .findAllByChildAndTreatDateBetweenOrderByTreatDateDesc(child, startDate, endDate)
        .stream()
        .map(MedicalRecordSummaryResponse::from)
        .toList();
  }

  @Override
  public List<MedicalRecordSummaryResponse> getMedicalRecordsByDate(String date) {
    return medicalRecordRepository.findAllByTreatDateOrderByTreatDateDesc(date).stream()
        .map(MedicalRecordSummaryResponse::from)
        .toList();
  }

  @Override
  public MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId) {
    MedicalRecord record =
        medicalRecordRepository
            .findById(recordId)
            .orElseThrow(
                () -> new CustomException(MedicalRecordErrorCode.MEDICAL_RECORD_NOT_FOUND));

    return MedicalRecordDetailResponse.from(record);
  }
}

package com.springboot.iboribe.domain.medicalrecord.service;

import java.util.List;

import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordSummaryResponse;

public interface MedicalRecordQueryService {

  List<MedicalRecordSummaryResponse> getMedicalRecords(Long userId, int year, int month);

  List<MedicalRecordSummaryResponse> getMedicalRecordsByChild(
      Long userId, Long childId, int year, int month);

  List<MedicalRecordSummaryResponse> getMedicalRecordsByDate(Long userId, String date);

  MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId);
}

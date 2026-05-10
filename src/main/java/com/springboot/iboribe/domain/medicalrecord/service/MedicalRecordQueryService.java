package com.springboot.iboribe.domain.medicalrecord.service;

import java.util.List;

import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordSummaryResponse;

public interface MedicalRecordQueryService {

  List<MedicalRecordSummaryResponse> getMedicalRecords(int year, int month);

  List<MedicalRecordSummaryResponse> getMedicalRecordsByChild(Long childId, int year, int month);

  List<MedicalRecordSummaryResponse> getMedicalRecordsByDate(String date);

  MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId);
}

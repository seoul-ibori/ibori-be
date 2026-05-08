package com.springboot.iboribe.domain.medicalrecord.service;

import java.util.List;

import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordGroupResponse;

public interface MedicalRecordQueryService {

  List<MedicalRecordGroupResponse> getMedicalRecords(int year, int month);

  MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId);
}

package com.springboot.iboribe.domain.medicalrecord.service;

import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordCreateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordUpdateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordCreateResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordUpdateResponse;

public interface MedicalRecordCommandService {

  MedicalRecordCreateResponse createMedicalRecord(MedicalRecordCreateRequest request);

  MedicalRecordUpdateResponse updateMedicalRecord(
      Long recordId, MedicalRecordUpdateRequest request);

  void deleteMedicalRecord(Long recordId);
}

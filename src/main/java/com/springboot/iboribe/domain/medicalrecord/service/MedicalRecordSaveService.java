package com.springboot.iboribe.domain.medicalrecord.service;

import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;

public interface MedicalRecordSaveService {

  void saveFromCodefResponse(Long userId, CodefTreatmentResponse response);
}

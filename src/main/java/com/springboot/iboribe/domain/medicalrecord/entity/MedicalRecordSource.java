package com.springboot.iboribe.domain.medicalrecord.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MedicalRecordSource {
  CODEF("CODEF로 불러온 과거 진료 내역"),
  USER("사용자가 직접 등록한 진료 기록");

  private final String description;
}

package com.springboot.iboribe.domain.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.hospital.dto.response.HospitalResponse;
import com.springboot.iboribe.domain.hospital.entity.Hospital;
import com.springboot.iboribe.domain.hospital.exception.HospitalErrorCode;
import com.springboot.iboribe.domain.hospital.repository.HospitalRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {

  private final HospitalRepository hospitalRepository;

  // 병원 상세정보 단일 조회
  public HospitalResponse getHospital(Long hospitalId) {
    Hospital hospital =
        hospitalRepository
            .findById(hospitalId)
            .orElseThrow(() -> new CustomException(HospitalErrorCode.HOSPITAL_NOT_FOUND));

    return HospitalResponse.from(hospital);
  }

  // 병원 위도, 경도, 반경 별 병원 목록 조회
  public List<HospitalResponse> getHospitals(
      double lat, double lng, double radius, boolean nightOnly) {
    return hospitalRepository.findWithinRadius(lat, lng, radius, nightOnly).stream()
        .map(HospitalResponse::from)
        .toList();
  }
}

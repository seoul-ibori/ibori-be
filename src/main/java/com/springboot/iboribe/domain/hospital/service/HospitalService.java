package com.springboot.iboribe.domain.hospital.service;

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

  public HospitalResponse getHospital(Long hospitalId) {
    Hospital hospital =
        hospitalRepository
            .findById(hospitalId)
            .orElseThrow(() -> new CustomException(HospitalErrorCode.HOSPITAL_NOT_FOUND));

    return HospitalResponse.from(hospital);
  }
}

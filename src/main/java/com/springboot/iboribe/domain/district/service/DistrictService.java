package com.springboot.iboribe.domain.district.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.district.dto.response.DistrictResponse;
import com.springboot.iboribe.domain.hospital.repository.HospitalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictService {

  private final HospitalRepository hospitalRepository;

  public List<DistrictResponse> search(String keyword) {
    return hospitalRepository.searchDistricts(keyword);
  }
}

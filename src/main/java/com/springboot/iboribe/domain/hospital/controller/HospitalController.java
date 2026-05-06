package com.springboot.iboribe.domain.hospital.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.iboribe.domain.hospital.dto.response.HospitalResponse;
import com.springboot.iboribe.domain.hospital.service.HospitalService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
@Tag(name = "Hospital", description = "병원 API")
public class HospitalController {

  private final HospitalService hospitalService;

  @Operation(
      summary = "[토큰 X] 반경 내 병원 목록 조회",
      description =
          """
          **Parameters**  \n
          lat: 중심 위도  \n
          lng: 중심 경도  \n
          radius: 반경 (미터 단위, 예: 1000 = 1km)  \n
          nightOnly: 야간진료 병원만 필터링 여부 (기본값: false)  \n

          **Returns**  \n
          반경 내 병원 목록 (거리순 정렬)  \n
          """)
  @GetMapping
  public ResponseEntity<BaseResponse<List<HospitalResponse>>> getHospitals(
      @RequestParam double lat,
      @RequestParam double lng,
      @RequestParam double radius,
      @RequestParam(defaultValue = "false") boolean nightOnly) {

    return ResponseEntity.ok(
        BaseResponse.success(hospitalService.getHospitals(lat, lng, radius, nightOnly)));
  }

  @Operation(
      summary = "[토큰 X] 병원 상세 정보 단일 조회",
      description =
          """
          **Returns**  \n
          id: 병원 아이디  \n
          name: 병원 명  \n
          gu: 자치구 명  \n
          dong: 동 명  \n
          type: 병원 타입  \n
          tel: 전화번호  \n
          nightCare: 야간진료 여부 (true: 야간진료 O)  \n
          lat: 위도  \n
          lng: 경도  \n
          todayHours: 오늘 진료 시간 (예: 오전 9:00 ~ 오후 6:00)  \n
          weeklyHours.mon: 월 진료시간  \n
          weeklyHours.tue: 화 진료시간  \n
          weeklyHours.wed: 수 진료시간  \n
          weeklyHours.thu: 목 진료시간  \n
          weeklyHours.fri: 금 진료시간  \n
          weeklyHours.sat: 토 진료시간  \n
          weeklyHours.sun: 일 진료시간  \n
          weeklyHours.hol: 공휴일 진료시간 (휴진 시 "휴진" 반환)  \n
          """)
  @GetMapping("/{hospitalId}")
  public ResponseEntity<BaseResponse<HospitalResponse>> getHospital(@PathVariable Long hospitalId) {

    return ResponseEntity.ok(BaseResponse.success(hospitalService.getHospital(hospitalId)));
  }
}

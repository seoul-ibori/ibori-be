package com.springboot.iboribe.domain.medicalrecord.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordGroupResponse;
import com.springboot.iboribe.domain.medicalrecord.service.MedicalRecordQueryService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medical-records")
@Tag(name = "MedicalRecord", description = "의료 기록 조회 API")
public class MedicalRecordController {

  private final MedicalRecordQueryService medicalRecordQueryService;

  @Operation(
      summary = "[토큰 O] 가족 전체 의료 기록 조회",
      description =
          """
          **Description** \n
          로그인한 사용자의 가족 전체 의료 기록을 조회합니다. \n

          **기능** \n
          - 아이별 의료 기록 그룹화 \n
          - 연/월 기준 조회 가능 \n
          - 캘린더 화면에서 사용 가능 \n

          **Query Parameter** \n
          - year: 조회 연도 \n
          - month: 조회 월 \n

          **Example** \n
          GET /api/medical-records?year=2026&month=5
          """)
  @GetMapping
  public ResponseEntity<BaseResponse<List<MedicalRecordGroupResponse>>> getMedicalRecords(
      @RequestParam int year, @RequestParam int month) {

    List<MedicalRecordGroupResponse> response =
        medicalRecordQueryService.getMedicalRecords(year, month);

    return ResponseEntity.ok(BaseResponse.success(200, "가족 전체 의료 기록 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 의료 기록 상세 조회",
      description =
          """
          **Description** \n
          특정 의료 기록 상세 정보를 조회합니다. \n

          **기능** \n
          - 병원 정보 조회 \n
          - 진료 유형 조회 \n
          - 약 정보 조회 \n
          - 복용 일수 조회 \n
          """)
  @GetMapping("/{recordId}")
  public ResponseEntity<BaseResponse<MedicalRecordDetailResponse>> getMedicalRecordDetail(
      @PathVariable Long recordId) {

    MedicalRecordDetailResponse response =
        medicalRecordQueryService.getMedicalRecordDetail(recordId);

    return ResponseEntity.ok(BaseResponse.success(200, "의료 기록 상세 조회 성공", response));
  }
}

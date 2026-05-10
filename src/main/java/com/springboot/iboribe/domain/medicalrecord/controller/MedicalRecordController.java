package com.springboot.iboribe.domain.medicalrecord.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordCreateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.request.MedicalRecordUpdateRequest;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordCreateResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordSummaryResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordUpdateResponse;
import com.springboot.iboribe.domain.medicalrecord.service.MedicalRecordCommandService;
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
  private final MedicalRecordCommandService medicalRecordCommandService;

  @Operation(
      summary = "[토큰 O] 진료 내역 목록 조회",
      description =
          """
          **Description** \n
          진료 내역 목록을 조회합니다. \n
          쿼리 파라미터 조합에 따라 월별 전체, 아이별 월별, 날짜별 전체 조회를 하나의 API에서 처리합니다. \n

          **Query Parameter** \n
          - year: 조회 연도, 월별 조회 시 필수 \n
          - month: 조회 월, 월별 조회 시 필수 \n
          - childId: 자녀 ID, 아이별 조회 시 선택 \n
          - date: 조회 날짜 yyyyMMdd, 날짜별 조회 시 선택 \n

          **Example** \n
          - 월별 전체 조회: GET /api/medical-records?year=2026&month=5 \n
          - 아이별 월별 조회: GET /api/medical-records?year=2026&month=5&childId=1 \n
          - 날짜별 전체 조회: GET /api/medical-records?date=20260510 \n
          """)
  @GetMapping
  public ResponseEntity<BaseResponse<List<MedicalRecordSummaryResponse>>> getMedicalRecords(
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Long childId,
      @RequestParam(required = false) String date) {

    List<MedicalRecordSummaryResponse> response;

    if (date != null && !date.isBlank()) {
      response = medicalRecordQueryService.getMedicalRecordsByDate(date);
    } else if (childId != null) {
      response = medicalRecordQueryService.getMedicalRecordsByChild(childId, year, month);
    } else {
      response = medicalRecordQueryService.getMedicalRecords(year, month);
    }

    return ResponseEntity.ok(BaseResponse.success(200, "진료 내역 목록 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 의료 기록 상세 조회",
      description =
          """
          **Description** \n
          특정 의료 기록 상세 정보를 조회합니다. \n

          **기능** \n
          - 병원 정보 조회 \n
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

  @Operation(
      summary = "[토큰 O] 사용자 직접 진료 기록 생성",
      description =
          """
          **Description** \n
          사용자가 직접 진료 일정 또는 진료 기록을 생성합니다. \n

          **기능** \n
          - 자녀 선택 \n
          - 병원명 입력 \n
          - 진료 날짜 입력 \n
          - 생성된 기록은 USER 출처로 저장 \n
          - 이후 해당 기록에 녹음 기반 AI 요약 생성 가능 \n
          """)
  @PostMapping
  public ResponseEntity<BaseResponse<MedicalRecordCreateResponse>> createMedicalRecord(
      @Valid @RequestBody MedicalRecordCreateRequest request) {

    MedicalRecordCreateResponse response = medicalRecordCommandService.createMedicalRecord(request);

    return ResponseEntity.status(201).body(BaseResponse.success(201, "진료 기록 생성 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 사용자 직접 진료 기록 수정",
      description =
          """
          **Description** \n
          사용자가 직접 생성한 진료 기록의 내용을 수정합니다. \n
          보내지 않은 필드(null)는 기존 값이 유지됩니다. \n

          **수정 가능 필드** \n
          - 시간(treatTime) \n
          - 제목(title) \n
          - 병원명(hospitalName) \n
          - 자녀(childId) \n
          - 메모(memo) \n
          """)
  @PatchMapping("/{recordId}")
  public ResponseEntity<BaseResponse<MedicalRecordUpdateResponse>> updateMedicalRecord(
      @PathVariable Long recordId, @Valid @RequestBody MedicalRecordUpdateRequest request) {

    MedicalRecordUpdateResponse response =
        medicalRecordCommandService.updateMedicalRecord(recordId, request);

    return ResponseEntity.ok(BaseResponse.success(200, "진료 기록 수정 성공", response));
  }

  @Operation(
      summary = "[토큰 O] 진료 기록 삭제",
      description = """
          **Description** \n
          특정 진료 기록을 삭제합니다. \n
          """)
  @DeleteMapping("/{recordId}")
  public ResponseEntity<BaseResponse<Void>> deleteMedicalRecord(@PathVariable Long recordId) {

    medicalRecordCommandService.deleteMedicalRecord(recordId);

    return ResponseEntity.ok(BaseResponse.success(200, "진료 기록 삭제 성공", null));
  }
}

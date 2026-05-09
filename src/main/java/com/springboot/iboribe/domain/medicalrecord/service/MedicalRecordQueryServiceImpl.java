package com.springboot.iboribe.domain.medicalrecord.service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordDetailResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordGroupResponse;
import com.springboot.iboribe.domain.medicalrecord.dto.response.MedicalRecordSummaryResponse;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordQueryServiceImpl implements MedicalRecordQueryService {

  private final MedicalRecordRepository medicalRecordRepository;

  @Override
  public List<MedicalRecordGroupResponse> getMedicalRecords(int year, int month) {
    YearMonth yearMonth = YearMonth.of(year, month);

    String startDate = yearMonth.atDay(1).format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
    String endDate =
        yearMonth.atEndOfMonth().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);

    List<MedicalRecord> records =
        medicalRecordRepository.findAllByTreatDateBetweenOrderByTreatDateDesc(startDate, endDate);

    Map<Long, List<MedicalRecord>> groupedByChild =
        records.stream().collect(Collectors.groupingBy(record -> record.getChild().getId()));

    return groupedByChild.entrySet().stream()
        .map(
            entry -> {
              MedicalRecord firstRecord = entry.getValue().get(0);

              return MedicalRecordGroupResponse.builder()
                  .childId(firstRecord.getChild().getId())
                  .childName(firstRecord.getChild().getName())
                  .records(
                      entry.getValue().stream().map(MedicalRecordSummaryResponse::from).toList())
                  .build();
            })
        .toList();
  }

  @Override
  public MedicalRecordDetailResponse getMedicalRecordDetail(Long recordId) {
    MedicalRecord record =
        medicalRecordRepository
            .findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("의료 기록을 찾을 수 없습니다."));

    return MedicalRecordDetailResponse.from(record);
  }
}

package com.springboot.iboribe.domain.aisummary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

public interface AiSummaryRepository extends JpaRepository<AiSummary, Long> {

  Optional<AiSummary> findByMedicalRecord(MedicalRecord medicalRecord);

  boolean existsByMedicalRecord(MedicalRecord medicalRecord);

  boolean existsByMedicalRecordId(Long medicalRecordId);

  @Query(
      "SELECT a FROM AiSummary a"
          + " JOIN FETCH a.medicalRecord mr"
          + " JOIN FETCH mr.child"
          + " WHERE mr.id = :recordId")
  Optional<AiSummary> findByMedicalRecordId(@Param("recordId") Long recordId);

  @Modifying(clearAutomatically = true)
  @Query("DELETE FROM AiSummary a WHERE a.medicalRecord.id = :recordId")
  void deleteByMedicalRecordId(@Param("recordId") Long recordId);
}

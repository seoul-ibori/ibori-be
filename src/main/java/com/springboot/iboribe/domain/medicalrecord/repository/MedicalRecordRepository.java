package com.springboot.iboribe.domain.medicalrecord.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

  Optional<MedicalRecord> findByChildAndHospitalNameAndTreatDate(
      Child child, String hospitalName, String treatDate);

  boolean existsByChildAndHospitalNameAndTreatDate(
      Child child, String hospitalName, String treatDate);

  @Query(
      "SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.aiSummary"
          + " WHERE mr.treatDate BETWEEN :startDate AND :endDate"
          + " ORDER BY mr.treatDate DESC")
  List<MedicalRecord> findAllByTreatDateBetweenOrderByTreatDateDesc(
      @Param("startDate") String startDate, @Param("endDate") String endDate);

  @Query(
      "SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.aiSummary"
          + " WHERE mr.child = :child AND mr.treatDate BETWEEN :startDate AND :endDate"
          + " ORDER BY mr.treatDate DESC")
  List<MedicalRecord> findAllByChildAndTreatDateBetweenOrderByTreatDateDesc(
      @Param("child") Child child,
      @Param("startDate") String startDate,
      @Param("endDate") String endDate);

  @Query(
      "SELECT mr FROM MedicalRecord mr LEFT JOIN FETCH mr.aiSummary"
          + " WHERE mr.treatDate = :treatDate"
          + " ORDER BY mr.treatDate DESC")
  List<MedicalRecord> findAllByTreatDateOrderByTreatDateDesc(@Param("treatDate") String treatDate);
}

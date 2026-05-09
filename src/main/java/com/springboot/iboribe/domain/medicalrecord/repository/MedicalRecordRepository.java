package com.springboot.iboribe.domain.medicalrecord.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

  Optional<MedicalRecord> findByChildAndHospitalNameAndTreatDateAndTreatType(
      Child child, String hospitalName, String treatDate, String treatType);

  List<MedicalRecord> findAllByTreatDateBetweenOrderByTreatDateDesc(
      String startDate, String endDate);
}

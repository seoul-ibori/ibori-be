package com.springboot.iboribe.domain.medicalrecord.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.codef.dto.response.CodefMedicalRecordResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;
import com.springboot.iboribe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordSaveServiceImpl implements MedicalRecordSaveService {

  private final UserRepository userRepository;
  private final ChildRepository childRepository;
  private final MedicalRecordRepository medicalRecordRepository;

  @Override
  public void saveFromCodefResponse(Long userId, CodefTreatmentResponse response) {
    if (response.getMedicalRecords() == null || response.getMedicalRecords().isEmpty()) {
      return;
    }

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    Family family = user.getFamily();

    for (CodefMedicalRecordResponse record : response.getMedicalRecords()) {
      if (record.getSubjectName() == null || "본인".equals(record.getSubjectName())) {
        continue;
      }

      Child child =
          childRepository
              .findByFamilyAndName(family, record.getSubjectName())
              .orElseGet(
                  () ->
                      childRepository.save(
                          Child.builder().family(family).name(record.getSubjectName()).build()));

      boolean alreadyExists =
          medicalRecordRepository
              .findByChildAndHospitalNameAndTreatDateAndTreatType(
                  child, record.getHospitalName(), record.getTreatDate(), record.getTreatType())
              .isPresent();

      if (alreadyExists) {
        continue;
      }

      medicalRecordRepository.save(
          MedicalRecord.builder()
              .child(child)
              .hospitalName(record.getHospitalName())
              .treatDate(record.getTreatDate())
              .treatType(record.getTreatType())
              .medications(record.getMedications())
              .build());
    }
  }
}

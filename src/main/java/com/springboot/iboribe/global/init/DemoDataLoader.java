package com.springboot.iboribe.global.init;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.child.entity.ChildProfileColor;
import com.springboot.iboribe.domain.child.repository.ChildRepository;
import com.springboot.iboribe.domain.codef.dto.response.CodefMedicationResponse;
import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.entity.FamilyRole;
import com.springboot.iboribe.domain.family.repository.FamilyRepository;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;
import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecordSource;
import com.springboot.iboribe.domain.medicalrecord.repository.MedicalRecordRepository;
import com.springboot.iboribe.domain.user.entity.User;
import com.springboot.iboribe.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final FamilyRepository familyRepository;
  private final ChildRepository childRepository;
  private final MedicalRecordRepository medicalRecordRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) {
    log.info("데모 데이터 초기화 시작...");

    Family family = getOrCreateDemoFamily();
    createDemoUsersIfNotExists(family);

    Child jiAn = getOrCreateChild(family, "박지안");
    Child seoYeon = getOrCreateChild(family, "박서연");

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "연세이비인후과의원",
        "20251203",
        "일반외래",
        List.of(createMedication("20251203", "외래", "부루펜시럽", "해열, 진통, 소염제", "3")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "연세웰이비인후과의원",
        "20251218",
        "일반외래",
        List.of(
            createMedication("20251218", "외래", "코미시럽", "항히스타민제", "3"),
            createMedication("20251218", "외래", "뮤코졸정", "진해거담제", "3")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "강북삼성병원",
        "20260115",
        "일반외래",
        List.of(createMedication("20260115", "외래", "타이레놀현탁액", "해열진통제", "2")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "서울센트럴이비인후과의원",
        "20260307",
        "일반외래",
        List.of(
            createMedication("20260307", "외래", "시네츄라시럽", "진해거담제", "4"),
            createMedication("20260307", "외래", "비오플250산", "정장제", "4")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "엠메디칼약국[의정부시 충의로]",
        "20260331",
        "처방조제",
        List.of(
            createMedication(
                "20260331", "약국", "시네츄라시럽(500mL) (Synatura Syrup(500mL))", "진해거담제", "4"),
            createMedication(
                "20260331", "약국", "신일브롬헥신염산염정 (Bromhexine HCl Tab. Sinil)", "진해거담제", "4"),
            createMedication("20260331", "약국", "비오플250산 (Bioflor 250 Powder)", "정장제", "4"),
            createMedication("20260331", "약국", "페니라민정 (Peniramin Tab.)", "항히스타민제", "4")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "엠메디칼약국[의정부시 충의로]",
        "20260327",
        "처방조제",
        List.of(
            createMedication(
                "20260327", "약국", "움카민시럽(500mL) (Umckamin Syrup(500mL))", "기타의 호흡기관용약", "4"),
            createMedication("20260327", "약국", "뮤코졸정 (Mucosol Tab.)", "진해거담제", "4"),
            createMedication("20260327", "약국", "코미시럽 (Comy Syrup)", "항히스타민제", "4")));

    createCodefMedicalRecordIfNotExists(
        jiAn,
        "삼성튼튼소아청소년과의원",
        "20260418",
        null,
        List.of(createMedication("20260418", "외래", "움카민시럽", "기타의 호흡기관용약", "4")));

    createCodefMedicalRecordIfNotExists(
        seoYeon,
        "연세우리소아과의원",
        "20251212",
        "일반외래",
        List.of(createMedication("20251212", "외래", "암브록솔시럽", "진해거담제", "3")));

    createCodefMedicalRecordIfNotExists(
        seoYeon,
        "정소아과의원",
        "20260128",
        "일반외래",
        List.of(
            createMedication("20260128", "외래", "페니라민정", "항히스타민제", "2"),
            createMedication("20260128", "외래", "비오플250산", "정장제", "2")));

    createCodefMedicalRecordIfNotExists(
        seoYeon,
        "서울적십자병원",
        "20260219",
        "일반외래",
        List.of(createMedication("20260219", "외래", "부루펜시럽", "해열, 진통, 소염제", "3")));

    createCodefMedicalRecordIfNotExists(
        seoYeon,
        "두리이비인후과의원",
        "20260331",
        "일반외래",
        List.of(
            createMedication("20260331", "외래", "시네츄라시럽", "진해거담제", "4"),
            createMedication("20260331", "외래", "코미시럽", "항히스타민제", "4")));

    createCodefMedicalRecordIfNotExists(
        seoYeon,
        "의료법인소화의원",
        "20260423",
        null,
        List.of(createMedication("20260423", "외래", "타이레놀현탁액", "해열진통제", "2")));

    createUserMedicalRecordIfNotExists(
        jiAn, "소아과 정기 검진", "광화문하나이비인후과의원", "20260515", "14:30", "기침 증상 확인 예정");

    createUserMedicalRecordIfNotExists(
        seoYeon, "예방접종 일정", "연세우리소아과의원", "20260520", "10:00", "예방접종 예정");

    log.info("데모 데이터 초기화 완료");
  }

  private Family getOrCreateDemoFamily() {
    return familyRepository
        .findByFamilyCode("YoonFamily2026!")
        .orElseGet(
            () -> familyRepository.save(Family.builder().familyCode("YoonFamily2026!").build()));
  }

  private void createDemoUsersIfNotExists(Family family) {
    if (!userRepository.existsByUsername("mom_jiyoon")) {
      User mother =
          userRepository.save(
              User.builder()
                  .name("김지윤")
                  .username("mom_jiyoon")
                  .password(passwordEncoder.encode("Demo!1234"))
                  .family(family)
                  .familyRole(FamilyRole.MOTHER)
                  .build());

      log.info("데모 엄마 계정 생성 완료 - userId: {}", mother.getId());
    }

    if (!userRepository.existsByUsername("dad_hyunwoo")) {
      User father =
          userRepository.save(
              User.builder()
                  .name("박현우")
                  .username("dad_hyunwoo")
                  .password(passwordEncoder.encode("Demo!1234"))
                  .family(family)
                  .familyRole(FamilyRole.FATHER)
                  .build());

      log.info("데모 아빠 계정 생성 완료 - userId: {}", father.getId());
    }
  }

  private Child getOrCreateChild(Family family, String childName) {
    return childRepository
        .findByFamilyAndName(family, childName)
        .orElseGet(
            () ->
                childRepository.save(
                    Child.builder()
                        .family(family)
                        .name(childName)
                        .familyRole(FamilyRole.CHILD)
                        .profileColor(ChildProfileColor.random())
                        .build()));
  }

  private void createCodefMedicalRecordIfNotExists(
      Child child,
      String hospitalName,
      String treatDate,
      String treatType,
      List<CodefMedicationResponse> medications) {
    createMedicalRecordIfNotExists(
        child,
        null,
        hospitalName,
        treatDate,
        null,
        treatType,
        null,
        medications,
        MedicalRecordSource.CODEF);
  }

  private void createUserMedicalRecordIfNotExists(
      Child child,
      String title,
      String hospitalName,
      String treatDate,
      String treatTime,
      String memo) {
    createMedicalRecordIfNotExists(
        child,
        title,
        hospitalName,
        treatDate,
        treatTime,
        null,
        memo,
        List.of(),
        MedicalRecordSource.USER);
  }

  private void createMedicalRecordIfNotExists(
      Child child,
      String title,
      String hospitalName,
      String treatDate,
      String treatTime,
      String treatType,
      String memo,
      List<CodefMedicationResponse> medications,
      MedicalRecordSource source) {
    boolean alreadyExists =
        medicalRecordRepository.existsByChildAndHospitalNameAndTreatDate(
            child, hospitalName, treatDate);

    if (alreadyExists) {
      return;
    }

    medicalRecordRepository.save(
        MedicalRecord.builder()
            .child(child)
            .title(title)
            .hospitalName(hospitalName)
            .treatDate(treatDate)
            .treatTime(treatTime)
            .memo(memo)
            .medications(medications)
            .source(source)
            .build());
  }

  private CodefMedicationResponse createMedication(
      String treatDate,
      String treatTypeDetail,
      String drugName,
      String drugEffect,
      String prescribeDays) {
    return CodefMedicationResponse.builder()
        .treatDate(treatDate)
        .treatTypeDetail(treatTypeDetail)
        .drugName(drugName)
        .drugEffect(drugEffect)
        .prescribeDays(prescribeDays)
        .build();
  }
}

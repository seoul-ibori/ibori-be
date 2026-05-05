package com.springboot.iboribe.global.init;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.springboot.iboribe.domain.hospital.repository.HospitalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HospitalDataLoader implements CommandLineRunner {

  private final HospitalRepository hospitalRepository;
  private final JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {
    if (hospitalRepository.count() > 0) {
      log.info("병원 데이터가 이미 존재합니다. 마이그레이션을 건너뜁니다.");
      return;
    }

    log.info("병원 데이터 마이그레이션 시작...");

    ClassPathResource resource = new ClassPathResource("hospitals.csv");
    List<Object[]> batchArgs = new ArrayList<>();

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

      reader.readLine(); // 헤더(BOM 포함) 스킵

      String line;
      while ((line = reader.readLine()) != null) {
        String[] cols = line.split(",");
        batchArgs.add(
            new Object[] {
              cols[0].trim(), // name
              cols[1].trim(), // gu
              cols[2].trim(), // dong
              cols[3].trim(), // type
              cols[22].trim(), // tel
              "True".equalsIgnoreCase(cols[23].trim()), // night_care
              parseDouble(cols[4]), // mon_s
              parseDouble(cols[5]), // mon_e
              parseDouble(cols[6]), // tue_s
              parseDouble(cols[7]), // tue_e
              parseDouble(cols[8]), // wed_s
              parseDouble(cols[9]), // wed_e
              parseDouble(cols[10]), // thu_s
              parseDouble(cols[11]), // thu_e
              parseDouble(cols[12]), // fri_s
              parseDouble(cols[13]), // fri_e
              parseDouble(cols[14]), // sat_s
              parseDouble(cols[15]), // sat_e
              parseDouble(cols[16]), // sun_s
              parseDouble(cols[17]), // sun_e
              parseDouble(cols[18]), // hol_s
              parseDouble(cols[19]), // hol_e
              parseDouble(cols[21]), // lat (위도, index 21)
              parseDouble(cols[20]), // lng (경도, index 20)
            });
      }
    }

    String sql =
        """
        INSERT INTO hospitals (name, gu, dong, type, tel, night_care,
          mon_s, mon_e, tue_s, tue_e, wed_s, wed_e, thu_s, thu_e,
          fri_s, fri_e, sat_s, sat_e, sun_s, sun_e, hol_s, hol_e,
          lat, lng)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    jdbcTemplate.batchUpdate(sql, batchArgs);
    log.info("병원 데이터 마이그레이션 완료: {}건", batchArgs.size());
  }

  private Double parseDouble(String value) {
    if (value == null || value.trim().isEmpty()) return null;
    return Double.parseDouble(value.trim());
  }
}

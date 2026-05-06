package com.springboot.iboribe.domain.hospital.dto.response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

import com.springboot.iboribe.domain.hospital.entity.Hospital;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "HospitalResponse: 병원 조회 응답 DTO")
public class HospitalResponse {

  @Schema(description = "병원 ID", example = "1")
  private Long id;

  @Schema(description = "기관명", example = "서울소아청소년과의원")
  private String name;

  @Schema(description = "구", example = "강남구")
  private String gu;

  @Schema(description = "동", example = "역삼동")
  private String dong;

  @Schema(description = "요양종별", example = "의원")
  private String type;

  @Schema(description = "전화번호", example = "02-1234-5678")
  private String tel;

  @Schema(description = "야간진료 여부", example = "true")
  private Boolean nightCare;

  @Schema(description = "위도", example = "37.5012")
  private Double lat;

  @Schema(description = "경도", example = "127.0396")
  private Double lng;

  @Schema(description = "오늘 진료 시간", example = "오전 9:00 ~ 오후 6:00")
  private String todayHours;

  @Schema(description = "요일별 진료 시간")
  private WeeklyHours weeklyHours;

  @Getter
  @Builder
  public static class WeeklyHours {
    private String mon;
    private String tue;
    private String wed;
    private String thu;
    private String fri;
    private String sat;
    private String sun;
    private String hol;
  }

  public static HospitalResponse from(Hospital hospital) {
    WeeklyHours weekly =
        WeeklyHours.builder()
            .mon(formatRange(hospital.getMonS(), hospital.getMonE()))
            .tue(formatRange(hospital.getTueS(), hospital.getTueE()))
            .wed(formatRange(hospital.getWedS(), hospital.getWedE()))
            .thu(formatRange(hospital.getThuS(), hospital.getThuE()))
            .fri(formatRange(hospital.getFriS(), hospital.getFriE()))
            .sat(formatRange(hospital.getSatS(), hospital.getSatE()))
            .sun(formatRange(hospital.getSunS(), hospital.getSunE()))
            .hol(formatRange(hospital.getHolS(), hospital.getHolE()))
            .build();

    return HospitalResponse.builder()
        .id(hospital.getId())
        .name(hospital.getName())
        .gu(hospital.getGu())
        .dong(hospital.getDong())
        .type(hospital.getType())
        .tel(hospital.getTel())
        .nightCare(hospital.getNightCare())
        .lat(hospital.getLat())
        .lng(hospital.getLng())
        .todayHours(resolveTodayHours(hospital, weekly))
        .weeklyHours(weekly)
        .build();
  }

  private static String resolveTodayHours(Hospital hospital, WeeklyHours weekly) {
    DayOfWeek today = LocalDate.now(ZoneId.of("Asia/Seoul")).getDayOfWeek();
    return switch (today) {
      case MONDAY -> weekly.getMon();
      case TUESDAY -> weekly.getTue();
      case WEDNESDAY -> weekly.getWed();
      case THURSDAY -> weekly.getThu();
      case FRIDAY -> weekly.getFri();
      case SATURDAY -> weekly.getSat();
      case SUNDAY -> weekly.getSun();
    };
  }

  private static String formatRange(Double start, Double end) {
    if (start == null || end == null) return "휴진";
    return formatTime(start) + " ~ " + formatTime(end);
  }

  private static String formatTime(Double value) {
    int total = value.intValue();
    int hours = total / 100;
    int minutes = total % 100;
    String period = hours < 12 ? "오전" : "오후";
    int display = hours == 0 ? 12 : (hours > 12 ? hours - 12 : hours);
    return String.format("%s %d:%02d", period, display, minutes);
  }
}

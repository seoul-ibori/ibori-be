package com.springboot.iboribe.domain.hospital.dto.response;

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

  @Schema(description = "월요일 진료 시작 (예: 900.0 = 09:00)")
  private Double monS;

  @Schema(description = "월요일 진료 종료")
  private Double monE;

  @Schema(description = "화요일 진료 시작")
  private Double tueS;

  @Schema(description = "화요일 진료 종료")
  private Double tueE;

  @Schema(description = "수요일 진료 시작")
  private Double wedS;

  @Schema(description = "수요일 진료 종료")
  private Double wedE;

  @Schema(description = "목요일 진료 시작")
  private Double thuS;

  @Schema(description = "목요일 진료 종료")
  private Double thuE;

  @Schema(description = "금요일 진료 시작")
  private Double friS;

  @Schema(description = "금요일 진료 종료")
  private Double friE;

  @Schema(description = "토요일 진료 시작")
  private Double satS;

  @Schema(description = "토요일 진료 종료")
  private Double satE;

  @Schema(description = "일요일 진료 시작")
  private Double sunS;

  @Schema(description = "일요일 진료 종료")
  private Double sunE;

  @Schema(description = "공휴일 진료 시작")
  private Double holS;

  @Schema(description = "공휴일 진료 종료")
  private Double holE;

  public static HospitalResponse from(Hospital hospital) {
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
        .monS(hospital.getMonS())
        .monE(hospital.getMonE())
        .tueS(hospital.getTueS())
        .tueE(hospital.getTueE())
        .wedS(hospital.getWedS())
        .wedE(hospital.getWedE())
        .thuS(hospital.getThuS())
        .thuE(hospital.getThuE())
        .friS(hospital.getFriS())
        .friE(hospital.getFriE())
        .satS(hospital.getSatS())
        .satE(hospital.getSatE())
        .sunS(hospital.getSunS())
        .sunE(hospital.getSunE())
        .holS(hospital.getHolS())
        .holE(hospital.getHolE())
        .build();
  }
}

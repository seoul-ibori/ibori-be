package com.springboot.iboribe.domain.codef.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "CodefTreatmentRequest: CODEF 진료 및 투약정보 1차 요청 DTO")
public class CodefTreatmentRequest {

  @NotBlank(message = "기관코드는 필수입니다.")
  @Schema(description = "기관코드", example = "0002")
  private String organization;

  @NotBlank(message = "로그인 구분은 필수입니다.")
  @Schema(description = "로그인 구분, 간편인증은 5", example = "5")
  private String loginType;

  @NotBlank(message = "간편인증 로그인 구분은 필수입니다.")
  @Schema(description = "1:카카오톡, 3:삼성패스, 4:KB모바일, 5:통신사, 6:네이버, 8:toss", example = "1")
  private String loginTypeLevel;

  @NotBlank(message = "사용자 이름은 필수입니다.")
  @Schema(description = "사용자 이름", example = "신채린")
  private String userName;

  @NotBlank(message = "생년월일은 필수입니다.")
  @Schema(description = "생년월일 YYYYMMDD", example = "20030709")
  private String identity;

  @NotBlank(message = "전화번호는 필수입니다.")
  @Schema(description = "전화번호", example = "01012345678")
  private String phoneNo;

  @Schema(description = "통신사, loginTypeLevel=5일 때 필수", example = "0")
  private String telecom;

  @Schema(description = "요청 식별 아이디", example = "user1-uuid")
  private String id;

  @Schema(description = "시작일자 yyyyMMdd", example = "20250101")
  private String startDate;

  @Schema(description = "종료일자 yyyyMMdd", example = "20250505")
  private String endDate;

  @Schema(description = "조회대상, 0:전체 / 1:본인 / 2:영유아", example = "1")
  private String type;

  @Schema(description = "약품이미지 포함 여부, 1:포함 / 0:미포함", example = "0")
  private String drugImageYN;

  @Schema(description = "복약지도 포함 여부, 1:포함 / 0:미포함", example = "0")
  private String medicationDirectionYN;

  @Schema(description = "상세 포함 여부, 1:포함 / 0:미포함", example = "0")
  private String detailYN;

  @Schema(description = "보안숫자 제한시간", example = "170")
  private String timeOut;
}

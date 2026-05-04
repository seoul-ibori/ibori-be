package com.springboot.iboribe.domain.codef.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "CodefChildRegister2WayRequest: CODEF 자녀 등록 2차 요청 DTO")
public class CodefChildRegister2WayRequest {

  @NotBlank(message = "기관코드는 필수입니다.")
  @Schema(description = "기관코드", example = "0002")
  private String organization;

  @NotBlank(message = "로그인 구분은 필수입니다.")
  @Schema(description = "로그인 구분, 간편인증은 5", example = "5")
  private String loginType;

  @NotBlank(message = "간편인증 로그인 구분은 필수입니다.")
  @Schema(description = "1:카카오톡, 3:삼성패스, 4:KB모바일인증서, 5:통신사인증서, 10:NH인증서", example = "1")
  private String loginTypeLevel;

  @NotBlank(message = "사용자 이름은 필수입니다.")
  @Schema(description = "보호자 이름", example = "김복남")
  private String userName;

  @NotBlank(message = "생년월일은 필수입니다.")
  @Schema(description = "보호자 생년월일 YYYYMMDD", example = "19990101")
  private String identity;

  @NotBlank(message = "전화번호는 필수입니다.")
  @Schema(description = "보호자 전화번호", example = "01012345678")
  private String phoneNo;

  @Schema(description = "통신사, loginTypeLevel=5일 때 필수", example = "0")
  private String telecom;

  @Schema(description = "요청 식별 아이디", example = "user1-uuid")
  private String id;

  @NotBlank(message = "간편인증 완료 여부는 필수입니다.")
  @Schema(description = "간편인증 완료 여부, 1: 확인", example = "1")
  private String simpleAuth;

  @NotBlank(message = "자녀 이름은 필수입니다.")
  @Schema(description = "등록할 영유아 이름", example = "김아기")
  private String childName;

  @NotNull(message = "추가 인증 정보는 필수입니다.")
  @Schema(description = "1차 요청 응답의 twoWayInfo")
  private Map<String, Object> twoWayInfo;
}

package com.springboot.iboribe.domain.codef.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "CodefTreatment2WayRequest: CODEF 진료 및 투약정보 2차 요청 DTO")
public class CodefTreatment2WayRequest {

  @NotBlank(message = "기관코드는 필수입니다.")
  private String organization;

  @NotBlank(message = "로그인 구분은 필수입니다.")
  private String loginType;

  @NotBlank(message = "간편인증 로그인 구분은 필수입니다.")
  private String loginTypeLevel;

  @NotBlank(message = "사용자 이름은 필수입니다.")
  private String userName;

  @NotBlank(message = "생년월일은 필수입니다.")
  private String identity;

  @NotBlank(message = "전화번호는 필수입니다.")
  private String phoneNo;

  private String telecom;
  private String id;
  private String startDate;
  private String endDate;
  private String type;
  private String drugImageYN;
  private String medicationDirectionYN;
  private String detailYN;
  private String timeOut;

  @Schema(description = "간편인증 완료 여부, 1: 확인", example = "1")
  private String simpleAuth;

  @Schema(description = "보안문자, 자동 인식 실패 시 입력")
  private String secureNo;

  @Schema(description = "보안문자 새로고침, 0:기본 / 1:재요청 / 2:입력취소", example = "0")
  private String secureNoRefresh;

  @NotNull(message = "추가 인증 정보는 필수입니다.")
  private Map<String, Object> twoWayInfo;
}

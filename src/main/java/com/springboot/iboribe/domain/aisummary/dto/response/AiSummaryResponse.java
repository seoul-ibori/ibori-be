package com.springboot.iboribe.domain.aisummary.dto.response;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "AiSummaryResponse: AI 진료 요약 응답 DTO")
public class AiSummaryResponse {

  @Schema(description = "AI 요약 ID", example = "1")
  private Long summaryId;

  @Schema(description = "자녀 ID", example = "2")
  private Long childId;

  @Schema(description = "진료 기록 ID", example = "10")
  private Long recordId;

  @Schema(description = "AI의 한 줄 요약", example = "예방접종으로 인해 미열이 날 수 있어 지켜보고, 다음 접종은 4주 뒤예요.")
  private String oneLineSummary;

  @Schema(
      description = "진료 요약",
      example = "DTaP(디프테리아/파상풍/백일해) 예방접종을 완료했으며, 접종 후 2~3일 정도 미열이 발생할 수 있습니다.")
  private String medicalSummary;

  @Schema(description = "주의사항", example = "열이 나면 해열제 복용이 가능하며, 접종 부위가 붓거나 아플 수 있지만 자연스러운 반응입니다.")
  private String caution;

  @Schema(description = "다음 일정", example = "4주 뒤 추가 예방접종이 필요합니다.")
  private String nextSchedule;

  public static AiSummaryResponse from(AiSummary aiSummary) {
    return AiSummaryResponse.builder()
        .summaryId(aiSummary.getId())
        .childId(aiSummary.getMedicalRecord().getChild().getId())
        .recordId(aiSummary.getMedicalRecord().getId())
        .oneLineSummary(aiSummary.getOneLineSummary())
        .medicalSummary(aiSummary.getMedicalSummary())
        .caution(aiSummary.getCaution())
        .nextSchedule(aiSummary.getNextSchedule())
        .build();
  }
}

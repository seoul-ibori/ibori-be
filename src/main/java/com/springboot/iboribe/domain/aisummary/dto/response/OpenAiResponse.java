package com.springboot.iboribe.domain.aisummary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "OpenAiResponse: OpenAI 응답 DTO")
public class OpenAiResponse {

  @Schema(description = "OpenAI 응답 choices 목록")
  private Choice[] choices;

  @Getter
  @Schema(title = "OpenAiChoice: OpenAI 선택 응답")
  public static class Choice {

    @Schema(description = "AI 메시지 정보")
    private Message message;
  }

  @Getter
  @Schema(title = "OpenAiMessage: OpenAI 메시지 응답")
  public static class Message {

    @Schema(
        description = "AI가 생성한 응답 내용",
        example =
            "AI의 한 줄 요약: 예방접종 후 미열이 발생할 수 있으며 다음 접종은 4주 뒤 예정입니다.\n\n진료 요약: DTaP 예방접종을 완료했고 접종 후 2~3일 정도 미열이 나타날 수 있습니다.\n\n주의사항: 열이 나면 해열제 복용이 가능하며 접종 부위 통증은 자연스러운 반응입니다.\n\n다음 일정: 4주 뒤 추가 예방접종 권장")
    private String content;
  }
}

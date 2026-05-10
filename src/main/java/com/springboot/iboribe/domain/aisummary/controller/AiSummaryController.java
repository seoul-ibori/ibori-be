package com.springboot.iboribe.domain.aisummary.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.iboribe.domain.aisummary.dto.request.AiSummaryAudioMultipartBody;
import com.springboot.iboribe.domain.aisummary.dto.response.AiSummaryResponse;
import com.springboot.iboribe.domain.aisummary.service.AiSummaryService;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai-summaries")
@Tag(name = "AiSummary", description = "AI 진료 요약 API")
public class AiSummaryController {

  private final AiSummaryService aiSummaryService;

  @Operation(
      summary = "[토큰 O] 녹음 파일 기반 AI 진료 요약 생성",
      description =
          """
          **Description**  \n
          진료 녹음 파일을 업로드하여 AI 요약을 생성합니다.  \n

          **Parameters**  \n
          childId: 자녀 ID (recordId 없을 때 필수)  \n
          recordId: 기존 진료 기록 ID (선택 - 기존 진료 내역에 연결할 경우에만 입력, 있으면 childId 불필요)  \n
          audioFile: 진료 녹음 오디오 파일  \n

          **Process**  \n
          - recordId 없으면 AI 소스 진료 기록 자동 생성  \n
          - 오디오 파일 업로드  \n
          - OpenAI Whisper/STT로 텍스트 변환  \n
          - 변환된 텍스트를 AI 요약  \n
          - 한줄요약, 진료요약, 주의사항, 다음일정 저장  \n

          **Returns**  \n
          summaryId, childId, recordId  \n
          oneLineSummary, medicalSummary, caution, nextSchedule  \n
          """,
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                      schema = @Schema(implementation = AiSummaryAudioMultipartBody.class))))
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BaseResponse<AiSummaryResponse>> generateSummaryFromAudio(
      @RequestParam(required = false) Long childId,
      @RequestParam(required = false) Long recordId,
      @RequestPart(value = "audioFile") MultipartFile audioFile,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    AiSummaryResponse response =
        aiSummaryService.generateSummaryFromAudio(
            childId, recordId, audioFile, userDetails.getUserId());

    return ResponseEntity.ok(BaseResponse.success(200, "AI 진료 요약 생성 성공", response));
  }

  @Operation(
      summary = "[토큰 O] AI 진료 요약 조회",
      description =
          """
          **Description**  \n
          특정 의료 기록의 AI 요약을 조회합니다.  \n

          **Parameters**  \n
          recordId: 조회할 의료 기록 ID  \n

          **Returns**  \n
          summaryId, childId, recordId  \n
          oneLineSummary, medicalSummary, caution, nextSchedule  \n
          """)
  @GetMapping("/{recordId}")
  public ResponseEntity<BaseResponse<AiSummaryResponse>> getAiSummary(@PathVariable Long recordId) {

    AiSummaryResponse response = aiSummaryService.getAiSummary(recordId);

    return ResponseEntity.ok(BaseResponse.success(200, "AI 진료 요약 조회 성공", response));
  }

  @Operation(
      summary = "[토큰 O] AI 진료 요약 삭제",
      description =
          """
          **Description**  \n
          특정 의료 기록의 AI 요약을 삭제합니다.  \n

          **Parameters**  \n
          recordId: 삭제할 AI 요약이 연결된 의료 기록 ID  \n
          """)
  @DeleteMapping("/{recordId}")
  public ResponseEntity<BaseResponse<Void>> deleteAiSummary(@PathVariable Long recordId) {

    aiSummaryService.deleteAiSummary(recordId);

    return ResponseEntity.ok(BaseResponse.success(200, "AI 진료 요약 삭제 성공", null));
  }
}

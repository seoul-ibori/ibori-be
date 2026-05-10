package com.springboot.iboribe.domain.aisummary.dto.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "AiSummaryAudioMultipartBody: AI 요약 오디오 업로드 요청")
public class AiSummaryAudioMultipartBody {

  @Schema(description = "진료 녹음 오디오 파일", type = "string", format = "binary")
  private MultipartFile audioFile;
}

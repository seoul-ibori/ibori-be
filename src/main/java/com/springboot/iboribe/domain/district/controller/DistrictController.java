package com.springboot.iboribe.domain.district.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.iboribe.domain.district.dto.response.DistrictResponse;
import com.springboot.iboribe.domain.district.service.DistrictService;
import com.springboot.iboribe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/district")
@Tag(name = "District", description = "동 검색 API")
public class DistrictController {

  private final DistrictService districtService;

  @Operation(
      summary = "[토큰 X] 동 키워드 검색",
      description =
          """
          **Parameters**  \n
          keyword: 검색할 동 키워드 (예: 잠실)  \n

          **Returns**  \n
          gu: 구 이름  \n
          dong: 동 이름  \n
          displayName: 표시 이름 (예: 서울시 송파구 잠실동)  \n
          """)
  @GetMapping("/search")
  public ResponseEntity<BaseResponse<List<DistrictResponse>>> search(@RequestParam String keyword) {

    return ResponseEntity.ok(BaseResponse.success(districtService.search(keyword)));
  }
}

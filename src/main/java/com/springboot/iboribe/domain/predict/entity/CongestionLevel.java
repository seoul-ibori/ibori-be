package com.springboot.iboribe.domain.predict.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CongestionLevel {
  여유,
  보통,
  혼잡,
  매우혼잡;

  @JsonCreator
  public static CongestionLevel from(String value) {
    for (CongestionLevel level : values()) {
      if (level.name().equals(value)) {
        return level;
      }
    }
    return null;
  }
}

package com.springboot.iboribe.domain.child.entity;

import java.util.Random;

public enum ChildProfileColor {
  RED,
  ORANGE,
  YELLOW,
  GREEN,
  SKY_BLUE,
  BLUE,
  PURPLE,
  PINK;

  private static final ChildProfileColor[] VALUES = values();
  private static final Random RANDOM = new Random();

  public static ChildProfileColor random() {
    return VALUES[RANDOM.nextInt(VALUES.length)];
  }
}

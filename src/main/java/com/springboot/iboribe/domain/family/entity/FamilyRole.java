package com.springboot.iboribe.domain.family.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FamilyRole {
  CHILD("아이"),
  MOTHER("엄마"),
  FATHER("아빠"),
  GRANDMOTHER("할머니"),
  GRANDFATHER("할아버지"),
  SIBLING("형제자매"),
  RELATIVE("친척");

  private final String description;
}

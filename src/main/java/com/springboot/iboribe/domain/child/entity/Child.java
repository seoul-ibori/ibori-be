package com.springboot.iboribe.domain.child.entity;

import jakarta.persistence.*;

import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.entity.FamilyRole;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
    name = "children",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"family_id", "name"})})
public class Child {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 30)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FamilyRole familyRole;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ChildProfileColor profileColor;

  @Column(length = 8)
  private String birthDate;

  @Column(length = 30)
  private String nickname;

  private Double height;

  private Double weight;

  @Column(columnDefinition = "TEXT")
  private String memo;

  public void update(
      ChildProfileColor profileColor,
      String birthDate,
      String nickname,
      Double height,
      Double weight,
      String memo) {
    if (profileColor != null) this.profileColor = profileColor;
    if (birthDate != null) this.birthDate = birthDate;
    if (nickname != null) this.nickname = nickname;
    if (height != null) this.height = height;
    if (weight != null) this.weight = weight;
    if (memo != null) this.memo = memo;
  }
}

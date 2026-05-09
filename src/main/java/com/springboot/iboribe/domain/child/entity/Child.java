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
}

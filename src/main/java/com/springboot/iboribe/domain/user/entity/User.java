package com.springboot.iboribe.domain.user.entity;

import jakarta.persistence.*;

import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.family.entity.FamilyRole;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "family_id", nullable = false)
  private Family family;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private FamilyRole familyRole;
}

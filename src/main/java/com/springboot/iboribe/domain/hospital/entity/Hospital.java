package com.springboot.iboribe.domain.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "hospitals")
public class Hospital {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 200)
  private String name;

  @Column(length = 50)
  private String gu;

  @Column(length = 100)
  private String dong;

  @Column(length = 50)
  private String type;

  @Column(length = 50)
  private String tel;

  private Boolean nightCare;

  private Double monS;

  private Double monE;

  private Double tueS;

  private Double tueE;

  private Double wedS;

  private Double wedE;

  private Double thuS;

  private Double thuE;

  private Double friS;

  private Double friE;

  private Double satS;

  private Double satE;

  private Double sunS;

  private Double sunE;

  private Double holS;

  private Double holE;

  private Double lat;

  private Double lng;
}

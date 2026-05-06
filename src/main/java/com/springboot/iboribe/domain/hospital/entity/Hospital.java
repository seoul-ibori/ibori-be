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

  @Column(name = "mon_s")
  private Double monS;

  @Column(name = "mon_e")
  private Double monE;

  @Column(name = "tue_s")
  private Double tueS;

  @Column(name = "tue_e")
  private Double tueE;

  @Column(name = "wed_s")
  private Double wedS;

  @Column(name = "wed_e")
  private Double wedE;

  @Column(name = "thu_s")
  private Double thuS;

  @Column(name = "thu_e")
  private Double thuE;

  @Column(name = "fri_s")
  private Double friS;

  @Column(name = "fri_e")
  private Double friE;

  @Column(name = "sat_s")
  private Double satS;

  @Column(name = "sat_e")
  private Double satE;

  @Column(name = "sun_s")
  private Double sunS;

  @Column(name = "sun_e")
  private Double sunE;

  @Column(name = "hol_s")
  private Double holS;

  @Column(name = "hol_e")
  private Double holE;

  private Double lat;

  private Double lng;
}

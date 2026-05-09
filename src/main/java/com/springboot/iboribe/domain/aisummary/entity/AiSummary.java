package com.springboot.iboribe.domain.aisummary.entity;

import jakarta.persistence.*;

import com.springboot.iboribe.domain.medicalrecord.entity.MedicalRecord;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ai_summaries")
public class AiSummary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "medical_record_id", nullable = false, unique = true)
  private MedicalRecord medicalRecord;

  @Column(nullable = false, length = 200)
  private String oneLineSummary;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String medicalSummary;

  @Column(columnDefinition = "TEXT")
  private String caution;

  @Column(columnDefinition = "TEXT")
  private String nextSchedule;
}

package com.springboot.iboribe.domain.medicalrecord.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.springboot.iboribe.domain.aisummary.entity.AiSummary;
import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.codef.dto.response.CodefMedicationResponse;

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
@Table(name = "medical_records")
public class MedicalRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "child_id", nullable = false)
  private Child child;

  @Column(nullable = false, length = 100)
  private String hospitalName;

  @Column(nullable = false, length = 8)
  private String treatDate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<CodefMedicationResponse> medications;

  @OneToOne(mappedBy = "medicalRecord", fetch = FetchType.LAZY)
  private AiSummary aiSummary;

  @Column(length = 100)
  private String title;

  @Column(length = 10)
  private String treatTime;

  @Column(columnDefinition = "TEXT")
  private String memo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private MedicalRecordSource source;

  public void update(
      Child child, String title, String hospitalName, String treatTime, String memo) {
    if (child != null) this.child = child;
    if (title != null) this.title = title;
    if (hospitalName != null) this.hospitalName = hospitalName;
    if (treatTime != null) this.treatTime = treatTime;
    if (memo != null) this.memo = memo;
  }
}

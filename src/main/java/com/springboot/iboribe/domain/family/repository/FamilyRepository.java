package com.springboot.iboribe.domain.family.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.iboribe.domain.family.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Long> {

  boolean existsByFamilyCode(String familyCode);

  Optional<Family> findByFamilyCode(String familyCode);
}

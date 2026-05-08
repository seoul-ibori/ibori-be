package com.springboot.iboribe.domain.child.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.iboribe.domain.child.entity.Child;
import com.springboot.iboribe.domain.family.entity.Family;

public interface ChildRepository extends JpaRepository<Child, Long> {

  Optional<Child> findByFamilyAndName(Family family, String name);

  List<Child> findAllByFamily(Family family);
}

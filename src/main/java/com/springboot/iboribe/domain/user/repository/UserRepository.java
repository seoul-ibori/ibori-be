package com.springboot.iboribe.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.iboribe.domain.family.entity.Family;
import com.springboot.iboribe.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  List<User> findAllByFamily(Family family);
}

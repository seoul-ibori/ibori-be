package com.springboot.iboribe.domain.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.iboribe.domain.district.dto.response.DistrictResponse;
import com.springboot.iboribe.domain.hospital.entity.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

  @Query(
      value =
          """
          SELECT * FROM (
            SELECT *, (6371000 * acos(
              cos(radians(:lat)) * cos(radians(lat)) *
              cos(radians(lng) - radians(:lng)) +
              sin(radians(:lat)) * sin(radians(lat))
            )) AS distance
            FROM hospitals
            WHERE (:nightOnly = false OR night_care = true)
          ) sub
          WHERE distance < :radius
          ORDER BY distance
          """,
      nativeQuery = true)
  List<Hospital> findWithinRadius(
      @Param("lat") double lat,
      @Param("lng") double lng,
      @Param("radius") double radius,
      @Param("nightOnly") boolean nightOnly);

  @Query(
      "SELECT new com.springboot.iboribe.domain.district.dto.response.DistrictResponse(h.gu, h.dong) "
          + "FROM Hospital h WHERE h.gu LIKE %:keyword% OR h.dong LIKE %:keyword% "
          + "GROUP BY h.gu, h.dong ORDER BY h.gu, h.dong")
  List<DistrictResponse> searchDistricts(@Param("keyword") String keyword);
}

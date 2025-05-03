package com.cema_health_ke_v1.Backend.CEMA_health.repository;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.HealthProgramEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HealthProgramRepository extends JpaRepository<HealthProgramEntity, Long> {
    // Find by program code
    Optional<HealthProgramEntity> findByCode(String code);

    // Find active programs
    List<HealthProgramEntity> findByStatus(HealthProgramEntity.ProgramStatus status, Pageable pageable);

    // Search programs
    @Query("SELECT p FROM HealthProgramEntity p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.code) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<HealthProgramEntity> searchPrograms(@Param("query") String query, Pageable pageable);

    // Check if code exists
    boolean existsByCode(String code);
}

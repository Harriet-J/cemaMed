package com.cema_health_ke_v1.Backend.CEMA_health.repository;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    // Find by username (for authentication)
    Optional<DoctorEntity> findByUsername(String username);

    // Check if username exists (for registration)
    boolean existsByUsername(String username);

    // Search doctors by name or specialization
    @Query("SELECT d FROM DoctorEntity d WHERE " +
            "LOWER(d.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<DoctorEntity> searchDoctors(@Param("query") String query);

    Page<DoctorEntity> findByRole(DoctorEntity.Role role);
}

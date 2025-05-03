package com.cema_health_ke_v1.Backend.CEMA_health.repository;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {


    // Find by medical record number (exact match)
    Optional<PatientEntity> findByMedicalRecordNumber(String medicalRecordNumber);

    // Case-insensitive search with pagination
    @Query("SELECT p FROM PatientEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.medicalRecordNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<PatientEntity> searchPatients(@Param("query") String query, Pageable pageable);

    // Case-insensitive search without pagination (for backward compatibility)
    @Query("SELECT p FROM PatientEntity p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.medicalRecordNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PatientEntity> searchPatients(@Param("query") String query);

    // Check if medical record number exists (case-insensitive)
    boolean existsByMedicalRecordNumberIgnoreCase(String medicalRecordNumber);

    // Additional useful queries
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhone(String phone);

    // Find by email (case-insensitive)
    Optional<PatientEntity> findByEmailIgnoreCase(String email);

    boolean existsByMedicalRecordNumber(String recordNumber);
}
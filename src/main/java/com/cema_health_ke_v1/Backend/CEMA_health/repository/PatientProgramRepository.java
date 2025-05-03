package com.cema_health_ke_v1.Backend.CEMA_health.repository;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientProgramEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PatientProgramRepository extends JpaRepository<PatientProgramEntity, Long> {
    // Find enrollments by patient
    List<PatientProgramEntity> findByPatientId(Long patientId, Pageable pageable);

    // Find enrollments by program
    List<PatientProgramEntity> findByProgramId(Long programId);

    // Find active enrollments
    List<PatientProgramEntity> findByStatus(PatientProgramEntity.EnrollmentStatus status);

    // Find enrollments between dates
    List<PatientProgramEntity> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);

    // Custom query for patient-program relationship check
    @Query("SELECT CASE WHEN COUNT(pp) > 0 THEN true ELSE false END " +
            "FROM PatientProgramEntity pp " +
            "WHERE pp.patient.id = :patientId AND pp.program.id = :programId")
    boolean existsByPatientAndProgram(
            @Param("patientId") Long patientId,
            @Param("programId") Long programId);

    // Find by doctor who enrolled
    List<PatientProgramEntity> findByEnrolledById(Long doctorId);

    boolean existsByPatientIdAndProgramId(Long patientId, Long programId);
}

package com.cema_health_ke_v1.Backend.CEMA_health.service;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.EnrollPatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.PatientProgramDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientProgramService {
    // Enrollment Operations
    PatientProgramDto enrollPatient(Long patientId, Long programId, Long doctorId);

    // Read Operations
    List<PatientProgramDto> getPatientEnrollments(Long patientId);
    Page<PatientProgramDto> getPatientEnrollments(Long patientId, Pageable pageable);

    List<PatientProgramDto> getProgramEnrollments(Long programId);
    Page<PatientProgramDto> getProgramEnrollments(Long programId, Pageable pageable);

    // Update Operations
    PatientProgramDto updateEnrollmentStatus(Long enrollmentId, String status);
    PatientProgramDto completeEnrollment(Long enrollmentId);

    // Validation
    boolean isPatientEnrolled(Long patientId, Long programId);

    PatientProgramDto enrollPatient(@Valid EnrollPatientDto dto);
}
package com.cema_health_ke_v1.Backend.CEMA_health.service;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.PatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.RegisterPatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    // Create
    PatientDto registerPatient(@Valid RegisterPatientDto patient);

    // Read
    Optional<PatientDto> findByMedicalRecordNumber(String recordNumber);
    List<PatientDto> searchPatients(String query);
    Page<PatientDto> searchPatients(String query, Pageable pageable);

    // Update
    PatientDto updatePatient(Long id, PatientEntity patient);

    // Delete
    void deletePatient(Long id);

    // Validation
    boolean medicalRecordNumberExists(String recordNumber);

    Page<PatientDto> getAllPatients(Pageable pageable);

    PatientDto getPatientById(Long id);
}
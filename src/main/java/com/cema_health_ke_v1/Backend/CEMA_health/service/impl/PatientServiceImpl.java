package com.cema_health_ke_v1.Backend.CEMA_health.service.impl;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.PatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.RegisterPatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientEntity;
import com.cema_health_ke_v1.Backend.CEMA_health.exception.CustomException;
import com.cema_health_ke_v1.Backend.CEMA_health.repository.PatientRepository;
import com.cema_health_ke_v1.Backend.CEMA_health.service.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientDto registerPatient(@Valid RegisterPatientDto patient) {
        try {
            if (medicalRecordNumberExists(patient.getMedicalRecordNumber())) {
                log.error("Duplicate medical record number: {}", patient.getMedicalRecordNumber());
                throw new CustomException.ConflictException("Medical record number already exists");
            }

            PatientEntity savedPatient = patientRepository.save(patient);
            return convertToDto(savedPatient);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Patient registration failed: {}", e.getMessage(), e);
            throw new CustomException.BusinessException("Patient registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<PatientDto> findByMedicalRecordNumber(String recordNumber) {
        try {
            Optional<PatientEntity> patient = patientRepository.findByMedicalRecordNumber(recordNumber);
            return patient.map(this::convertToDto);
        } catch (Exception e) {
            log.error("Error finding patient by record number: {}", recordNumber, e);
            throw new CustomException.BusinessException("Failed to find patient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PatientDto> searchPatients(String query) {
        try {
            List<PatientEntity> patients = patientRepository.searchPatients(query);
            if (patients.isEmpty()) {
                log.info("No patients found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No patients found");
            }
            return patients.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching patients: {}", query, e);
            throw new CustomException.BusinessException("Failed to search patients", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<PatientDto> searchPatients(String query, Pageable pageable) {
        try {
            Page<PatientEntity> patients = patientRepository.searchPatients(query, pageable);
            if (patients.isEmpty()) {
                log.info("No patients found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No patients found");
            }
            return patients.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching patients: {}", query, e);
            throw new CustomException.BusinessException("Failed to search patients", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PatientDto updatePatient(Long id, PatientEntity patient) {
        try {
            PatientEntity existing = patientRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Patient not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Patient");
                    });

            // Update allowed fields
            existing.setFirstName(patient.getFirstName());
            existing.setLastName(patient.getLastName());
            existing.setDateOfBirth(patient.getDateOfBirth());
            existing.setGender(patient.getGender());
            existing.setAddress(patient.getAddress());
            existing.setPhone(patient.getPhone());
            existing.setEmail(patient.getEmail());
            existing.setBloodType(patient.getBloodType());
            existing.setAllergies(patient.getAllergies());

            PatientEntity updatedPatient = patientRepository.save(existing);
            return convertToDto(updatedPatient);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating patient with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to update patient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deletePatient(Long id) {
        try {
            if (!patientRepository.existsById(id)) {
                log.error("Patient not found with ID: {}", id);
                throw new CustomException.ResourceNotFoundException("Patient");
            }
            patientRepository.deleteById(id);
            log.info("Deleted patient with ID: {}", id);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting patient with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to delete patient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean medicalRecordNumberExists(String recordNumber) {
        try {
            return patientRepository.existsByMedicalRecordNumber(recordNumber);
        } catch (Exception e) {
            log.error("Error checking medical record number: {}", recordNumber, e);
            throw new CustomException.BusinessException("Failed to check medical record number", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<PatientDto> getAllPatients(Pageable pageable) {
        try {
            Page<PatientEntity> patients = patientRepository.findAll(pageable);

            if (patients.isEmpty()) {
                log.info("No patients found in database");
                throw new CustomException.ResourceNotFoundException("No patients found");
            }

            return patients.map(this::convertToDto);

        } catch (CustomException.BusinessException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            log.error("Error fetching paginated patients: {}", e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to retrieve patients",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public PatientDto getPatientById(Long id) {
        try {
            PatientEntity patient = patientRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Patient not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Patient");
                    });

            log.info("Retrieved patient with ID: {}", id);
            return convertToDto(patient);

        } catch (CustomException.BusinessException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            log.error("Error fetching patient with ID {}: {}", id, e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to retrieve patient details",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    private PatientDto convertToDto(PatientEntity patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setMedicalRecordNumber(patient.getMedicalRecordNumber());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        dto.setBloodType(patient.getBloodType());
        dto.setAllergies(patient.getAllergies());
        dto.setCreatedAt(patient.getCreatedAt());
        return dto;
    }

}
package com.cema_health_ke_v1.Backend.CEMA_health.service.impl;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.EnrollPatientDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.PatientProgramDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.*;
import com.cema_health_ke_v1.Backend.CEMA_health.exception.CustomException;
import com.cema_health_ke_v1.Backend.CEMA_health.repository.*;
import com.cema_health_ke_v1.Backend.CEMA_health.service.PatientProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PatientProgramServiceImpl implements PatientProgramService {
    private final PatientProgramRepository enrollmentRepository;
    private final PatientRepository patientRepository;
    private final HealthProgramRepository programRepository;
    private final DoctorRepository doctorRepository;

    public PatientProgramServiceImpl(PatientProgramRepository enrollmentRepository,
                                     PatientRepository patientRepository,
                                     HealthProgramRepository programRepository,
                                     DoctorRepository doctorRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.patientRepository = patientRepository;
        this.programRepository = programRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public PatientProgramDto enrollPatient(Long patientId, Long programId, Long doctorId) {
        try {
            if (isPatientEnrolled(patientId, programId)) {
                log.error("Duplicate enrollment - Patient: {}, Program: {}", patientId, programId);
                throw new CustomException.ConflictException("Patient already enrolled in this program");
            }

            PatientEntity patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> {
                        log.error("Patient not found: {}", patientId);
                        return new CustomException.ResourceNotFoundException("Patient");
                    });

            HealthProgramEntity program = programRepository.findById(programId)
                    .orElseThrow(() -> {
                        log.error("Program not found: {}", programId);
                        return new CustomException.ResourceNotFoundException("Program");
                    });

            DoctorEntity doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> {
                        log.error("Doctor not found: {}", doctorId);
                        return new CustomException.ResourceNotFoundException("Doctor");
                    });

            PatientProgramEntity enrollment = new PatientProgramEntity();
            enrollment.setPatient(patient);
            enrollment.setProgram(program);
            enrollment.setEnrolledBy(doctor);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setStatus(PatientProgramEntity.EnrollmentStatus.ACTIVE);
            enrollment.setCreatedAt(LocalDateTime.now());

            return convertToDto(enrollmentRepository.save(enrollment));

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Enrollment failed - Patient: {}, Program: {}, Error: {}",
                    patientId, programId, e.getMessage(), e);
            throw new CustomException.BusinessException("Enrollment failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public List<PatientProgramDto> getPatientEnrollments(Long patientId) {
        try {
            Pageable pageable = null;
            List<PatientProgramEntity> enrollments = enrollmentRepository.findByPatientId(patientId, pageable);
            if (enrollments.isEmpty()) {
                log.info("No enrollments found for patient: {}", patientId);
                throw new CustomException.ResourceNotFoundException("No enrollments found");
            }
            return enrollments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get enrollments for patient: {}", patientId, e);
            throw new CustomException.BusinessException("Failed to retrieve enrollments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<PatientProgramDto> getPatientEnrollments(Long patientId, Pageable pageable) {
        try {
            Page<PatientProgramEntity> page = (Page<PatientProgramEntity>) enrollmentRepository.findByPatientId(patientId, pageable);
            if (page.isEmpty()) {
                log.info("No enrollments found for patient: {}", patientId);
                throw new CustomException.ResourceNotFoundException("No enrollments found");
            }
            return page.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get paginated enrollments for patient: {}", patientId, e);
            throw new CustomException.BusinessException("Failed to retrieve enrollments", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<PatientProgramDto> getProgramEnrollments(Long programId) {
        return List.of();
    }

    @Override
    public Page<PatientProgramDto> getProgramEnrollments(Long programId, Pageable pageable) {
        return null;
    }

    @Override
    public PatientProgramDto updateEnrollmentStatus(Long enrollmentId, String status) {
        try {
            PatientProgramEntity enrollment = enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() -> {
                        log.error("Enrollment not found: {}", enrollmentId);
                        return new CustomException.ResourceNotFoundException("Enrollment");
                    });

            enrollment.setStatus(PatientProgramEntity.EnrollmentStatus.valueOf(status));
            return convertToDto(enrollmentRepository.save(enrollment));

        } catch (IllegalArgumentException e) {
            log.error("Invalid status value: {}", status);
            throw new CustomException.ValidationException("Invalid status value");
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update enrollment status: {}", enrollmentId, e);
            throw new CustomException.BusinessException("Failed to update enrollment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PatientProgramDto completeEnrollment(Long enrollmentId) {
        try {
            PatientProgramEntity enrollment = enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() -> {
                        log.error("Enrollment not found: {}", enrollmentId);
                        return new CustomException.ResourceNotFoundException("Enrollment");
                    });

            enrollment.setStatus(PatientProgramEntity.EnrollmentStatus.COMPLETED);
            enrollment.setCompletionDate(LocalDate.now());
            return convertToDto(enrollmentRepository.save(enrollment));

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to complete enrollment: {}", enrollmentId, e);
            throw new CustomException.BusinessException("Failed to complete enrollment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isPatientEnrolled(Long patientId, Long programId) {
        try {
            return enrollmentRepository.existsByPatientIdAndProgramId(patientId, programId);
        } catch (Exception e) {
            log.error("Enrollment check failed - Patient: {}, Program: {}", patientId, programId, e);
            throw new CustomException.BusinessException("Enrollment check failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PatientProgramDto enrollPatient(EnrollPatientDto dto) {
        try {
            // Validate patient exists
            PatientEntity patient = patientRepository.findById(dto.getPatientId())
                    .orElseThrow(() -> {
                        log.error("Patient not found with ID: {}", dto.getPatientId());
                        return new CustomException.ResourceNotFoundException("Patient");
                    });

            // Validate program exists
            HealthProgramEntity program = programRepository.findById(dto.getProgramId())
                    .orElseThrow(() -> {
                        log.error("Program not found with ID: {}", dto.getProgramId());
                        return new CustomException.ResourceNotFoundException("Program");
                    });

            // Validate doctor exists
            DoctorEntity doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> {
                        log.error("Doctor not found with ID: {}", dto.getDoctorId());
                        return new CustomException.ResourceNotFoundException("Doctor");
                    });

            // Check if already enrolled
            if (enrollmentRepository.existsByPatientIdAndProgramId(dto.getPatientId(), dto.getProgramId())) {
                log.error("Patient {} already enrolled in program {}", dto.getPatientId(), dto.getProgramId());
                throw new CustomException.ConflictException("Patient already enrolled in this program");
            }

            // Create new enrollment
            PatientProgramEntity enrollment = new PatientProgramEntity();
            enrollment.setPatient(patient);
            enrollment.setProgram(program);
            enrollment.setEnrolledBy(doctor);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setStatus(PatientProgramEntity.EnrollmentStatus.ACTIVE);
            enrollment.setClinicalNotes(dto.getClinicalNotes());
            enrollment.setCreatedAt(LocalDateTime.now());

            PatientProgramEntity savedEnrollment = enrollmentRepository.save(enrollment);
            log.info("Successfully enrolled patient {} in program {}", patient.getId(), program.getId());

            return convertToDto(savedEnrollment);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error enrolling patient: {}", e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to enroll patient",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}
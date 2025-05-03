package com.cema_health_ke_v1.Backend.CEMA_health.contoller;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.*;
import com.cema_health_ke_v1.Backend.CEMA_health.service.*;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.ResponseCode;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthAppController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final HealthProgramService programService;
    private final PatientProgramService enrollmentService;

    private <T> PagedResponse<T> buildSuccessResponse(Page<T> page) {
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                ResponseCode.StatusCode.SUCCESS.getDescription(),
                ResponseCode.MessageCode.SUCCESS.getCode(),
                ResponseCode.MessageCode.SUCCESS.getDescription(),
                page.getContent(),
                page.getNumber() + 1,  // Convert to 1-based index
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize()
        );
    }

    private <T> PagedResponse<T> buildErrorResponse(Page<T> page) {
        return new PagedResponse<>(
                ResponseCode.StatusCode.ERROR.getCode(),
                ResponseCode.StatusCode.ERROR.getDescription(),
                ResponseCode.MessageCode.NOT_FOUND.getCode(),
                ResponseCode.MessageCode.NOT_FOUND.getDescription(),
                page.getContent(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize()
        );
    }

    // ========== AUTH ENDPOINTS ==========

    // ========== DOCTOR ENDPOINTS ==========
    @GetMapping("/doctors")
    public PagedResponse<DoctorDto> getAllDoctors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DoctorDto> doctors = doctorService.getAllDoctors(PageRequest.of(page - 1, size));
        return doctors.isEmpty() ? buildErrorResponse(doctors) : buildSuccessResponse(doctors);
    }

    @GetMapping("/doctors/{id}")
    public PagedResponse<DoctorDto> getDoctorById(@PathVariable Long id) {
        DoctorDto doctor = doctorService.getDoctorById(id);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Doctor retrieved",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(doctor),
                1, 1, 1, 10
        );
    }

    // ========== PATIENT ENDPOINTS ==========
    @PostMapping("/patients")
    public PagedResponse<PatientDto> registerPatient(
            @Valid @RequestBody RegisterPatientDto dto) {
        PatientDto patient = patientService.registerPatient(dto);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Patient registered",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(patient),
                1, 1, 1, 10
        );
    }

    @GetMapping("/patients")
    public PagedResponse<PatientDto> getAllPatients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PatientDto> patients = patientService.getAllPatients(PageRequest.of(page - 1, size));
        return patients.isEmpty() ? buildErrorResponse(patients) : buildSuccessResponse(patients);
    }

    @GetMapping("/patients/{id}")
    public PagedResponse<PatientDto> getPatientById(@PathVariable Long id) {
        PatientDto patient = patientService.getPatientById(id);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Patient retrieved",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(patient),
                1, 1, 1, 10
        );
    }

    // ========== PROGRAM ENDPOINTS ==========
    @PostMapping("/programs")
    public PagedResponse<HealthProgramDto> createProgram(
            @Valid @RequestBody CreateProgramDto dto) {
        HealthProgramDto program = programService.createProgram(dto);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Program created",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(program),
                1, 1, 1, 10
        );
    }

    @GetMapping("/programs")
    public PagedResponse<HealthProgramDto> getAllPrograms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<HealthProgramDto> programs = programService.getAllPrograms(PageRequest.of(page - 1, size));
        return programs.isEmpty() ? buildErrorResponse(programs) : buildSuccessResponse(programs);
    }

    @GetMapping("/programs/{id}")
    public PagedResponse<HealthProgramDto> getProgramById(@PathVariable Long id) {
        HealthProgramDto program = programService.getProgramById(id);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Program retrieved",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(program),
                1, 1, 1, 10
        );
    }

    // ========== ENROLLMENT ENDPOINTS ==========
    @PostMapping("/enrollments")
    public PagedResponse<PatientProgramDto> enrollPatient(
            @Valid @RequestBody EnrollPatientDto dto) {
        PatientProgramDto enrollment = enrollmentService.enrollPatient(dto);
        return new PagedResponse<>(
                ResponseCode.StatusCode.SUCCESS.getCode(),
                "Patient enrolled",
                ResponseCode.MessageCode.SUCCESS.getCode(),
                "Operation successful",
                List.of(enrollment),
                1, 1, 1, 10
        );
    }

    @GetMapping("/enrollments/patient/{patientId}")
    public PagedResponse<PatientProgramDto> getPatientEnrollments(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PatientProgramDto> enrollments = enrollmentService.getPatientEnrollments(
                patientId, PageRequest.of(page - 1, size));
        return enrollments.isEmpty() ? buildErrorResponse(enrollments) : buildSuccessResponse(enrollments);
    }
}
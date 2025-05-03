package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientProgramEntity;
import jdk.jshell.Snippet;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PatientProgramDto {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long programId;
    private String programName;
    private Long doctorId;
    private String doctorName;
    private LocalDate enrollmentDate;
    private LocalDate completionDate;
    private PatientProgramEntity.EnrollmentStatus status;
    private String clinicalNotes;
    private LocalDateTime createdAt;


}

package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PatientDto {
    private Long id;
    private String medicalRecordNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String bloodType;
    private String allergies;
    private LocalDateTime createdAt;

}

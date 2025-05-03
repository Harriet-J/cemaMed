package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterPatientDto {

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
}

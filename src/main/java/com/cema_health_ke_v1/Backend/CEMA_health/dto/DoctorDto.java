package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.DoctorEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorDto {
    private Long id;
    private String username;
    private String fullName;
    private String specialization;
    private DoctorEntity.Role role;
    private DoctorEntity.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

}

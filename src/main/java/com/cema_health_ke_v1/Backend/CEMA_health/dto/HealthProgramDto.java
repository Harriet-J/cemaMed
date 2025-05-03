package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.HealthProgramEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HealthProgramDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private HealthProgramEntity.ProgramStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

}

package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponseDto {
    private LocalDateTime timestamp;
    private boolean success;
    private String message;

}

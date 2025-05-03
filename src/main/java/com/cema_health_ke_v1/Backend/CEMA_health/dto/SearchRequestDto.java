package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class SearchRequestDto {
    private String query;
    private Pageable pageable;
}

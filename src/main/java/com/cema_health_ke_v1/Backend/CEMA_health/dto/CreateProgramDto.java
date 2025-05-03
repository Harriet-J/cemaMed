package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
public class CreateProgramDto {
    @NotNull
    private String code;

    @NotNull
    private String name;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
}

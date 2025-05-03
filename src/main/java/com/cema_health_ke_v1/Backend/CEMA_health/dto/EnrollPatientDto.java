package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class EnrollPatientDto {
    @NotNull
    private Long patientId;

    @NotNull
    private Long programId;

    private String clinicalNotes;
}

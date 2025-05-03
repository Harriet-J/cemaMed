package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.PatientProgramEntity;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class UpdateEnrollmentDto {
    @NotNull
    private PatientProgramEntity.EnrollmentStatus status;

    private String clinicalNotes;
}

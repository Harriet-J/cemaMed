package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import com.cema_health_ke_v1.Backend.CEMA_health.entity.DoctorEntity;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RegisterDoctorDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String fullName;

    @NotNull
    private String specialization;

    @NotNull
    private DoctorEntity.Role role;
}

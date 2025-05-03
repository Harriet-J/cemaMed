package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class DoctorLoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;
}

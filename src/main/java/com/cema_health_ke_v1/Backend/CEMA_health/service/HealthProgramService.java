//package com.cema_health_ke_v1.Backend.CEMA_health.service;
//
//import com.cema_health_ke_v1.Backend.CEMA_health.dto.HealthProgramDto;
//import com.cema_health_ke_v1.Backend.CEMA_health.entity.HealthProgramEntity;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//public interface HealthProgramService {
//    HealthProgramEntity createProgram(HealthProgramEntity program);
//    List<HealthProgramEntity> getAllPrograms();
//
//    Page<HealthProgramDto> getAllPrograms(Pageable pageable);
//
//    List<HealthProgramEntity> getActivePrograms();
//
//    Page<HealthProgramDto> getActivePrograms(Pageable pageable);
//
//    List<HealthProgramEntity> searchPrograms(String query);
//
//    Page<HealthProgramDto> searchPrograms(String query, Pageable pageable);
//
//    HealthProgramEntity updateProgram(Long id, HealthProgramEntity program);
//    void deactivateProgram(Long id);
//    boolean programCodeExists(String code);
//}

package com.cema_health_ke_v1.Backend.CEMA_health.service;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.CreateProgramDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.HealthProgramDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.HealthProgramEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HealthProgramService {
    // Create
    HealthProgramDto createProgram(@Valid CreateProgramDto program);

    // Read
    List<HealthProgramDto> getAllPrograms();
    Page<HealthProgramDto> getAllPrograms(Pageable pageable);

    List<HealthProgramDto> getActivePrograms();
    Page<HealthProgramDto> getActivePrograms(Pageable pageable);

    List<HealthProgramDto> searchPrograms(String query);
    Page<HealthProgramDto> searchPrograms(String query, Pageable pageable);

    // Update
    HealthProgramDto updateProgram(Long id, HealthProgramEntity program);
    void deactivateProgram(Long id);

    // Utility
    boolean programCodeExists(String code);

    HealthProgramDto getProgramById(Long id);
}

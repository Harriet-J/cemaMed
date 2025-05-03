package com.cema_health_ke_v1.Backend.CEMA_health.service;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.DoctorDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDto registerDoctor(DoctorEntity doctor);
    Optional<DoctorDto> findByUsername(String username);
    List<DoctorEntity> searchDoctors(String query);
    List<DoctorEntity> findByRole(DoctorEntity.Role role);

    Page<DoctorDto> searchDoctors(String query, Pageable pageable);

    Page<DoctorDto> findByRole(DoctorEntity.Role role, Pageable pageable);

    DoctorEntity updateDoctor(Long id, DoctorEntity doctor);
    void deleteDoctor(Long id);
    boolean usernameExists(String username);

    DoctorDto getDoctorById(Long id);


    Page<DoctorDto> getAllDoctors(Pageable pageable);
}

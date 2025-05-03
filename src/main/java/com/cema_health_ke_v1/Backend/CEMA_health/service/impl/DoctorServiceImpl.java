package com.cema_health_ke_v1.Backend.CEMA_health.service.impl;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.DoctorDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.DoctorEntity;
import com.cema_health_ke_v1.Backend.CEMA_health.exception.CustomException;
import com.cema_health_ke_v1.Backend.CEMA_health.repository.DoctorRepository;
import com.cema_health_ke_v1.Backend.CEMA_health.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DoctorDto registerDoctor(DoctorEntity doctor) {
        try {
            if (usernameExists(doctor.getUsername())) {
                log.error("Username already taken: {}", doctor.getUsername());
                throw new CustomException.ConflictException("Username already taken");
            }
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctor.setCreatedAt(LocalDateTime.now());
            DoctorEntity savedDoctor = doctorRepository.save(doctor);
            return convertToDto(savedDoctor);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error registering doctor: {}", e.getMessage(), e);
            throw new CustomException.BusinessException("Failed to register doctor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<DoctorDto> findByUsername(String username) {
        try {
            Optional<DoctorEntity> doctor = doctorRepository.findByUsername(username);
            return doctor.map(this::convertToDto);
        } catch (Exception e) {
            log.error("Error fetching doctor by username: {}", username, e);
            throw new CustomException.BusinessException("Failed to fetch doctor by username", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<DoctorEntity> searchDoctors(String query) {
        try {
            List<DoctorEntity> doctors = (List<DoctorEntity>) doctorRepository.searchDoctors(query);
            if (doctors.isEmpty()) {
                log.info("No doctors found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No doctors found");
            }
            return doctors;
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching doctors: {}", query, e);
            throw new CustomException.BusinessException("Failed to search doctors", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<DoctorEntity> findByRole(DoctorEntity.Role role) {
        try {
            List<DoctorEntity> doctors = (List<DoctorEntity>) doctorRepository.findByRole(role);
            if (doctors.isEmpty()) {
                log.info("No doctors found for role: {}", role);
                throw new CustomException.ResourceNotFoundException("No doctors found for role: " + role);
            }
            return doctors;
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching doctors by role: {}", role, e);
            throw new CustomException.BusinessException("Failed to fetch doctors by role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<DoctorDto> searchDoctors(String query, Pageable pageable) {
        try {
            Page<DoctorEntity> doctors = doctorRepository.searchDoctors(query);
            if (doctors.isEmpty()) {
                log.info("No doctors found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No doctors found");
            }
            return doctors.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching doctors: {}", query, e);
            throw new CustomException.BusinessException("Failed to search doctors", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<DoctorDto> findByRole(DoctorEntity.Role role, Pageable pageable) {
        try {
            Page<DoctorEntity> doctors = doctorRepository.findByRole(role);
            if (doctors.isEmpty()) {
                log.info("No doctors found for role: {}", role);
                throw new CustomException.ResourceNotFoundException("No doctors found for role: " + role);
            }
            return doctors.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching doctors by role: {}", role, e);
            throw new CustomException.BusinessException("Failed to fetch doctors by role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DoctorEntity updateDoctor(Long id, DoctorEntity doctor) {
        try {
            DoctorEntity existing = doctorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Doctor not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Doctor");
                    });

            existing.setFullName(doctor.getFullName());
            existing.setSpecialization(doctor.getSpecialization());
            existing.setRole(doctor.getRole());
            existing.setStatus(doctor.getStatus());

            return doctorRepository.save(existing);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating doctor with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to update doctor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                log.error("Doctor not found with ID: {}", id);
                throw new CustomException.ResourceNotFoundException("Doctor");
            }
            doctorRepository.deleteById(id);
            log.info("Deleted doctor with ID: {}", id);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting doctor with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to delete doctor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        try {
            return doctorRepository.existsByUsername(username);
        } catch (Exception e) {
            log.error("Error checking username existence: {}", username, e);
            throw new CustomException.BusinessException("Failed to check username availability", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        try {
            DoctorEntity doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Doctor not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Doctor");
                    });

            log.info("Successfully retrieved doctor with ID: {}", id);
            return convertToDto(doctor);

        } catch (CustomException.BusinessException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            log.error("Error retrieving doctor with ID {}: {}", id, e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to retrieve doctor details",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public Page<DoctorDto> getAllDoctors(Pageable pageable) {
        try {
            Page<DoctorEntity> doctorsPage = doctorRepository.findAll(pageable);

            if (doctorsPage.isEmpty()) {
                log.info("No doctors found in database");
                throw new CustomException.ResourceNotFoundException("No doctors available");
            }

            return doctorsPage.map(this::convertToDto);

        } catch (CustomException.BusinessException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            log.error("Error fetching paginated doctors: {}", e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to retrieve doctors list",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private DoctorDto convertToDto(DoctorEntity doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setUsername(doctor.getUsername());
        dto.setFullName(doctor.getFullName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setRole(doctor.getRole());
        dto.setStatus(doctor.getStatus());
        dto.setCreatedAt(doctor.getCreatedAt());
        dto.setLastLogin(doctor.getLastLogin());
        return dto;
    }
}
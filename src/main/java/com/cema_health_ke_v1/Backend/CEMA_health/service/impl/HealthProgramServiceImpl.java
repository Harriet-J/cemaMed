package com.cema_health_ke_v1.Backend.CEMA_health.service.impl;

import com.cema_health_ke_v1.Backend.CEMA_health.dto.CreateProgramDto;
import com.cema_health_ke_v1.Backend.CEMA_health.dto.HealthProgramDto;
import com.cema_health_ke_v1.Backend.CEMA_health.entity.HealthProgramEntity;
import com.cema_health_ke_v1.Backend.CEMA_health.exception.CustomException;
import com.cema_health_ke_v1.Backend.CEMA_health.repository.HealthProgramRepository;
import com.cema_health_ke_v1.Backend.CEMA_health.service.HealthProgramService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class HealthProgramServiceImpl implements HealthProgramService {
    private final HealthProgramRepository programRepository;

    public HealthProgramServiceImpl(HealthProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public HealthProgramDto createProgram(@Valid CreateProgramDto programDto) {
        try {
            if (programCodeExists(programDto.getCode())) {
                log.error("Program code already exists: {}", programDto.getCode());
                throw new CustomException.ConflictException("Program code already exists");
            }

            // Convert DTO to Entity
            HealthProgramEntity program = new HealthProgramEntity();
            program.setCode(programDto.getCode());
            program.setName(programDto.getName());
            program.setDescription(programDto.getDescription());
//            program.setStartDate(programDto.getStartDate());
//            program.setEndDate(programDto.getEndDate());
            program.setStatus(HealthProgramEntity.ProgramStatus.ACTIVE);
            program.setCreatedAt(LocalDateTime.now());

            HealthProgramEntity savedProgram = programRepository.save(program);
            return convertToDto(savedProgram);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating program: {}", e.getMessage(), e);
            throw new CustomException.BusinessException(
                    "Failed to create program",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public List<HealthProgramDto> getAllPrograms() {
        try {
            List<HealthProgramEntity> programs = programRepository.findAll();
            if (programs.isEmpty()) {
                log.info("No programs found");
                throw new CustomException.ResourceNotFoundException("No programs found");
            }
            return programs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching all programs", e);
            throw new CustomException.BusinessException("Failed to fetch programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<HealthProgramDto> getAllPrograms(Pageable pageable) {
        try {
            Page<HealthProgramEntity> programs = programRepository.findAll(pageable);
            if (programs.isEmpty()) {
                log.info("No programs found");
                throw new CustomException.ResourceNotFoundException("No programs found");
            }
            return programs.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching paginated programs", e);
            throw new CustomException.BusinessException("Failed to fetch programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<HealthProgramDto> getActivePrograms() {
        try {
            Pageable pageable = null;
            List<HealthProgramEntity> programs = programRepository.findByStatus(HealthProgramEntity.ProgramStatus.ACTIVE, pageable);
            if (programs.isEmpty()) {
                log.info("No active programs found");
                throw new CustomException.ResourceNotFoundException("No active programs found");
            }
            return programs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching active programs", e);
            throw new CustomException.BusinessException("Failed to fetch active programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<HealthProgramDto> getActivePrograms(Pageable pageable) {
        try {
            Page<HealthProgramEntity> programs = (Page<HealthProgramEntity>) programRepository.findByStatus(HealthProgramEntity.ProgramStatus.ACTIVE, pageable);
            if (programs.isEmpty()) {
                log.info("No active programs found");
                throw new CustomException.ResourceNotFoundException("No active programs found");
            }
            return programs.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching active programs", e);
            throw new CustomException.BusinessException("Failed to fetch active programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<HealthProgramDto> searchPrograms(String query) {
        try {
            Pageable pageable = null;
            List<HealthProgramEntity> programs = programRepository.searchPrograms(query, pageable);
            if (programs.isEmpty()) {
                log.info("No programs found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No programs found");
            }
            return programs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching programs with query: {}", query, e);
            throw new CustomException.BusinessException("Failed to search programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<HealthProgramDto> searchPrograms(String query, Pageable pageable) {
        try {
            Page<HealthProgramEntity> programs = (Page<HealthProgramEntity>) programRepository.searchPrograms(query, pageable);
            if (programs.isEmpty()) {
                log.info("No programs found for query: {}", query);
                throw new CustomException.ResourceNotFoundException("No programs found");
            }
            return programs.map(this::convertToDto);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error searching programs with query: {}", query, e);
            throw new CustomException.BusinessException("Failed to search programs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HealthProgramDto updateProgram(Long id, HealthProgramEntity program) {
        try {
            HealthProgramEntity existing = programRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Program not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Program");
                    });

            // Update only allowed fields
            existing.setName(program.getName());
            existing.setDescription(program.getDescription());
            HealthProgramEntity updatedProgram = programRepository.save(existing);
            return convertToDto(updatedProgram);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating program with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to update program", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deactivateProgram(Long id) {
        try {
            HealthProgramEntity program = programRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Program not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Program");
                    });

            program.setStatus(HealthProgramEntity.ProgramStatus.INACTIVE);
            programRepository.save(program);
            log.info("Deactivated program with ID: {}", id);

        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deactivating program with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to deactivate program", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean programCodeExists(String code) {
            try {
                return programRepository.existsByCode(code);
            } catch (Exception e) {
                log.error("Error checking program code existence: {}", code, e);
                throw new CustomException.BusinessException("Failed to check program code availability", HttpStatus.INTERNAL_SERVER_ERROR);
            }


    }

    @Override
    public HealthProgramDto getProgramById(Long id) {
        try {
            HealthProgramEntity program = programRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Program not found with ID: {}", id);
                        return new CustomException.ResourceNotFoundException("Program");
                    });
            return convertToDto(program);
        } catch (CustomException.BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching program with ID: {}", id, e);
            throw new CustomException.BusinessException("Failed to retrieve program", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private HealthProgramDto convertToDto(HealthProgramEntity program) {
        HealthProgramDto dto = new HealthProgramDto();
        dto.setId(program.getId());
        dto.setCode(program.getCode());
        dto.setName(program.getName());
        dto.setDescription(program.getDescription());
        dto.setStatus(program.getStatus());
        dto.setCreatedAt(program.getCreatedAt());
        return dto;
    }
}
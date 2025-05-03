package com.cema_health_ke_v1.Backend.CEMA_health.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_programs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PatientProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private HealthProgramEntity program;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity enrolledBy;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    private LocalDate completionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED, TRANSFERRED
    }

    public void setProgram(HealthProgramEntity program) {
        this.program = program;
        program.getEnrollments().add(this);
    }
}
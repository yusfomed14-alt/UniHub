package com.unihub.entity;
import com.unihub.entity.enums.Cycle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = 'classroom')
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Classroom {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false )
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "ects_credits", nullable = false)
    private int ectsCredits;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle", nullable = false)
    private Cycle cycle;

    @Column(name = "semester")
    private String semester;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "max_absences", nullable = false)
    private int maxAbsences;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Person professor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

   @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}

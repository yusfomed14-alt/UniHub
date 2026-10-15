package com.unihub.entity;

import com.unihub.entity.enums.Cycle;
import com.unihub.entity.enums.StudentType;
import com.unihub.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Data
@NoArgsConstructor
@Table(name = "student")
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "person_id")
public class Student extends Person{


    @Enumerated(EnumType.STRING)
    @Column(name = "tuition_status", nullable = false)
    private StudentType studentType;

    @Column(name = "enrollment_year" , nullable = false)
    private Integer enrollmentYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "Cycle", nullable = false)
    private Cycle Cycle;

    @Column(name = "current_year" , nullable = false)
    private Integer currentYear;

    @Column(name = "total_ects_earned" , nullable = false ,columnDefinition = "integer default 0")
    private Integer totalEctsEarned;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status",nullable = false)
    private UserStatus userStatus;

}

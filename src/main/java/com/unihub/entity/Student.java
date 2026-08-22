package com.unihub.entity;

import com.unihub.entity.enums.Degree;
import com.unihub.entity.enums.Tuition_Status;
import com.unihub.entity.enums.User_Status;
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
    private Tuition_Status tuitionStatus;

    @Column(name = "enrollment_year" , nullable = false)
    private Integer enrollmentYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree", nullable = false)
    private Degree degree;

    @Column(name = "current_year" , nullable = false)
    private Integer currentYear;

    @Column(name = "total_ects_earned" , nullable = false ,columnDefinition = "integer default 0")
    private Integer totalEctsEarned;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status",nullable = false)
    private User_Status userStatus;

}

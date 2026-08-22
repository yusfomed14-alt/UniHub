package com.unihub.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import com.unihub.entity.enums.TeacherType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="teacher")
@PrimaryKeyJoinColumn(name="person_id")
@EqualsAndHashCode(callSuper=true)

public class Teacher extends Person{

    @Column(name="employee_number",nullable = false)
    private String employeeNumber;

    @Column(name = "teacher_title" , nullable = false)
    private String teacher_title;

    @Enumerated(EnumType.STRING)
    @Column(name = "teacher_type", nullable = false)
    private TeacherType teacherType;

    @Column(name="office_room")
    private String office_room;

    @Column(name = "hired_at")
    private LocalDate hired_at;
}

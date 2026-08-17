package com.unihub.repository;
import com.unihub.entity.enums.Teacher_Type;
import com.unihub.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    List<Teacher> findByTeacherType(Teacher_Type teacherType);
}

package com.unihub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.unihub.entity.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
    List<Student> findByEnrollmentYear(Integer enrollmentYear);
    List<Student> findByDepartment(String department);
}

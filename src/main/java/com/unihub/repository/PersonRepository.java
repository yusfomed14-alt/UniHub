package com.unihub.repository;

import com.unihub.entity.Person;
import com.unihub.entity.enums.Role;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PersonRepository extends JpaRepository<Person, String>{
    Optional<Person> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(String id);
    List<Person> findByRole(Role role);
}

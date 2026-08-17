package com.unihub.repository;

import com.unihub.entity.Otp;
import com.unihub.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Optional<Otp> findByPersonAndOtpCode(Person person, String otpCode);
    void deleteByPerson(Person person);
}

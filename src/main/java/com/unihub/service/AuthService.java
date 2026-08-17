package com.unihub.service;

import com.unihub.dto.*;
import com.unihub.entity.*;
import com.unihub.entity.enums.Role;
import com.unihub.exception.BusinessRuleException;
import com.unihub.exception.UnauthorizedException;
import com.unihub.exception.ResourceNotFoundException;
import com.unihub.repository.*;
import com.unihub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PersonRepository personRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final OtpRepository otpRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final JavaMailSender mailSender;

    @Value("${app.university.email-domain}")
    private String allowedEmailDomain;

    @Value("${app.otp.expiry-minutes}")
    private int otpExpiryMinutes;

//    Student activates own account
    @Transactional
    public MessageResponse register(RegisterRequest request) {

        // check id exists
        Person person=personRepository.findById(request.getId())
                .orElseThrow(()->new ResourceNotFoundException("account not found with id: "+request.getId()));

        // check email matches
        if(!person.getEmail().equals(request.getEmail())){
            throw new UnauthorizedException("email dose not match our records. ");

        }

        // check not already activated
        if (person.getPassword()!=null){
            throw new BusinessRuleException("Account is already activated please Login.");
        }

        // check passwords match
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new BusinessRuleException("Passwords do not match.");
        }

        // check university email domain
        if(!request.getEmail().endsWith("@"+allowedEmailDomain)){
            throw new BusinessRuleException("must use a university email address @"+allowedEmailDomain);
        }

        // save password and name
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setUpdatedBy(request.getEmail());
        personRepository.save(person);

        // send OTP
        sendOtp(person);
        return new MessageResponse("Account activated. Check your email for the OTP.");
    }

//    Login
    @Transactional
    public MessageResponse login(LoginRequest request) {

        // verify credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        }catch (Exception e){
            throw new UnauthorizedException("Wrong email or password.");
        }
        Person person=personRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("Person not Found with email : "+request.getEmail()));

        sendOtp(person);

        return new MessageResponse("OTP sent to your email.");
    }

}

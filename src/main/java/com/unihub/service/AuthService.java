package com.unihub.service;

import com.unihub.dto.request.*;
import com.unihub.dto.response.AuthResponse;
import com.unihub.dto.response.MessageResponse;
import com.unihub.entity.*;
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
        //add stuts for checking
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

    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        Person person=personRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("Person Not found."));

        Otp otp=otpRepository.findByPersonAndOtpCode(person, request.getOtp())
                .orElseThrow(()->new UnauthorizedException("Invalid OTP"));

        // check expiry
        if (otp.getOtpExpiry().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByPerson(person);
            throw new UnauthorizedException("OTP has expired, please request for a new one.");
        }
        otpRepository.deleteByPerson(person);


        // generate tokens
        String Token=jwtUtil.generateToken(person.getEmail());
        String refreshToken=jwtUtil.generateRefreshToken(person.getEmail());

        return AuthResponse.builder()
                .Token(Token)
                .refreshToken(refreshToken)
                .id(person.getId())
                .email(person.getEmail())
                .role(person.getRole())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .build();
    }

    // Refresh token ──────────────────────────────────
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken=request.getRefreshToken();

        if(!jwtUtil.isTokenValid(refreshToken)){
            throw new UnauthorizedException("Invalid or expired refresh token.");
        }

        String email=jwtUtil.extractEmail(refreshToken);
        Person person=personRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("Person not found with email : "+email));

        String newtoken=jwtUtil.generateToken(person.getEmail());
        String newrefreshToken=jwtUtil.generateRefreshToken(person.getEmail());

        return AuthResponse.builder()
                .Token(newtoken)
                .refreshToken(newrefreshToken)
                .id(person.getId())
                .email(person.getEmail())
                .role(person.getRole())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .build();
    }

    // ── 4. Forgot password ────────────────────────────────
    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        Person person = personRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new ResourceNotFoundException(
                        "No account found with this email : "+request.getEmail()));
        passwordResetRepository.deleteByPerson(person);

        String token=UUID.randomUUID().toString();

        PasswordReset passwordReset=PasswordReset.builder()
                .person(person)
                .token(token)
                .expiry(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5))
                .build();

        passwordResetRepository.save(passwordReset);
        sendResetEmail(person.getEmail(),token);

        return new MessageResponse("Password reset link sent to your email.");
    }
    // ── 5. Change password (logged user) ───────────────
    @Transactional
    public MessageResponse changePassword(String email,
                                         String currentPassword,
                                          String newPassword,
                                          String confirmPassword){

        Person person=personRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("Person Not Found with this email : "+email));

        // verify current password
        if (!passwordEncoder.matches(currentPassword,person.getPassword())) {
            throw new UnauthorizedException("Current Passwords is Incorrect."+
                    "If you forgot your password use forgot password.");
        }

        // check new passwords match
        if(!newPassword.equals(confirmPassword)){
            throw new BusinessRuleException("Passwords do not match.");
        }

        // check new password is not same as current
        if (passwordEncoder.matches(newPassword,person.getPassword())) {
            throw  new BusinessRuleException("new password is equal with the current password.");
        }

        person.setPassword(passwordEncoder.encode(newPassword));
        person.setUpdatedBy(person.getEmail());
        personRepository.save(person);

        return new MessageResponse("Password changed successfully.");


    }

    // ── Helper — send OTP email ───────────────────────────
    private void sendOtp(Person person){
        otpRepository.deleteByPerson(person);
        String otpCode=String.format("%06d",new Random().nextInt(999999));

        Otp otp=Otp.builder()
                .person(person)
                .otpCode(otpCode)
                .otpExpiry(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(otpExpiryMinutes))
                .build();
        otpRepository.save(otp);

        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(person.getEmail());
        message.setSubject("UniHub — Your OTP Code");
        message.setText("Your OTP code is: " + otpCode +
                "\n\nThis code expires in " + otpExpiryMinutes + " minutes." +
                "\n\nIf you did not request this, please ignore this email.");

        mailSender.send(message);
    }

    // ── Helper — send reset email ─────────────────────────
    private  void sendResetEmail(String email, String token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("UniHub - Password Reset");
        message.setText("Click the link below to reset your password: "+
                "\n\nhttps://unihub.com/reset-password?token="+token+
                "\n\nThis link expires in 5 minutes."+
                "\n\nIf you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}

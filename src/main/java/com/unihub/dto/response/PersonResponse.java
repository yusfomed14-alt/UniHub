package com.unihub.dto.response;

import com.unihub.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PersonResponse {
    private String id;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String profilePictureUrl;
    private String phoneNumber;
    private String department;
    private String faculty;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

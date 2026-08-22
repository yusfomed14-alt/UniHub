package com.unihub.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePersonRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String prfilePictureUrl;
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phoneNumber;
}

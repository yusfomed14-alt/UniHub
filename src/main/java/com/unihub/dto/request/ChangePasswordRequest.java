package com.unihub.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
  @NotBlank(message = "Current Password is required ")
  private String currentPassword;

    @NotBlank(message = "New Password is required")
    @Size(min = 8,message = "password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must contain uppercase, lowercase, number and symbol"
    )
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}

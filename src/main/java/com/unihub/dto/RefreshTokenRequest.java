package com.unihub.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class RefreshTokenRequest {
    @NotBlank(message = "Refresh Token is Required")
    private String refreshToken;
}

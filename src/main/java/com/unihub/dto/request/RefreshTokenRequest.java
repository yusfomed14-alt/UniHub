package com.unihub.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class RefreshTokenRequest {
    @NotBlank(message = "Refresh Token is Required")
    private String refreshToken;
}

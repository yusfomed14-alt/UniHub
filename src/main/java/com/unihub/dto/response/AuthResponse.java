package com.unihub.dto.response;
import com.unihub.entity.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AuthResponse {
    private String Token;
    private String refreshToken;
    private String id;
    private Role role;
    private String email;
    private String firstName;
    private String lastName;
}

package aladma.alamdaspring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String email;
    private String name;
    private String refreshToken;

    public LoginResponse(Long id, String email, String name, String refreshToken) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.refreshToken = refreshToken;
    }
}

package admin.message.auth;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long accessExpiresIn;
    private long refreshExpiresIn;
}

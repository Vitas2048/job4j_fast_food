package admin.jwtmodel;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class JwtRequest {

    private String login;

    private String password;
}

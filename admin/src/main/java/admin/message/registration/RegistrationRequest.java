package admin.message.registration;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RegistrationRequest {
    private String login;
    private String password;
}

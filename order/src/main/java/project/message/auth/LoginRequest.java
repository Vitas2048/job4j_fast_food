package project.message.auth;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LoginRequest {

    private String login;

    private String password;
}

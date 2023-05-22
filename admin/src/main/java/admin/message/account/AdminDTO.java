package admin.message.account;

import lombok.Getter;
import lombok.Setter;
import model.*;

@Getter@Setter
public class AdminDTO {
    private int id;
    private String login;

    public AdminDTO(Admin admin) {
        this.id = admin.getId();
        this.login = admin.getLogin();
    }
}

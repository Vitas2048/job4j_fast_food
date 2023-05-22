package model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter@Getter
@NoArgsConstructor
public class NotificationDTO {
    private int customerId;

    private List<Notification> notifications;
}

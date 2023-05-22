package model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "db_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String noticeText;

    private int customerId;

    private LocalDateTime createTime;

}

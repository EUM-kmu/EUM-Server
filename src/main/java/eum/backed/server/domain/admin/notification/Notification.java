package eum.backed.server.domain.admin.notification;

import eum.backed.server.domain.admin.admin.Admin;
import eum.backed.server.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column
    private String title;
    private String content;
    private String eventUrl;
    private Boolean isNotice;

    @ManyToOne
    @JoinColumn(name="community_admin_id")
    private Admin admin;

}

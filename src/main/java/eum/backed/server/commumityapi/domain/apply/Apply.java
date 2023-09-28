package eum.backed.server.commumityapi.domain.apply;

import eum.backed.server.commumityapi.domain.BaseTimeEntity;
import eum.backed.server.commumityapi.domain.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Apply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    @Column
    private Boolean isAccepted;
    private String content;

    @ManyToOne
    @JoinColumn(name="applicant_id")
    private Users user;
}

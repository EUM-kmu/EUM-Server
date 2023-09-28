package eum.backed.server.admin.domain.inquiryanswer;

import eum.backed.server.admin.domain.communityadmin.CommunityAdmin;
import eum.backed.server.admin.domain.inquiry.Inquiry;
import eum.backed.server.commumityapi.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InquiryAnswer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryAnswerId;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    @ManyToOne
    @JoinColumn(name = "community_admin_id")
    private CommunityAdmin communityAdmin;
}

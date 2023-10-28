package eum.backed.server.domain.admin.inquiryanswer;

import eum.backed.server.domain.admin.admin.Admin;
import eum.backed.server.domain.admin.inquiry.Inquiry;
import eum.backed.server.common.BaseTimeEntity;
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
    private Admin admin;
}

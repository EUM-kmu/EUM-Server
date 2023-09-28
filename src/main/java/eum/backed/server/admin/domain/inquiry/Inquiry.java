package eum.backed.server.admin.domain.inquiry;

import eum.backed.server.admin.domain.inquirycategory.InquiryCategory;
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
public class Inquiry extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column
    private String title;
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="inquiry_category_id")
    private InquiryCategory inquiryCategory;
}

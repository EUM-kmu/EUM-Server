package eum.backed.server.domain.admin.inquirycategory;

import eum.backed.server.domain.admin.inquiry.Inquiry;
import eum.backed.server.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InquiryCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryCategoryId;

    @Column
    private String content;

    @OneToMany(mappedBy = "inquiryCategory", orphanRemoval = true)
    private List<Inquiry> inquiries = new ArrayList<>();
}

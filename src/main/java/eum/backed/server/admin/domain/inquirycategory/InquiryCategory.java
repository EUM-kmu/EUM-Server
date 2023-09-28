package eum.backed.server.admin.domain.inquirycategory;

import eum.backed.server.admin.domain.inquiry.Inquiry;
import eum.backed.server.commumityapi.domain.BaseTimeEntity;
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

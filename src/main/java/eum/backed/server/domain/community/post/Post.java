package eum.backed.server.domain.community.post;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.category.Category;
import eum.backed.server.domain.community.region.GU.Gu;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column
    private String title;
    private String contents;
    private Date startDate;
    private Date endDate;
    private int pay;
    private String location;
    private int volunteerTime;
    private Boolean needHelper;
    private int maxNumOfPeople;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="gu_id")
    private Gu gu;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}

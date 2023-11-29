package eum.backed.server.domain.community.votepost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.voteresult.VoteResult;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VotePost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long votePostId;

    @Column
    private String title;
    private String content;
    private int agreeCount;
    private int disagreeCount;
    private int total;
//    private int likeCount;
    private Date endTime;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="region_id")
    private Regions regions;

    @OneToMany(mappedBy = "votePost", orphanRemoval = true)
    private List<VoteResult> voteResults = new ArrayList<>();

    @OneToMany(mappedBy = "votePost", orphanRemoval = true)
    private List<VoteComment> voteComments = new ArrayList<>();


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void addAgreeCount() {this.agreeCount += 1;}

    public void addDisagreeCount() {this.disagreeCount +=1 ;}

    public void addTotal() {this.total += 1;}

    public static VotePost toEntity(String title, String content, Date endTime, Users user){
        return VotePost.builder()
                .title(title)
                .content(content)
                .agreeCount(0)
                .disagreeCount(0)
                .total(0)
                .endTime(endTime)
                .user(user)
                .regions(user.getProfile().getRegions()).build();
    }
}

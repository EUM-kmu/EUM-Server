package eum.backed.server.domain.community.votepost;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.region.DONG.Dong;
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
    private Date endTime;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="dong_id")
    private Dong dong;

    @OneToMany(mappedBy = "votePost", orphanRemoval = true)
    private List<VoteResult> voteResults = new ArrayList<>();

    @OneToMany(mappedBy = "votePost", orphanRemoval = true)
    private List<VoteComment> voteComments = new ArrayList<>();

    public static VotePost toEntity(String title, String content, Date endTime, Users user){
        return VotePost.builder()
                .title(title)
                .content(content)
                .agreeCount(0)
                .disagreeCount(0)
                .total(0)
                .endTime(endTime)
                .user(user)
                .dong(user.getProfile().getDong()).build();
    }
}

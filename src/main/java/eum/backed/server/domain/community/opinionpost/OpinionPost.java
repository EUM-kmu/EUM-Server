package eum.backed.server.domain.community.opinionpost;

import com.fasterxml.jackson.databind.ser.Serializers;
import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Users;
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
public class OpinionPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opinionPostId;

    @Column
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name="dong_id")
    private Dong dong;

    @OneToMany(mappedBy = "opinionPost", orphanRemoval = true)
    private List<OpinionComment> opinionComments = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public static OpinionPost toEntity(String title, String content, Users user, Dong dong){
        return OpinionPost.builder()
                .dong(dong)
                .title(title)
                .content(content)
                .user(user).build();
    }
}

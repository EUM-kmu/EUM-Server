package eum.backed.server.domain.community.chat;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.user.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column
    private String chatRoomKeyFB;

    @ManyToOne
    @JoinColumn(name = "transaciton_post_id")
    private TransactionPost transactionPost;

    @ManyToOne
    @JoinColumn(name="apply_id")
    private Apply apply;

    @ManyToOne
    @JoinColumn(name="post_writer_id")
    private Users postWriter;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Users applicant;



    public static ChatRoom toEntity(String chatRoomKeyFB, TransactionPost transactionPost, Apply apply){
        return ChatRoom.builder()
                .chatRoomKeyFB(chatRoomKeyFB)
                .transactionPost(transactionPost)
                .apply(apply)
                .postWriter(transactionPost.getUser())
                .applicant(apply.getUser())
                .build();

    }


}

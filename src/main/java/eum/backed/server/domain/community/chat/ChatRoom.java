package eum.backed.server.domain.community.chat;

import eum.backed.server.common.BaseTimeEntity;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.marketpost.MarketPost;
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
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "market_post_id")
    private MarketPost marketPost;


    @ManyToOne
    @JoinColumn(name="post_writer_id")
    private Users postWriter;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Users applicant;



    public static ChatRoom toEntity(String chatRoomKeyFB, MarketPost marketPost, Apply apply){
        return ChatRoom.builder()
                .chatRoomKeyFB(chatRoomKeyFB)
                .marketPost(marketPost)
                .isDeleted(false)
                .postWriter(marketPost.getUser())
                .applicant(apply.getUser())
                .build();

    }


}

package eum.backed.server.domain.community.chat;

import lombok.*;

@Data
@Builder
public class Chat {
    private String transactionPostUserNickName;
    private String applicantNickName;
    private Long postId;
    private Long applyId;
    private Message message;

    public static Chat toEntity(String transactionPostUserNickName,String applicantNickName, Long postId, Long applyId,Message message){
        return Chat.builder()
                .transactionPostUserNickName(transactionPostUserNickName)
                .applicantNickName(applicantNickName)
                .postId(postId)
                .applyId(applyId)
                .message(message).build();
    }

}


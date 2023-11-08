package eum.backed.server.domain.community.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String senderNickName;
    private String message;
    private String timestamp;
    private String photoUrl;

    public static Message toEntity(String senderNickName,String message, String timestamp, String photoUrl){
        return Message.builder()
                .senderNickName(senderNickName)
                .message(message)
                .timestamp(timestamp)
                .photoUrl(photoUrl).build();
    }
}

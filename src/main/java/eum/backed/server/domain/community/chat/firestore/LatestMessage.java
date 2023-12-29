package eum.backed.server.domain.community.chat.firestore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Builder
@Getter
@Setter
public class LatestMessage {
    private Timestamp createdAt ;
    private String text;
    private Long userId;

}

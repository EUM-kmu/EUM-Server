package eum.backed.server.domain.community.chat.firestore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
public class Messages {
    private Timestamp timestamp;
    private String text;
    private Long userId;
}

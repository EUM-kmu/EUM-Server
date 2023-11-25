package eum.backed.server.domain.community.chat.firestore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Builder
@Getter
@Setter
public class Conversation {
    private Long applyId;
    private Long postId;
    private String postTitle;
    private boolean isGroup;
    private LatestMessage latestMessage;
    private List<Long> users;
    private Map<String, Integer> usersUnreadCountInfo;

}

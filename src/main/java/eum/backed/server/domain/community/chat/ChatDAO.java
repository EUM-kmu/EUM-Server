package eum.backed.server.domain.community.chat;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.chat.firestore.Conversation;
import eum.backed.server.domain.community.chat.firestore.LatestMessage;
import eum.backed.server.domain.community.chat.firestore.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
@Slf4j
@Repository
public class ChatDAO {
    public static final String COLLECTION_NAME = "chatrooms";

    public String createChat(Apply apply) throws ExecutionException, InterruptedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<Long> users = new ArrayList<>();
        Map<String, Integer> usersUnreadCountInfo = new HashMap<>();
        Messages messages = Messages.builder().userId(-1L).text("채팅방이 생성되었습니다").timestamp(timestamp).build();

        users.add(apply.getMarketPost().getUser().getUserId());
        users.add(apply.getUser().getUserId());

        usersUnreadCountInfo.put(apply.getMarketPost().getUser().getUserId().toString(),1);
        usersUnreadCountInfo.put(apply.getUser().getUserId().toString(),1);

        LatestMessage latestMessage = LatestMessage.builder().createdAt(messages.getTimestamp()).userId(messages.getUserId()).text(messages.getText()).build();
        Conversation conversation = Conversation.builder().isGroup(false).applyId(apply.getApplyId()).postId(apply.getMarketPost().getMarketPostId()).postTitle(apply.getMarketPost().getTitle()).latestMessage(latestMessage).users(users).usersUnreadCountInfo(usersUnreadCountInfo).build();
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<DocumentReference> apiFuture = db.collection(COLLECTION_NAME).add(conversation);
        DocumentReference chatroomsDocument = apiFuture.get();
        log.info(chatroomsDocument.getId());

        ApiFuture<DocumentReference> messageApiFutrue = chatroomsDocument.collection("messages").add(messages);
        log.info(messageApiFutrue.get().getId());

        return chatroomsDocument.getId();
    }
}

package eum.backed.server.domain.community.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.*;

@Data
@Builder
public class Chat {
    private String transactionPostUserNickName;
    private String applicantNickName;
    private Long postId;
    private Long applyId;

    public String saveToFirebase(Chat chat, Message message) {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chatRooms");
        DatabaseReference chatRoomRef = chatsRef.push(); // 채팅방에 대한 고유한 키 생성
        String chatRoomKey = chatRoomRef.getKey();
        chatRoomRef.child("transactionPostUserNickName").setValueAsync(chat.getTransactionPostUserNickName());
        chatRoomRef.child("applicantNickName").setValueAsync(chat.applicantNickName);
        chatRoomRef.child("postId").setValueAsync(chat.getPostId());
        chatRoomRef.child("applyId").setValueAsync(chat.getApplyId());

        // 이 채팅방에 대한 메시지 추가
        DatabaseReference messagesRef = chatRoomRef.child("messages");
        DatabaseReference newMessageRef = messagesRef.push(); // 메시지에 대한 고유한 키 생성
        newMessageRef.child("message").setValueAsync(message.getMessage());
        newMessageRef.child("photoUrl").setValueAsync(message.getPhotoUrl());
        newMessageRef.child("senderNickName").setValueAsync(message.getSenderNickName());
        newMessageRef.child("timestamp").setValueAsync(message.getTimestamp());
        return chatRoomKey;
    }
    public static Chat toEntity(String transactionPostUserNickName,String applicantNickName, Long postId, Long applyId){
        return Chat.builder()
                .transactionPostUserNickName(transactionPostUserNickName)
                .applicantNickName(applicantNickName)
                .postId(postId)
                .applyId(applyId)
                .build();
    }

}


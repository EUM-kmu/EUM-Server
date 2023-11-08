package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatRoomResponseDTO {

    private Long myId;
    private String myNickName;
    private Long opponentId;
    private String opponentNickName;
    private String chatRoomKeyFB;
    private Long applyId;
    private Long transactionPostId;

    public static ChatRoomResponseDTO newChatRoomResponse(Users mine, Users opponent, ChatRoom chatRoom){
        return ChatRoomResponseDTO.builder()
                .myId(mine.getUserId())
                .myNickName(mine.getProfile().getNickname())
                .opponentId(opponent.getUserId())
                .opponentNickName(opponent.getProfile().getNickname())
                .chatRoomKeyFB(chatRoom.getChatRoomKeyFB())
                .applyId(chatRoom.getApply().getApplyId())
                .transactionPostId(chatRoom.getTransactionPost().getTransactionPostId())
                .build();
    }
}

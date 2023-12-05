package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatRoomResponseDTO {

    private ProfileResponseDTO.UserInfo myInfo;
    private ProfileResponseDTO.UserInfo opponentInfo;
    private PostResponseDTO.PostResponse postInfo;
    private String chatRoomKeyFB;
    private Long chatRoomId;

//    송금 버튼 활성화 여부 ,
    private Boolean isRemittanceButton;


    public static ChatRoomResponseDTO newChatRoomResponse(BankTransactionDTO.TransactionUser transactionUser,Users mine, Users opponent, ChatRoom chatRoom){
        return ChatRoomResponseDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .myInfo(ProfileResponseDTO.toUserInfo(mine))
                .opponentInfo(ProfileResponseDTO.toUserInfo(opponent))
                .chatRoomKeyFB(chatRoom.getChatRoomKeyFB())
                .postInfo(PostResponseDTO.newPostResponse(chatRoom.getMarketPost()))
                .isRemittanceButton(mine == transactionUser.getSender())
                .build();
    }
}

package eum.backed.server.service.community;

import com.google.firebase.database.DatabaseReference;
import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.enums.ChatType;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.chat.Chat;
import eum.backed.server.domain.community.chat.ChatRoom;
import eum.backed.server.domain.community.chat.ChatRoomRepository;
import eum.backed.server.domain.community.chat.Message;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final DatabaseReference databaseReference;
    private final ChatRoomRepository chatRoomRepository;

    private final ApplyRepository applyRepository;
    private final MarketPostRepository marketPostRepository;
    private final UsersRepository usersRepository;

    public void createChatRoom(Long applyId){
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid Id"));
        if(apply.getIsAccepted() == false) throw new IllegalArgumentException("선정되지 않은 유저와는 채팅을 만들 수 없습니다");
        String transactionPostUserNickName  = apply.getMarketPost().getUser().getProfile().getNickname();

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = currentTime.format(formatter);

        Chat chat = Chat.toEntity(transactionPostUserNickName, apply.getUser().getProfile().getNickname(), apply.getMarketPost().getTransactionPostId(), apply.getApplyId());
        Message message = Message.toEntity(transactionPostUserNickName, "채팅방이 개설되었어요", timestamp, "");
        String chatRoomKey = chat.saveToFirebase(chat,message);

        ChatRoom chatRoom = ChatRoom.toEntity(chatRoomKey, apply.getMarketPost(), apply);
        chatRoomRepository.save(chatRoom);
    }

    private DataResponse<List<ChatRoomResponseDTO>> getChatListInMyPost(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriter(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser);
        return new DataResponse<>(chatRoomResponseDTOS).success(chatRoomResponseDTOS, "내 게시글 채팅 조회");
    }
    private DataResponse<List<ChatRoomResponseDTO>> getChatListInOtherPost(String email){
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByApplicant(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser);
        return new DataResponse<>(chatRoomResponseDTOS).success(chatRoomResponseDTOS, "내 신청 게시글 채팅 조회");
    }
    private List<ChatRoomResponseDTO> getChatRoomResponses(List<ChatRoom> chatRooms,Users mine){
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomResponseDTO chatRoomResponseDTO = ChatRoomResponseDTO.newChatRoomResponse(mine,chatRoom.getApplicant(),chatRoom);
            chatRoomResponseDTOS.add(chatRoomResponseDTO);
        }
        return chatRoomResponseDTOS;
    }

    public DataResponse<List<ChatRoomResponseDTO>> getChatListFilter(ChatType chatType, String email) {
        if(chatType == ChatType.mine){
            return getChatListInMyPost(email);
        }
        return getChatListInOtherPost(email);
    }
}

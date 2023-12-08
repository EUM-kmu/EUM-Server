package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.enums.ChatType;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.chat.*;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.community.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    private final ApplyRepository applyRepository;
    private final UsersRepository usersRepository;
    private final ChatDAO chatDAO;
    private final ProfileRepository profileRepository;
    public void createChatRoomWithFireStore(Long applyId) throws ExecutionException, InterruptedException {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid Id"));
        if(apply.getIsAccepted() == false) throw new IllegalArgumentException("선정되지 않은 유저와는 채팅을 만들 수 없습니다");
        String chatRoomKey = chatDAO.createChat(apply);

        ChatRoom chatRoom = ChatRoom.toEntity(chatRoomKey, apply.getMarketPost(), apply);
        chatRoomRepository.save(chatRoom);
    }

    public void createChatRoom(Long applyId){
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid Id"));
        if(apply.getIsAccepted() == false) throw new IllegalArgumentException("선정되지 않은 유저와는 채팅을 만들 수 없습니다");
        String transactionPostUserNickName  = apply.getMarketPost().getUser().getProfile().getNickname();

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = currentTime.format(formatter);

        Chat chat = Chat.toEntity(transactionPostUserNickName, apply.getUser().getProfile().getNickname(), apply.getMarketPost().getMarketPostId(), apply.getApplyId());
        Message message = Message.toEntity(transactionPostUserNickName, "채팅방이 개설되었어요", timestamp, "");
        String chatRoomKey = chat.saveToFirebase(chat,message);

        ChatRoom chatRoom = ChatRoom.toEntity(chatRoomKey, apply.getMarketPost(), apply);
        chatRoomRepository.save(chatRoom);
    }

    private APIResponse<List<ChatRoomResponseDTO>> getChatListInMyPost(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriter(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser,true);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }
    private APIResponse<List<ChatRoomResponseDTO>> getChatListInOtherPost(String email){
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByApplicant(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser,false);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }
    private APIResponse<List<ChatRoomResponseDTO>> getAllChatList(String email){
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriterOrApplicant(getUser,getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getAllChatRoomResponses(chatRooms, getUser);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }
    private List<ChatRoomResponseDTO> getChatRoomResponses(List<ChatRoom> chatRooms,Users mine,Boolean amIWriter){
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            BankTransactionDTO.TransactionUser transactionUser = checkSender(chatRoom);
            if(amIWriter){
                ChatRoomResponseDTO chatRoomResponseDTO = ChatRoomResponseDTO.newChatRoomResponse(transactionUser,mine,chatRoom.getApplicant(),chatRoom);
                chatRoomResponseDTOS.add(chatRoomResponseDTO);
            }else {
                ChatRoomResponseDTO chatRoomResponseDTO = ChatRoomResponseDTO.newChatRoomResponse(transactionUser,mine, chatRoom.getPostWriter(), chatRoom);
                chatRoomResponseDTOS.add(chatRoomResponseDTO);
            }
        }
        return chatRoomResponseDTOS;
    }
    private List<ChatRoomResponseDTO> getAllChatRoomResponses(List<ChatRoom> chatRooms,Users mine){
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            BankTransactionDTO.TransactionUser transactionUser = checkSender(chatRoom);
            Users opponentUser = (mine != chatRoom.getApplicant()) ? chatRoom.getApplicant() : chatRoom.getPostWriter();
            ChatRoomResponseDTO chatRoomResponseDTO = ChatRoomResponseDTO.newChatRoomResponse(transactionUser,mine,opponentUser,chatRoom);
            chatRoomResponseDTOS.add(chatRoomResponseDTO);

        }
        return chatRoomResponseDTOS;
    }

    public APIResponse<List<ChatRoomResponseDTO>> getChatListFilter(ChatType chatType, String email) {
        if(chatType == ChatType.mine){
            return getChatListInMyPost(email);
        } else if (chatType ==ChatType.others) {
            return getChatListInOtherPost(email);
        }
        return getAllChatList(email);
    }
    private BankTransactionDTO.TransactionUser checkSender(ChatRoom chatRoom){
//        true인경우 도움 요청, 작성자가 송금
        if(chatRoom.getMarketPost().getMarketType()== MarketType.REQUEST_HELP){
            Users sender = chatRoom.getPostWriter();
            Users receiver = chatRoom.getApplicant();
            return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
        }
        Users sender = chatRoom.getApplicant();
        Users receiver = chatRoom.getPostWriter();
        return BankTransactionDTO.TransactionUser.builder().sender(sender).receiver(receiver).build();
    }
    public void blockedChat(Users user){
        if(!profileRepository.existsByUser(user)) return;
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriterOrApplicant(user, user).orElse(Collections.emptyList());
        for(ChatRoom chatRoom : chatRooms){
            chatRoom.updateBlocked(true);
            chatRoomRepository.save(chatRoom);
        }
    }
}

package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.enums.ChatType;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.controller.community.DTO.response.ChatRoomResponseDTO;
import eum.backed.server.domain.community.apply.Apply;
import eum.backed.server.domain.community.apply.ApplyRepository;
import eum.backed.server.domain.community.chat.*;
import eum.backed.server.domain.community.profile.ProfileRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /**
     * 서버 DB에 채팅방 생성
     * @param applyId
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void createChatRoomWithFireStore(Long applyId) throws ExecutionException, InterruptedException {
        Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new NullPointerException("invalid Id"));
        String chatRoomKey = chatDAO.createChat(apply); //firestore에 채팅방 생성 및 초기 메시지 세팅

        ChatRoom chatRoom = ChatRoom.toEntity(chatRoomKey, apply.getMarketPost(), apply);
        chatRoomRepository.save(chatRoom);
    }


//내 게시글 채팅방 리스트
    private APIResponse<List<ChatRoomResponseDTO>> getChatListInMyPost(Long userId) {
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new NullPointerException("invalid userId"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriter(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser,true);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }
//    상대방 게시글 채팅방 리스트
    private APIResponse<List<ChatRoomResponseDTO>> getChatListInOtherPost(Long userId){
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new NullPointerException("invalid userId"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByApplicant(getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getChatRoomResponses(chatRooms, getUser,false);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }
//    전체 채팅 조회 -> 위에 세개 합쳐서 쿼리문으로 작성해야함
    private APIResponse<List<ChatRoomResponseDTO>> getAllChatList(Long userId){
        Users getUser = usersRepository.findById(userId).orElseThrow(() -> new NullPointerException("invalid userId"));
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriterOrApplicant(getUser,getUser).orElse(Collections.emptyList());
        List<ChatRoomResponseDTO> chatRoomResponseDTOS = getAllChatRoomResponses(chatRooms, getUser);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,chatRoomResponseDTOS);
    }

    /**
     * 내 게시글이냐 상대 게시글이냐에 따른 채팅 리스트 DTO 로 만들기
     * @param chatRooms
     * @param mine
     * @param amIWriter
     * @return
     */
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

    /**
     * 전체 채팅 dto response -> 위에 response 반환 함수랑 합쳐야할듯 왜이렇게 짰지
     * @param chatRooms
     * @param mine
     * @return
     */
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

    public APIResponse<List<ChatRoomResponseDTO>> getChatListFilter(ChatType chatType, Long userId) {
        if(chatType == ChatType.mine){
            return getChatListInMyPost(userId);
        } else if (chatType ==ChatType.others) {
            return getChatListInOtherPost(userId);
        }
        return getAllChatList(userId);
    }

    /**
     * 채팅 송금의 경우 돈을 보내는 사람이 정해져있기때문에 이에 대핸 예외처리
     * @param chatRoom
     * @return
     */
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

    /**
     * 탈퇴할때의 채팅 block
     * @param user
     */
    public void blockedChatInWithdrawal(Users user){
        if(!profileRepository.existsByUser(user)) return;
        List<ChatRoom> chatRooms = chatRoomRepository.findByPostWriterOrApplicant(user, user).orElse(Collections.emptyList());
        for(ChatRoom chatRoom : chatRooms){
            chatRoom.updateBlocked(true);
            chatRoomRepository.save(chatRoom);
        }
    }

    /**
     * 차단할때의 채팅 block
     * @param isBlocked
     * @param blocker
     * @param blocked
     */
    public void blockedAction(Boolean isBlocked,Users blocker, Users blocked) {
        List<ChatRoom> allChatRooms = new ArrayList<>();
        List<ChatRoom> chatRooms1 = chatRoomRepository.findByPostWriterAndApplicant(blocker, blocked).orElse(Collections.emptyList());
        List<ChatRoom> chatRooms2 = chatRoomRepository.findByPostWriterAndApplicant(blocked, blocker).orElse(Collections.emptyList());
        allChatRooms.addAll(chatRooms1);
        allChatRooms.addAll(chatRooms2);
        for(ChatRoom chatRoom : allChatRooms){
            chatRoom.updateBlocked(isBlocked);
            chatRoomRepository.save(chatRoom);
        }

    }
}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.community.ChatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final BankAccountService bankAccountService;
//
//    @GetMapping
//    public DataResponse createChat(@RequestParam Long applyId){
//        return chatService.createChatRoom(applyId);
//    }

    @GetMapping("/getChatListInMypost")
    public DataResponse<List<ChatRoomResponseDTO>> getChatListInMyPost(@AuthenticationPrincipal String email){
        return chatService.getChatListInMyPost(email);
    }
    @GetMapping("/getChatListInOtherPost")
    public DataResponse<List<ChatRoomResponseDTO>> getCheckListInOtherPost(@AuthenticationPrincipal String email){
        return chatService.getChatListInOtherPost(email);
    }
    @PostMapping("/remittance")
    @ApiOperation(value = "채팅으로 송금하기")
    public DataResponse remittance(@RequestParam Long chatRoomId, @AuthenticationPrincipal String email){
        return bankAccountService.remittanceByChat(chatRoomId, email);
    }
}
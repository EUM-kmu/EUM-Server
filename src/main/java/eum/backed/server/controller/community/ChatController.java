package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.ChatType;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.domain.community.chat.ChatDAO;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import eum.backed.server.service.community.ChatService;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.ProfileService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final BankAccountService bankAccountService;
    private final MarketPostService marketPostService;
    private final ProfileService profileService;
    private final ChatDAO chatDAO;

    @GetMapping("/{chatType}")
    public ResponseEntity<APIResponse<List<ChatRoomResponseDTO>>> getChatListFilter(@PathVariable ChatType chatType, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(chatService.getChatListFilter(chatType,email));
    }
//    @GetMapping
//    public void createchat() throws ExecutionException, InterruptedException {
//        chatDAO.createChat();
//
//    }

    @PostMapping("/{chatRoomId}/remittance")
    @ApiOperation(value = "채팅으로 송금하기")
    public ResponseEntity<APIResponse> remittance(@PathVariable Long chatRoomId, @RequestBody BankAccountRequestDTO.Password password, @AuthenticationPrincipal String email){
        BankTransactionDTO.UpdateTotalSunrise updateTotalSunrise =bankAccountService.remittanceByChat(password.getPassword(),chatRoomId, email);
        marketPostService.updateStatusCompleted(chatRoomId);
        profileService.updateTotalSunrise(updateTotalSunrise.getMe().getProfile(), updateTotalSunrise.getAmount() );
        profileService.updateTotalSunrise(updateTotalSunrise.getReceiver().getProfile(), updateTotalSunrise.getAmount());
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS));
    }
}

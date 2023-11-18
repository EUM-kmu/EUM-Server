package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.ChatType;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import eum.backed.server.service.community.ChatService;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.ProfileService;
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
    private final MarketPostService marketPostService;
    private final ProfileService profileService;

    @GetMapping("/{chatType}")
    public DataResponse<List<ChatRoomResponseDTO>> getChatListFilter(@PathVariable ChatType chatType, @AuthenticationPrincipal String email){
        return chatService.getChatListFilter(chatType,email);
    }

    @PostMapping("/{chatRoomId}/remittance")
    @ApiOperation(value = "채팅으로 송금하기")
    public DataResponse remittance(@PathVariable Long chatRoomId, @RequestBody BankAccountRequestDTO.Password password, @AuthenticationPrincipal String email){
        BankTransactionDTO.UpdateTotalSunrise updateTotalSunrise =bankAccountService.remittanceByChat(password.getPassword(),chatRoomId, email);
        marketPostService.updateStatusCompleted(chatRoomId);
        profileService.updateTotalSunrise(updateTotalSunrise.getMe().getProfile(), updateTotalSunrise.getAmount() );
        profileService.updateTotalSunrise(updateTotalSunrise.getReceiver().getProfile(), updateTotalSunrise.getAmount());
        return new DataResponse<>().success("채팅 송금 성공");
    }
}

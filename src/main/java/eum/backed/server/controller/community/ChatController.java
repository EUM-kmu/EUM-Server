package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.ChatType;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import eum.backed.server.service.community.ChatService;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.ProfileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<ChatRoomResponseDTO>>> getChatListFilter(@RequestParam(name = "type",required = false) ChatType chatType, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(chatService.getChatListFilter(chatType,email));
    }

    @PostMapping("/{chatRoomId}/remittance")
    @ApiOperation(value = "채팅으로 송금하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> remittance(@PathVariable Long chatRoomId, @RequestBody BankAccountRequestDTO.Password password, @AuthenticationPrincipal String email){
        BankTransactionDTO.UpdateTotalSunrise updateTotalSunrise =bankAccountService.remittanceByChat(password.getPassword(),chatRoomId, email);
        marketPostService.updateStatusCompleted(chatRoomId);
        profileService.updateTotalSunrise(updateTotalSunrise.getMe().getProfile(), updateTotalSunrise.getAmount() );
        profileService.updateTotalSunrise(updateTotalSunrise.getReceiver().getProfile(), updateTotalSunrise.getAmount());
        return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS), HttpStatus.CREATED);
    }
}

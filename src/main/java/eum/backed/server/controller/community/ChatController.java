package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.bank.DTO.request.BankAccountRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.ChatType;
import eum.backed.server.controller.community.DTO.response.ChatRoomResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.domain.auth.dto.CustomUserInfoDto;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.DTO.BankTransactionDTO;
import eum.backed.server.service.community.ApplyService;
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
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {
    private final ChatService chatService;
    private final BankAccountService bankAccountService;
    private final MarketPostService marketPostService;
    private final ProfileService profileService;
    private final ApplyService applyService;

    /**
     *
     * @param chatType : 내 게시글, 상대 게시글 조회
     * @param customUserDetails : jwt에 담긴 email
     * @return : 내정보, 상대 정보, 게시글 정보, 파이어베이스에 저장된 키값
     */
    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<ChatRoomResponseDTO>>> getChatListFilter(@RequestParam(name = "type",required = false) ChatType chatType, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(chatService.getChatListFilter(chatType,Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     *
     * @param chatRoomId : 채팅방 id
     * @param password : 송금 확인 비밀번호
     * @param customUserDetails : jwt에 담긴 email
     * @return
     */

    @PostMapping("/{chatRoomId}/remittance")
    @ApiOperation(value = "채팅으로 송금하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> remittance(@PathVariable Long chatRoomId, @RequestBody BankAccountRequestDTO.Password password, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        bankAccountService.remittanceByChat(password.getPassword(),chatRoomId, Long.valueOf(customUserDetails.getUsername())); //송금 처리
        marketPostService.updateStatusCompleted(chatRoomId); // 거래 상태 업데이트
        return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS), HttpStatus.CREATED);
    }
    /**
     * 채팅방안에서 활동파기
     * @param postId : 게시글 Id
     * @param chatId : 파기할 채팅 id
     * @param customUserDetails : jwt에 담긴 email
     * @return
     */
    @DeleteMapping("{postId}/cancel/{chatId}")
    @ApiOperation(value = "활동파기", notes = "활동 파기") @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> cancelTrading(@PathVariable Long postId, @PathVariable Long chatId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(applyService.cancel(postId,chatId,Long.valueOf(customUserDetails.getUsername())));
    }

}

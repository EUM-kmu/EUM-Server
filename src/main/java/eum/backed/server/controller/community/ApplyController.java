package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.service.community.ApplyService;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/v1/market/post")
@RequiredArgsConstructor
@Api(tags = "market")
@CrossOrigin("*")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping("/{postId}/apply")
    @ApiOperation(value = "지원하기", notes = "도움 게시글에 지원")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> apply(@PathVariable Long postId,@RequestBody ApplyRequestDTO.Apply apply, @AuthenticationPrincipal String email){
        return new ResponseEntity<>(applyService.doApply(postId,apply, email), HttpStatus.CREATED);
    }
    @DeleteMapping("/{postId}/apply/{applyId}")
    @ApiOperation(value = "지원취소", notes = "지원 쉬소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> unapply(@PathVariable Long postId,@PathVariable Long applyId, @AuthenticationPrincipal String email){
        return new ResponseEntity<>(applyService.unApply(postId,applyId, email), HttpStatus.CREATED);
    }
    @GetMapping("/{postId}/apply")
    @ApiOperation(value ="지원리스트 조회", notes = "게시글 당 지원리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<ApplyResponseDTO.ApplyListResponse>>> getApplyList( @PathVariable Long postId){
        return ResponseEntity.ok(applyService.getApplyList(postId));
    }
    @PostMapping("/{postId}/apply/{applyIds}")
    @ApiOperation(value = "선정하기", notes = "해당 신청자 선정") @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> accept(@PathVariable List<Long> applyIds,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(applyService.accept(applyIds,email));
    }
    @PostMapping("/{postId}/accept")
    @ApiOperation(value = "선정하기", notes = "해당 신청자 선정") @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> acceptByPost(@RequestBody ApplyRequestDTO.AcceptList acceptList,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(applyService.accept(acceptList.getApplyIds(),email));
    }

    @DeleteMapping("{postId}/cancel/{chatId}")
    @ApiOperation(value = "활동파기", notes = "활동 파기") @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> cancelTrading(@PathVariable Long postId, @PathVariable Long chatId, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(applyService.cancel(postId,chatId,email));
    }

}

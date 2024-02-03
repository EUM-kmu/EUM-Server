package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.request.ApplyRequestDTO;
import eum.backed.server.controller.community.DTO.response.ApplyResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.service.community.ApplyService;
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
@CrossOrigin("*")
public class ApplyController {
    private final ApplyService applyService;

    /**
     * 지원하기
     * @param postId : 지원할 게시글 id
     * @param apply : 지원 폼
     * @return
     */
    @PostMapping("/{postId}/apply")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> apply(@PathVariable Long postId,@RequestBody ApplyRequestDTO.Apply apply, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return new ResponseEntity<>(applyService.doApply(postId,apply, Long.valueOf(customUserDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 지원취소
     * @param postId : 게시글 id
     * @param applyId : 지원취소할 id
     * @param customUserDetails : jwt에 담긴 email
     * @return
     */
    @DeleteMapping("/{postId}/apply/{applyId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse> unapply(@PathVariable Long postId,@PathVariable Long applyId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return new ResponseEntity<>(applyService.unApply(postId,applyId, Long.valueOf(customUserDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 내 지원자 리스트 조회
     * @param postId : 지원자리스트를 보고싶은 게시글 id
     * @return : 지원자 정보, 지원정보(지원 id, 게시글 id, 한줄 소개, 수락 여부)
     */
    @GetMapping("/{postId}/apply")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<ApplyResponseDTO.ApplyListResponse>>> getApplyList( @PathVariable Long postId){
        return ResponseEntity.ok(applyService.getApplyList(postId));
    }

    /**
     *
     * @param acceptList : 수락할 지원 리스크
     * @return
     */
    @PostMapping("/{postId}/accept")
    public ResponseEntity<APIResponse> acceptByPost(@RequestBody ApplyRequestDTO.AcceptList acceptList,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(applyService.accept(acceptList.getApplyIds(),Long.valueOf(customUserDetails.getUsername())));
    }


}

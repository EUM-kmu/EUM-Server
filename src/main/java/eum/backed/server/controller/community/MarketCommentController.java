package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.CommentRequestDTO;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.service.community.CommentServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market/post")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MarketCommentController {
    private final CommentServiceImpl commentServiceImpl;

    /**
     * 게시글 id에 해당하는 댓글들 조회
     * @param postId : 게시글 id
     * @param pageable : 페이지 네이션 (디폴트 값 설정해야함)
     * @param customUserDetails : jwt에 담긴 email
     * @return : 댓글 내용, 작성자 정보, 게시글 작성자/댓글 작성자 판별
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/{postId}/comment")
    ResponseEntity<APIResponse<List<CommentResponseDTO.CommentResponse>>> getComments(@PathVariable Long postId,  Pageable pageable, @AuthenticationPrincipal CustomUserDetails customUserDetails ){
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS,commentServiceImpl.getComments(postId, Long.valueOf(customUserDetails.getUsername()),pageable)));
    }

    /**
     * 댓글 작성
     * @param postId : 게시글
     * @param commentCreate : 댓글 내용
     * @param customUserDetails : jwt에 담긴 email
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/{postId}/comment")
    ResponseEntity<APIResponse> create(@PathVariable Long postId, @RequestBody @Validated CommentRequestDTO.CommentCreate commentCreate, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return new ResponseEntity<>(commentServiceImpl.createComment(postId, commentCreate, Long.valueOf(customUserDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     * @param commentId : 수정할 댓글 id
     * @param commentUpdate : 수정할 댓글 내용
     * @param customUserDetails : jwt에 담긴 customUserDetails
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PutMapping("{postId}/comment/{commentId}")
    ResponseEntity<APIResponse> update(@PathVariable Long commentId, @RequestBody @Validated CommentRequestDTO.CommentUpdate commentUpdate, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(commentServiceImpl.updateComment(commentId, commentUpdate, Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 댓글 삭제
     * @param commentId : 삭제할 댓글 id
     * @param customUserDetails : jwt에 담긴 email
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @DeleteMapping("/{postId}/comment/{commentId}")
    ResponseEntity<APIResponse> delete(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(commentServiceImpl.deleteComment(commentId, Long.valueOf(customUserDetails.getUsername())));
    }


}

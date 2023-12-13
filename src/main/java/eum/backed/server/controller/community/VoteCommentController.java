package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.service.community.CommentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vote/post")
@RequiredArgsConstructor
@Api(tags = "vote")
@CrossOrigin("*")
public class VoteCommentController {
    private final CommentServiceImpl commentService;
    @ApiOperation(value = "투표 댓글 작성", notes = "댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/{postId}/comment")
    ResponseEntity<APIResponse> create(@PathVariable Long postId, @RequestBody @Validated CommentRequestDTO.CommentCreate commentCreate, @AuthenticationPrincipal String email){
        return new ResponseEntity<>(commentService.createComment(postId, commentCreate, email,CommentType.VOTE), HttpStatus.CREATED);
    }
    @ApiOperation(value = "투표 수정", notes = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PutMapping("/{postId}/comment/{commentId}")
    ResponseEntity<APIResponse> update(@PathVariable Long commentId, @RequestBody CommentRequestDTO.CommentUpdate commentUpdate, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdate, email,CommentType.VOTE));
    }
    @ApiOperation(value = "투표 삭제", notes = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @DeleteMapping("/{postId}/comment/{commentId}")
    ResponseEntity<APIResponse> delete(@PathVariable Long commentId, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(commentService.deleteComment(commentId, email,CommentType.VOTE));
    }
}

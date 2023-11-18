package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.service.community.CommentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/market")
@RequiredArgsConstructor
@Api(tags = "market comment")
public class MarketCommentController {
    private final CommentServiceImpl commentServiceImpl;
    @ApiOperation(value = "거래 댓글 작성", notes = "댓글 작성")
    @PostMapping("/{postId}/comment")
    DataResponse create(@PathVariable Long postId,@RequestBody @Validated CommentRequestDTO.Create create, @AuthenticationPrincipal String email){
        return commentServiceImpl.createComment(postId,create, email, CommentType.TRANSACTION);
    }
    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    @PutMapping("{postId}/comment/{commentId}")
    DataResponse update(@PathVariable Long commentId,@RequestBody CommentRequestDTO.Update update,@AuthenticationPrincipal String email){
        return commentServiceImpl.updateComment(commentId,update, email,CommentType.TRANSACTION);
    }
    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제")
    @DeleteMapping("/{postId}/comment/{commentId}")
    DataResponse delete(@PathVariable Long commentId, @AuthenticationPrincipal String email){
        return commentServiceImpl.deleteComment(commentId, email,CommentType.TRANSACTION);
    }


}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.service.community.CommentServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post/opinion")
@RestController
@RequiredArgsConstructor
@Api(tags = "opinion comment")
public class OpinionCommentController {
    private final CommentServiceImpl commentService;
    @PostMapping("/{postId}/comment")
    public DataResponse create(@PathVariable Long postId,@RequestBody CommentRequestDTO.Create create, @AuthenticationPrincipal String email){
        return commentService.createComment(postId,create, email, CommentType.OPINION);
    }

    @PutMapping("/{postId}/comment/{commentId}")
    public DataResponse update(@PathVariable Long commentId,@RequestBody CommentRequestDTO.Update update,@AuthenticationPrincipal String email){
        return commentService.updateComment(commentId,update, email,CommentType.OPINION);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public DataResponse delete(@PathVariable Long commentId,@AuthenticationPrincipal String email){
        return commentService.deleteComment(commentId, email,CommentType.OPINION);
    }

}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.service.community.CommentServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/post/opinion")
@RestController
@RequiredArgsConstructor
@Api(tags = "opinion")
@CrossOrigin("*")
public class OpinionCommentController {
    private final CommentServiceImpl commentService;
    @PostMapping("/{postId}/comment")
    public ResponseEntity<APIResponse> create(@PathVariable Long postId, @RequestBody @Validated CommentRequestDTO.CommentCreate commentCreate, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(commentService.createComment(postId, commentCreate, email, CommentType.OPINION));
    }

    @PutMapping("/{postId}/comment/{commentId}")
    public  ResponseEntity<APIResponse> update(@PathVariable Long commentId, @RequestBody @Validated CommentRequestDTO.CommentUpdate commentUpdate, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(commentService.updateComment(commentId, commentUpdate, email,CommentType.OPINION));
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long commentId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(commentService.deleteComment(commentId, email,CommentType.OPINION));
    }

}

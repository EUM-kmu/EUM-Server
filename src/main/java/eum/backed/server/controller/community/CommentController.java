package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Api(tags = "거래 게시글 댓글")
public class CommentController {
    private final CommentService commentService;
    @ApiOperation(value = "거래 댓글 작성", notes = "댓글 작성")
    @PostMapping
    DataResponse create(@RequestBody @Validated CommentRequestDTO.Create create, @AuthenticationPrincipal String email){
        return commentService.create(create, email);
    }
    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    @PutMapping
    DataResponse update(@RequestBody CommentRequestDTO.Update update,@AuthenticationPrincipal String email){
        return commentService.update(update, email);
    }
    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제")
    @DeleteMapping
    DataResponse delete(@RequestParam Long commentId, @AuthenticationPrincipal String email){
        return commentService.delete(commentId, email);
    }
//    @ApiOperation(value = "거래 게시글에 따른 댓글 조회", notes = "게시글 조회시 게시글 내용 + 댓글들 데이터로 수정 예정, 댓글 response 폼은 그대로")
//    @GetMapping("/findByPostId")
//    DataResponse<List<CommentResponseDTO.CommentResponse>> findByPostId(@RequestParam Long postId){
//        return commentService.findByPostId(postId);
//    }

}

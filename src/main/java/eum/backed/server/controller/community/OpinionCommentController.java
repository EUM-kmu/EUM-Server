package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.service.community.OpinionCommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("opinionComment/")
@RestController
@RequiredArgsConstructor
@Api(tags = "의견 댓글")
public class OpinionCommentController {
    private final OpinionCommentService opinionCommentService;
    @PostMapping("/create")
    public DataResponse create(@RequestBody CommentRequestDTO.Create create, @AuthenticationPrincipal String email){
        return opinionCommentService.create(create, email);
    }

    @PutMapping()
    public DataResponse update(@RequestBody CommentRequestDTO.Update update,@AuthenticationPrincipal String email){
        return opinionCommentService.update(update, email);
    }

    @DeleteMapping
    public DataResponse delete(@RequestParam Long opinionPostId,@AuthenticationPrincipal String email){
        return opinionCommentService.delete(opinionPostId, email);
    }

}

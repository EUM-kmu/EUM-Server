package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    public APIResponse createComment(Long postId, CommentRequestDTO.Create create, String email, CommentType commentType);

    public APIResponse updateComment(Long postId, CommentRequestDTO.Update update, String email, CommentType commentType);

    public APIResponse deleteComment(Long commentId,String email, CommentType commentType );
}

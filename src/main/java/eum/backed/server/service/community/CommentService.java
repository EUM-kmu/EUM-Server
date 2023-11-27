package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    public APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, String email, CommentType commentType);

    public APIResponse updateComment(Long postId, CommentRequestDTO.CommentUpdate commentUpdate, String email, CommentType commentType);

    public APIResponse deleteComment(Long commentId,String email, CommentType commentType );
}

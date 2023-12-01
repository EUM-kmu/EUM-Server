package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.domain.community.comment.CommentType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<CommentResponseDTO.CommentResponse> getComments(Long postId, String email, CommentType commentType, Pageable pageable);

    APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, String email, CommentType commentType);

    APIResponse updateComment(Long postId, CommentRequestDTO.CommentUpdate commentUpdate, String email, CommentType commentType);

    APIResponse deleteComment(Long commentId,String email, CommentType commentType );
}

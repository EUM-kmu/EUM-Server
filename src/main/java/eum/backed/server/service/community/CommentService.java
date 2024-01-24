package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.request.CommentRequestDTO;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.comments.CommentType;

import java.util.List;

@Service
public interface CommentService {
    List<CommentResponseDTO.CommentResponse> getComments(Long postId, Long userId, Pageable pageable);

    APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, Long userId);

    APIResponse updateComment(Long postId, CommentRequestDTO.CommentUpdate commentUpdate, Long userId);

    APIResponse deleteComment(Long commentId,Long userId);
}

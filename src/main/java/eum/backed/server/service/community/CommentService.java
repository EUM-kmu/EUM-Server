package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.CommentType;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    public DataResponse createComment(CommentRequestDTO.Create create, String email, CommentType commentType);

    public DataResponse updateComment(CommentRequestDTO.Update update, String email, CommentType commentType);

    public DataResponse deleteComment(Long commentId,String email, CommentType commentType );
}

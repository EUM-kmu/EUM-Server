package eum.backed.server.service;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteCommentService {
    private final VoteCommentRepository voteCommentRepository;

    public DataResponse create(CommentRequestDTO.Create create, String email) {
    }
}

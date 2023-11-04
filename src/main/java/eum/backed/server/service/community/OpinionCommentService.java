package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.OpinionCommentRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpinionCommentService {
    private final OpinionCommentRepository opinionCommentRepository;
    private final UsersRepository userRepository;
    private final OpinionPostRepository opinionPostRepository;

    public DataResponse create(CommentRequestDTO.Create create, String email) {
        OpinionPost getOpinionPost = opinionPostRepository.findById(create.getPostId()).orElseThrow(() -> new NullPointerException("invalid id"));
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid user"));
        if( getUser.getRole() == Role.ROLE_TEMPORARY_USER) throw new IllegalArgumentException("프로필 없는 유저");
        OpinionComment opinionComment = OpinionComment.toEntity(create.getContent(), getUser, getOpinionPost);
        opinionCommentRepository.save(opinionComment);
        return new DataResponse().success("데이터 저장 성공");
    }

    public DataResponse update(CommentRequestDTO.Update update, String email) {
        OpinionComment getOpinionComment = opinionCommentRepository.findById(update.getCommentId()).orElseThrow(() -> new NullPointerException("Invalid id"));
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid user"));
        if(getOpinionComment.getUser() != getUser) throw new IllegalArgumentException("권한이 없습니다");
        getOpinionComment.updateComment(update.getContent());
        return new DataResponse().success("수정 성공");
    }

    public DataResponse delete(Long opinionPostId, String email) {
        OpinionComment getOpinionComment = opinionCommentRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("Invalid id"));
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid user"));
        if(getOpinionComment.getUser() != getUser) throw new IllegalArgumentException("권한이 없습니다");
        opinionCommentRepository.delete(getOpinionComment);
        return new DataResponse().success("삭제 성공");
    }
}

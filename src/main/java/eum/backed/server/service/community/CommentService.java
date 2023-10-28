package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.Response;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.domain.community.comment.TransactionComment;
import eum.backed.server.domain.community.comment.TransactionCommentRepository;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.TransactionPostRepository;
import eum.backed.server.domain.community.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TransactionCommentRepository transactionCommentRepository;
    private final TransactionPostRepository transactionPostRepository;
    private final CommentResponseDTO commentResponseDTO;

    public DataResponse create(CommentRequestDTO.Create create, Users user) {
        TransactionPost getTransactionPost = transactionPostRepository.findById(create.getTransactionPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
        TransactionComment transactionComment = TransactionComment.builder()
                .content(create.getContent())
                .transactionPost(getTransactionPost)
                .user(user).build();
        transactionCommentRepository.save(transactionComment);
        return new DataResponse<>(Response.class).success("댓글 작성 성공");
    }

    public DataResponse update(CommentRequestDTO.Update update, Users user) {
        TransactionComment getTransactionComment = transactionCommentRepository.findById(update.getCommentId()).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(user.getUserId() != getTransactionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getTransactionComment.updateContent(update.getContent());
        transactionCommentRepository.save(getTransactionComment);
        return new DataResponse<>(Response.class).success("댓글 수정 성공");
    }

    public DataResponse delete(Long categoryId, Users user) {
        TransactionComment getTransactionComment = transactionCommentRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(user.getUserId() != getTransactionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자)");
        transactionCommentRepository.delete(getTransactionComment);
        return new DataResponse<>(Response.class).success("댓글 삭제 성공");
    }

    public DataResponse<List<CommentResponseDTO.CommentResponse>> findByPostId(Long postId) {
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        List<TransactionComment> transactionComments = transactionCommentRepository.findByTransactionPostOrderByCreateDateDesc(getTransactionPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> findByPost = getAllComment(transactionComments, getTransactionPost);
        return new DataResponse<>(findByPost).success(findByPost, "게시글 별 댓글 조회성공");
    }
    private List<CommentResponseDTO.CommentResponse> getAllComment(List<TransactionComment> transactionComments, TransactionPost transactionPost){
        List<CommentResponseDTO.CommentResponse> commentResponseArrayList = new ArrayList<>();
        for(TransactionComment transactionComment : transactionComments){
            boolean writer = (transactionPost.getUser().getUserId() == transactionComment.getUser().getUserId()) ? true : false;
            CommentResponseDTO.CommentResponse singleCommentResponse = commentResponseDTO.newCommentResponse(transactionComment, writer);
            commentResponseArrayList.add(singleCommentResponse);
        }
        return commentResponseArrayList;
    }
}

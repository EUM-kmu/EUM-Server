package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.CommentRequestDTO;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.comment.MarketCommentRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.auth.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eum.backed.server.controller.community.DTO.response.CommentResponseDTO.newCommentResponse;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final MarketCommentRepository marketCommentRepository;
    private final MarketPostRepository marketPostRepository;
    private final UsersRepository userRepository;
    @Override
    public List<CommentResponseDTO.CommentResponse> getComments(Long postId, Long userId, Pageable pageable) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        List<CommentResponseDTO.CommentResponse> commentResponses ;
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
        List<MarketComment> marketComments = marketCommentRepository.findByMarketPostOrderByCreateDateDesc(getMarketPost,pageable).orElse(Collections.emptyList());
        commentResponses = marketComments.stream()
                .map(marketComment -> newCommentResponse(
                        marketComment.getMarketPost().getMarketPostId(),
                        marketComment.getMarketCommentId(),
                        getUser == marketComment.getUser(),
                        marketComment.getUser() == marketComment.getMarketPost().getUser(),
                        marketComment.getContent(),
                        marketComment.getCreateDate(),
                        marketComment.getUser()
                )).collect(Collectors.toList());
        return commentResponses;
    }
    @Override
    public APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
            MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
            MarketComment marketComment = MarketComment.toEntity(commentCreate.getContent(), getUser, getMarketPost);
            marketCommentRepository.save(marketComment);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }
    @Override
    public APIResponse updateComment(Long commentId, CommentRequestDTO.CommentUpdate commentUpdate, Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));

        MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketComment.updateContent(commentUpdate.getContent());
        marketCommentRepository.save(getMarketComment);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }
    @Override
    public APIResponse deleteComment(Long commentId, Long userId) {
        Users getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        marketCommentRepository.delete(getMarketComment);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

}

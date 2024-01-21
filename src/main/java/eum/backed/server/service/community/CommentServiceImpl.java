package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.CommentRequestDTO;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.domain.community.comment.MarketComment;
import eum.backed.server.domain.community.comment.MarketCommentRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.comments.CommentType;

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
    public List<CommentResponseDTO.CommentResponse> getComments(Long postId, String email, Pageable pageable) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
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
    public APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
            MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
            MarketComment marketComment = MarketComment.toEntity(commentCreate.getContent(), getUser, getMarketPost);
            marketCommentRepository.save(marketComment);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }
    @Override
    public APIResponse updateComment(Long commentId, CommentRequestDTO.CommentUpdate commentUpdate, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));

        MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getMarketComment.updateContent(commentUpdate.getContent());
        marketCommentRepository.save(getMarketComment);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }
    @Override
    public APIResponse deleteComment(Long commentId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
        if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        marketCommentRepository.delete(getMarketComment);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

}

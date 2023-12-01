package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import eum.backed.server.domain.community.comment.*;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.votepost.VotePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eum.backed.server.controller.community.dto.response.CommentResponseDTO.newCommentResponse;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final MarketCommentRepository marketCommentRepository;
    private final MarketPostRepository marketPostRepository;
    private final OpinionCommentRepository opinionCommentRepository;
    private final OpinionPostRepository opinionPostRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VotePostRepository votePostRepository;
    private final UsersRepository userRepository;
    @Override
    public List<CommentResponseDTO.CommentResponse> getComments(Long postId, String email, CommentType commentType, Pageable pageable) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<CommentResponseDTO.CommentResponse> commentResponses = new ArrayList<>();
        if(commentType == CommentType.TRANSACTION){
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
        }else if(commentType == CommentType.OPINION){
            OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(()-> new NullPointerException("Invalid argument"));
            List<OpinionComment> opinionComments = opinionCommentRepository.findByOpinionPostOrderByCreateDateDesc(getOpinionPost).orElse(Collections.emptyList());
            commentResponses = opinionComments.stream()
                    .map(opinionComment -> newCommentResponse(
                            opinionComment.getOpinionPost().getOpinionPostId(),
                            opinionComment.getOpinionCommentId(),
                            getUser == opinionComment.getUser(),
                            opinionComment.getUser() == opinionComment.getUser(),
                            opinionComment.getComment(),
                            opinionComment.getCreateDate(),
                            opinionComment.getUser()
                    )).collect(Collectors.toList());
        }else if (commentType == CommentType.VOTE){
            VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            List<VoteComment> voteComments = voteCommentRepository.findByVotePostOrderByCreateDateDesc(getVotePost).orElse(Collections.emptyList());
            commentResponses = voteComments.stream()
                    .map(voteComment -> newCommentResponse(
                            voteComment.getVotePost().getVotePostId(),
                            voteComment.getVoteCommentId(),
                            getUser == voteComment.getUser(),
                            voteComment.getUser() == voteComment.getUser(),
                            voteComment.getContent(),
                            voteComment.getCreateDate(),
                            voteComment.getUser()
                    )).collect(Collectors.toList());

        }
        return commentResponses;
    }
    @Override
    public APIResponse createComment(Long postId, CommentRequestDTO.CommentCreate commentCreate, String email, CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if(commentType == CommentType.TRANSACTION){
            MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
            MarketComment marketComment = MarketComment.toEntity(commentCreate.getContent(), getUser, getMarketPost);
            marketCommentRepository.save(marketComment);
        }else if(commentType == CommentType.OPINION){
            OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(()-> new NullPointerException("Invalid argument"));
            OpinionComment opinionComment = OpinionComment.toEntity(commentCreate.getContent(), getUser, getOpinionPost);
            opinionCommentRepository.save(opinionComment);
        }else if (commentType == CommentType.VOTE){
            VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            VoteComment voteComment = VoteComment.toEntity(commentCreate.getContent(), getUser, getVotePost);
            voteCommentRepository.save(voteComment);
        }
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }
    @Override
    public APIResponse updateComment(Long commentId, CommentRequestDTO.CommentUpdate commentUpdate, String email, CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));

        if(commentType == CommentType.TRANSACTION){
            MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
            if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getMarketComment.updateContent(commentUpdate.getContent());
            marketCommentRepository.save(getMarketComment);;
        }else if(commentType == CommentType.OPINION){
            OpinionComment getOpinionComment = opinionCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getOpinionComment.updateComment(commentUpdate.getContent());
            opinionCommentRepository.save(getOpinionComment);
        }else if (commentType == CommentType.VOTE){
            VoteComment getVoteComment = voteCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getVoteComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getVoteComment.updateContent(commentUpdate.getContent());
            voteCommentRepository.save(getVoteComment);
        }
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }
    @Override
    public APIResponse deleteComment(Long commentId, String email,CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if(commentType == CommentType.TRANSACTION){
            MarketComment getMarketComment = marketCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
            if(getUser.getUserId() != getMarketComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            marketCommentRepository.delete(getMarketComment);;
        }else if(commentType == CommentType.OPINION){
            OpinionComment getOpinionComment = opinionCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            opinionCommentRepository.delete(getOpinionComment);
        }else if (commentType == CommentType.VOTE){
            VoteComment getVoteComment = voteCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getVoteComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            voteCommentRepository.delete(getVoteComment);
        }
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

}

package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.CommentRequestDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import eum.backed.server.domain.community.comment.*;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.votepost.VotePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TransactionCommentRepository transactionCommentRepository;
    private final MarketPostRepository marketPostRepository;
    private final OpinionCommentRepository opinionCommentRepository;
    private final OpinionPostRepository opinionPostRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VotePostRepository votePostRepository;
    private final UsersRepository userRepository;

    @Override
    public DataResponse createComment(Long postId,CommentRequestDTO.Create create, String email,CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if(commentType == CommentType.TRANSACTION){
            MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postID"));
            TransactionComment transactionComment = TransactionComment.toEntity(create.getContent(), getUser, getMarketPost);
            transactionCommentRepository.save(transactionComment);
        }else if(commentType == CommentType.OPINION){
            OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(()-> new NullPointerException("Invalid argument"));
            OpinionComment opinionComment = OpinionComment.toEntity(create.getContent(), getUser, getOpinionPost);
            opinionCommentRepository.save(opinionComment);
        }else if (commentType == CommentType.VOTE){
            VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            VoteComment voteComment = VoteComment.toEntity(create.getContent(), getUser, getVotePost);
            voteCommentRepository.save(voteComment);
        }
        return new DataResponse<>().success("댓글 작성 성공");
    }
    @Override
    public DataResponse updateComment(Long commentId,CommentRequestDTO.Update update, String email,CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));

        if(commentType == CommentType.TRANSACTION){
            TransactionComment getTransactionComment = transactionCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
            if(getUser.getUserId() != getTransactionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getTransactionComment.updateContent(update.getContent());
            transactionCommentRepository.save(getTransactionComment);;
        }else if(commentType == CommentType.OPINION){
            OpinionComment getOpinionComment = opinionCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getOpinionComment.updateComment(update.getContent());
            opinionCommentRepository.save(getOpinionComment);
        }else if (commentType == CommentType.VOTE){
            VoteComment getVoteComment = voteCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getVoteComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            getVoteComment.updateContent(update.getContent());
            voteCommentRepository.save(getVoteComment);
        }
        return new DataResponse<>().success("댓글 수정 성공");
    }
    @Override
    public DataResponse deleteComment(Long commentId, String email,CommentType commentType) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if(commentType == CommentType.TRANSACTION){
            TransactionComment getTransactionComment = transactionCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid commentID"));
            if(getUser.getUserId() != getTransactionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            transactionCommentRepository.delete(getTransactionComment);;
        }else if(commentType == CommentType.OPINION){
            OpinionComment getOpinionComment = opinionCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");if(getUser.getUserId() != getOpinionComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            opinionCommentRepository.delete(getOpinionComment);
        }else if (commentType == CommentType.VOTE){
            VoteComment getVoteComment = voteCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Invalid argument"));
            if(getUser.getUserId() != getVoteComment.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
            voteCommentRepository.delete(getVoteComment);
        }
        return new DataResponse<>().success("댓글 수정 성공");
    }

}

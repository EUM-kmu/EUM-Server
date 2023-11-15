package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.votepost.VotePostRepository;
import eum.backed.server.domain.community.voteresult.VoteResult;
import eum.backed.server.domain.community.voteresult.VoteResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotePostService {
    private final VotePostRepository votePostRepository;
    private final UsersRepository usersRepository;
    private final VoteResultRepository voteResultRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VotePostResponseDTO votePostResponseDTO;
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);


    public DataResponse create(VotePostRequestDTO.Create create, String email) throws ParseException {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        if(getUser.getRole() != Role.ROLE_USER) throw new IllegalArgumentException("프로필이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);
        VotePost votePost = VotePost.toEntity(create.getTitle(), create.getContent(), simpleDateFormat.parse(create.getEndDate()), getUser);
        votePostRepository.save(votePost);
        return new DataResponse().success("게시글 작성 성공");
    }

    public DataResponse update(VotePostRequestDTO.Update update,String email) throws ParseException {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(update.getVotePostId()).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if(getVotePost.getUser() != getUser) throw new IllegalArgumentException("수정권한이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);
        getVotePost.updateContent(update.getContent());
        getVotePost.updateTitle(update.getTitle());
        getVotePost.updateEndTime(simpleDateFormat.parse(update.getEndDate()));
        votePostRepository.save(getVotePost);
        return new DataResponse().success("게시글 수정 성공");
    }

    public DataResponse delete(Long votePostId, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(votePostId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if(getVotePost.getUser() != getUser) throw new IllegalArgumentException("삭제권한이 없는 유저");
        votePostRepository.delete(getVotePost);
        return new DataResponse().success("게시글 삭제 성공");
    }
    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<VotePost> votePosts = votePostRepository.findByDongOrderByCreateDate(getUser.getProfile().getDong()).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return new DataResponse<>(votePostResponses).success(votePostResponses,"전체 투표 게시글 조회");
    }

    public DataResponse voting(VotePostRequestDTO.Voting voting, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(voting.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if (voteResultRepository.existsByUserAndVotePost(getUser,getVotePost)) throw new IllegalArgumentException("이미 투표한 사람");
        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(Instant.from(now));
        if(currentDate.after(getVotePost.getEndTime())) throw new RuntimeException("시간이 지나서 투표를 할 수 없습니다");

        VoteResult voteResult = VoteResult.toEntity(voting.getAgree(), getUser, getVotePost);
        voteResultRepository.save(voteResult);
        reflectResult(getVotePost,voting.getAgree());
        votePostRepository.save(getVotePost);
        return new DataResponse().success("투표 성공");
    }
    @Scheduled(cron = "0 * * * * *")
    public DataResponse<VotePostResponseDTO.VotePostWithComment> getVotePostWithComment(Long postId, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        List<VoteComment> voteComments = voteCommentRepository.findByVotePostOrderByCreateDateDesc(getVotePost).orElse(Collections.emptyList());
        Boolean amIVote = voteResultRepository.existsByUserAndVotePost(getUser,getVotePost);
        List<CommentResponseDTO.CommentResponse> commentResponses = voteComments.stream().map(voteComment ->{
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(postId)
                    .commentId(voteComment.getVoteCommentId())
                    .commentNickName(voteComment.getUser().getProfile().getNickname())
                    .commentUserAddress(voteComment.getUser().getProfile().getDong().getDong())
                    .isPostWriter(getVotePost.getUser() == voteComment.getUser())
                    .createdTime(voteComment.getCreateDate())
                    .commentContent(voteComment.getContent()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(Instant.from(now));
        Boolean isVoting = currentDate.before(getVotePost.getEndTime());
        VotePostResponseDTO.VotePostWithComment votePostWithComment = votePostResponseDTO.newVotePostWithComment(getVotePost,commentResponses,isVoting,amIVote);
        return new DataResponse<>(votePostWithComment).success("게시글 조회 + 댓글 목록");
    }
    private void reflectResult(VotePost votePost, Boolean IsAgree){
        if (IsAgree) {
            votePost.addAgreeCount();
        }else{
            votePost.addDisagreeCount();
        }
        votePost.addTotal();
    }



}

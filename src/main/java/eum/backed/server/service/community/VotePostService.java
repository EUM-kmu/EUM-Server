package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.region.DONG.Township;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import eum.backed.server.domain.community.votepost.VotePost;
import eum.backed.server.domain.community.votepost.VotePostRepository;
import eum.backed.server.domain.community.voteresult.VoteResult;
import eum.backed.server.domain.community.voteresult.VoteResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        if(getUser.getRole() == Role.ROLE_TEMPORARY_USER) throw new IllegalArgumentException("프로필이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.KOREAN);
        VotePost votePost = VotePost.toEntity(create.getTitle(), create.getContent(), simpleDateFormat.parse(create.getEndDate()), getUser);
        votePostRepository.save(votePost);
        return new DataResponse().success("게시글 작성 성공");
    }

    public DataResponse update(Long postId,VotePostRequestDTO.Update update,String email) throws ParseException {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if(getVotePost.getUser() != getUser) throw new IllegalArgumentException("수정권한이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.KOREAN);
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
    private DataResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(Township township) {
        List<VotePost> votePosts = votePostRepository.findByTownshipOrderByCreateDateDesc(township).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return new DataResponse<>(votePostResponses).success(votePostResponses,"전체 투표 게시글 조회");
    }

    public DataResponse voting(Long postId,VotePostRequestDTO.Voting voting, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if (voteResultRepository.existsByUserAndVotePost(getUser,getVotePost)) throw new IllegalArgumentException("이미 투표한 사람");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Date currentDate = Date.from(zonedDateTime.toInstant());
        if(currentDate.after(getVotePost.getEndTime())) throw new RuntimeException("시간이 지나서 투표를 할 수 없습니다");

        VoteResult voteResult = VoteResult.toEntity(voting.getAgree(), getUser, getVotePost);
        voteResultRepository.save(voteResult);
        reflectResult(getVotePost,voting.getAgree());
        votePostRepository.save(getVotePost);
        return new DataResponse().success("투표 성공");
    }
//    @Scheduled(cron = "0 * * * * *")
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
                    .commentUserAddress(voteComment.getUser().getProfile().getTownship().getName())
                    .isPostWriter(getVotePost.getUser() == voteComment.getUser())
                    .createdTime(voteComment.getCreateDate())
                    .commentContent(voteComment.getContent()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        VotePostResponseDTO.VotePostWithComment votePostWithComment = votePostResponseDTO.newVotePostWithComment(getVotePost,commentResponses,amIVote);
        return new DataResponse<>(votePostWithComment).success(votePostWithComment,"게시글 조회 + 댓글 목록");
    }
    private void reflectResult(VotePost votePost, Boolean IsAgree){
        if (IsAgree) {
            votePost.addAgreeCount();
        }else{
            votePost.addDisagreeCount();
        }
        votePost.addTotal();
    }


    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getMyPosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<VotePost> votePosts = votePostRepository.findByUserOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return new DataResponse<>(votePostResponses).success(votePostResponses," 내가 작성한 투표 게시글 조회");
    }

    private DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByKeyWord(String keyWord, Township township) {
        List<VotePost> votePosts = votePostRepository.findByTownshipAndTitleContainingOrderByCreateDateDesc(township, keyWord).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return new DataResponse<>(votePostResponses).success(votePostResponses,"투표 게시글 키워드 검색");
    }

    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByFilter(String keyword, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Township getTownship = getUser.getProfile().getTownship();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, getTownship);
        }
        return getAllVotePosts(getTownship);
    }
}

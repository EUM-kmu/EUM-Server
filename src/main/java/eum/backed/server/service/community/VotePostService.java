package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.ProfileResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.domain.community.VoteCommentRepository;
import eum.backed.server.domain.community.comment.VoteComment;
import eum.backed.server.domain.community.region.Regions;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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


    public APIResponse create(VotePostRequestDTO.Create create, String email) throws ParseException {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        if(getUser.getRole() == Role.ROLE_TEMPORARY_USER) throw new IllegalArgumentException("프로필이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.KOREAN);
        VotePost votePost = VotePost.toEntity(create.getTitle(), create.getContent(), simpleDateFormat.parse(create.getEndDate()), getUser);
        votePostRepository.save(votePost);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);}

    public APIResponse update(Long postId,VotePostRequestDTO.Update update,String email) throws ParseException {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if(getVotePost.getUser() != getUser) throw new IllegalArgumentException("수정권한이 없는 유저");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.KOREAN);
        getVotePost.updateContent(update.getContent());
        getVotePost.updateTitle(update.getTitle());
        getVotePost.updateEndTime(simpleDateFormat.parse(update.getEndDate()));
        votePostRepository.save(getVotePost);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }

    public APIResponse delete(Long votePostId, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(votePostId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        if(getVotePost.getUser() != getUser) throw new IllegalArgumentException("삭제권한이 없는 유저");
        votePostRepository.delete(getVotePost);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }
    private APIResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(Regions regions) {
        List<VotePost> votePosts = votePostRepository.findByRegionsOrderByCreateDateDesc(regions).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, votePostResponses);
    }

    public APIResponse voting(Long postId,VotePostRequestDTO.Voting voting, String email) {
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
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, "투표성공");
    }
//    @Scheduled(cron = "0 * * * * *")
    public APIResponse<VotePostResponseDTO.VotePostWithComment> getVotePostWithComment(Long postId, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        VotePost getVotePost = votePostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        List<VoteComment> voteComments = voteCommentRepository.findByVotePostOrderByCreateDateDesc(getVotePost).orElse(Collections.emptyList());
        Boolean amIVote = voteResultRepository.existsByUserAndVotePost(getUser,getVotePost);
        List<CommentResponseDTO.CommentResponse> commentResponses = voteComments.stream().map(voteComment ->{
            LocalDateTime utcDateTime = LocalDateTime.parse(voteComment.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);

            // UTC 시간을 한국 시간대로 변환
            ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));

            // 한국 시간대로 포맷팅
            String formattedDateTime = koreaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(postId)
                    .commentId(voteComment.getVoteCommentId())
                    .writerInfo(ProfileResponseDTO.toUserInfo(voteComment.getUser()))
                    .isPostWriter(getVotePost.getUser() == voteComment.getUser())
                    .createdTime(formattedDateTime)
                    .commentContent(voteComment.getContent()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        VotePostResponseDTO.VotePostWithComment votePostWithComment = votePostResponseDTO.newVotePostWithComment(getVotePost,commentResponses,amIVote);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, votePostWithComment);
    }
    private void reflectResult(VotePost votePost, Boolean IsAgree){
        if (IsAgree) {
            votePost.addAgreeCount();
        }else{
            votePost.addDisagreeCount();
        }
        votePost.addTotal();
    }


    public APIResponse<List<VotePostResponseDTO.VotePostResponses>> getMyPosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<VotePost> votePosts = votePostRepository.findByUserOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, votePostResponses);
    }

    private APIResponse<List<VotePostResponseDTO.VotePostResponses>> findByKeyWord(String keyWord, Regions regions) {
        List<VotePost> votePosts = votePostRepository.findByRegionsAndTitleContainingOrderByCreateDateDesc(regions, keyWord).orElse(Collections.emptyList());
        List<VotePostResponseDTO.VotePostResponses> votePostResponses = votePosts.stream().map(VotePostResponseDTO.VotePostResponses::new).collect(Collectors.toList());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,votePostResponses);
    }

    public APIResponse<List<VotePostResponseDTO.VotePostResponses>> findByFilter(String keyword, String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Regions getRegions = getUser.getProfile().getRegions();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, getRegions);
        }
        return getAllVotePosts(getRegions);
    }
}

package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.controller.community.dto.response.ProfileResponseDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.OpinionCommentRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpinionPostService {
    private final OpinionPostRepository opinionPostRepository;
    private final OpinionCommentRepository opinionCommentRepository;
    private final UsersRepository userRepository;
    private final OpinionResponseDTO opinionResponseDTO;

    public APIResponse create(OpinionPostRequestDTO.Create create, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (getUser.getRole() == Role.ROLE_TEMPORARY_USER ) throw new IllegalArgumentException("프로필이 없느 유저");
        Regions getRegions =getUser.getProfile().getRegions();
        OpinionPost opinionPost = OpinionPost.toEntity(create.getTitle(), create.getContent(), getUser, getRegions);
        opinionPostRepository.save(opinionPost);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS);
    }

    public APIResponse update(Long postId,OpinionPostRequestDTO.Update update, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("수정할 권한이 없습니다");
        getOpinionPost.updateContent(update.getContent());
        getOpinionPost.updateTitle(update.getTitle());
        opinionPostRepository.save(getOpinionPost);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS);
    }

    public APIResponse delete(Long opinionPostId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("삭제 권한이 없습니다");
        opinionPostRepository.delete(getOpinionPost);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(Regions regions) {
//        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByRegionsOrderByCreateDateDesc(regions).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }
    public APIResponse<OpinionResponseDTO.OpinionPostWithComment> getOpininonPostWithComment(Long opinionPostId) {
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        List<OpinionComment> opinionComments = opinionCommentRepository.findByOpinionPostOrderByCreateDateDesc(getOpinionPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> commentResponseDTOS = opinionComments.stream().map(opinionComment -> {
            LocalDateTime utcDateTime = LocalDateTime.parse(opinionComment.getCreateDate().toString(), DateTimeFormatter.ISO_DATE_TIME);

            // UTC 시간을 한국 시간대로 변환
            ZonedDateTime koreaZonedDateTime = utcDateTime.atZone(ZoneId.of("Asia/Seoul"));

            // 한국 시간대로 포맷팅
            String formattedDateTime = koreaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(opinionPostId)
                    .commentId(opinionComment.getOpinionCommentId())
                    .writerInfo(ProfileResponseDTO.toUserInfo(opinionComment.getUser()))
                    .isPostWriter(getOpinionPost.getUser() == opinionComment.getUser())
                    .createdTime(formattedDateTime)
                    .commentContent(opinionComment.getComment()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        OpinionResponseDTO.OpinionPostWithComment opinionPostWithComment = opinionResponseDTO.newOpinionPostWithComment(getOpinionPost,commentResponseDTOS);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, opinionPostWithComment);
    }
    private List<OpinionResponseDTO.AllOpinionPostsResponses> getAllOpinionResponseDTO(List<OpinionPost> opinionPosts) {
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = new ArrayList<>();
        for (OpinionPost opinionPost : opinionPosts) {
            OpinionResponseDTO.AllOpinionPostsResponses singleOpinionPostResponse = opinionResponseDTO.newOpinionPostsResponse(opinionPost);
            allOpinionPostsResponses.add(singleOpinionPostResponse);
        }
        return allOpinionPostsResponses;
    }


    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getHottestPosts(Regions regions) {
//        Users getUser = userRepository.findByEmail(regions).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
//        Regions getRegions = getUser.getProfile().getRegions();
//        int majorityCounts = regions.getProfiles().size()/2;
        List<OpinionPost> opinionPosts = opinionPostRepository.findByLikeCountGreaterThanOrderByLikeCountDesc(10).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    public APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getMyOpinionPosts(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByUserOrderByCreateDate(getUser).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByKeyWord(String keyWord, Regions regions) {
//        Users getUser = userRepository.findByEmail(regions).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByRegionsAndTitleContainingOrderByCreateDateDesc(regions, keyWord).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    public APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByFilter(String keyword, String isShow, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Regions getRegions = getUser.getProfile().getRegions();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, getRegions);
        }else if (isShow!=null &&isShow.equals("true") ){
            log.info(">>>>>."+isShow);
            return getHottestPosts(getRegions);
        }
        return getAllOpinionPosts(getRegions);
    }
}

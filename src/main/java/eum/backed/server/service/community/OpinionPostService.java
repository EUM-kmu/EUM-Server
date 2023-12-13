package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.Time;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.controller.community.dto.response.ProfileResponseDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.OpinionCommentRepository;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPostRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final LikeOpinionPostRepository likeOpinionPostRepository;

    public APIResponse<OpinionResponseDTO.SavedOpinionResponse> create(OpinionPostRequestDTO.Create create, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (getUser.getRole() == Role.ROLE_UNPROFILE_USER) throw new IllegalArgumentException("프로필이 없느 유저");
        Regions getRegions =getUser.getProfile().getRegions();
        OpinionPost opinionPost = OpinionPost.toEntity(create.getTitle(), create.getContent(), getUser, getRegions);
        OpinionPost savedOpinionPost = opinionPostRepository.save(opinionPost);
        OpinionResponseDTO.SavedOpinionResponse response = OpinionResponseDTO.toCreateResponse(savedOpinionPost, getUser);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS,response);
    }

    public APIResponse<OpinionResponseDTO.SavedOpinionResponse> update(Long postId, OpinionPostRequestDTO.Update update, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("수정할 권한이 없습니다");
        getOpinionPost.updateContent(update.getContent());
        getOpinionPost.updateTitle(update.getTitle());
        OpinionPost savedOpinionPost = opinionPostRepository.save(getOpinionPost);
        OpinionResponseDTO.SavedOpinionResponse response = OpinionResponseDTO.toCreateResponse(savedOpinionPost, getUser);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,response);
    }

    public APIResponse delete(Long opinionPostId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("삭제 권한이 없습니다");
        getOpinionPost.updateDeleted();
        opinionPostRepository.save(getOpinionPost);
        return APIResponse.of(SuccessCode.DELETE_SUCCESS);
    }

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(Regions regions,List<Users> blockedUsers) {
        List<OpinionPost> opinionPosts = opinionPostRepository.findByRegionsAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(regions,blockedUsers).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }
    public APIResponse<OpinionResponseDTO.OpinionPostWithComment> getOpininonPostWithComment(Long opinionPostId,String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        List<OpinionComment> opinionComments = opinionCommentRepository.findByOpinionPostOrderByCreateDateDesc(getOpinionPost).orElse(Collections.emptyList());
        boolean doLike = likeOpinionPostRepository.existsByUserAndOpinionPost(getUser, getOpinionPost);
        List<CommentResponseDTO.CommentResponse> commentResponseDTOS = opinionComments.stream().map(opinionComment -> {
            String createdTime = Time.localDateTimeToKoreaZoned(opinionComment.getCreateDate());
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(opinionPostId)
                    .commentId(opinionComment.getOpinionCommentId())
                    .writerInfo(ProfileResponseDTO.toUserInfo(opinionComment.getUser()))
                    .isPostWriter(getOpinionPost.getUser() == opinionComment.getUser())
                    .createdTime(createdTime)
                    .commentContent(opinionComment.getComment()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        OpinionResponseDTO.OpinionPostWithComment opinionPostWithComment = opinionResponseDTO.newOpinionPostWithComment(getOpinionPost,commentResponseDTOS,getUser,doLike);
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


    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getHottestPosts(Regions regions,List<Users> blockedUsers) {
//        Users getUser = userRepository.findByEmail(regions).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
//        Regions getRegions = getUser.getProfile().getRegions();
//        int majorityCounts = regions.getProfiles().size()/2;
        List<OpinionPost> opinionPosts = opinionPostRepository.findByRegionsAndLikeCountGreaterThanAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(regions,10,blockedUsers).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    public APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getMyOpinionPosts(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByUserAndIsDeletedFalseOrderByCreateDate(getUser).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByKeyWord(String keyWord, Regions regions,List<Users> blockedUsers) {
//        Users getUser = userRepository.findByEmail(regions).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByRegionsAndTitleContainingAndIsDeletedFalseAndUserNotInOrderByCreateDateDesc(regions, keyWord,blockedUsers).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    public APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByFilter(String keyword, String isShow, Users getUser, List<Users> blockedUsers) {
        Regions getRegions = getUser.getProfile().getRegions();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, getRegions,blockedUsers);
        }else if (isShow!=null &&isShow.equals("true") ){
            log.info(">>>>>."+isShow);
            return getHottestPosts(getRegions,blockedUsers);
        }
        return getAllOpinionPosts(getRegions,blockedUsers);
    }
}

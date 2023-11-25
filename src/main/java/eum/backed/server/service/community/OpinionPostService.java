package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.OpinionCommentRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.region.DONG.Township;
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

    public APIResponse create(OpinionPostRequestDTO.Create create, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (getUser.getRole() == Role.ROLE_TEMPORARY_USER ) throw new IllegalArgumentException("프로필이 없느 유저");
        Township getTownship =getUser.getProfile().getTownship();
        OpinionPost opinionPost = OpinionPost.toEntity(create.getTitle(), create.getContent(), getUser, getTownship);
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

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(Township township) {
//        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByTownshipOrderByCreateDateDesc(township).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }
    public APIResponse<OpinionResponseDTO.OpinionPostWithComment> getOpininonPostWithComment(Long opinionPostId) {
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        List<OpinionComment> opinionComments = opinionCommentRepository.findByOpinionPostOrderByCreateDateDesc(getOpinionPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> commentResponseDTOS = opinionComments.stream().map(opinionComment -> {
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(opinionPostId)
                    .commentId(opinionComment.getOpinionCommentId())
                    .commentNickName(opinionComment.getUser().getProfile().getNickname())
                    .commentUserAddress(opinionComment.getUser().getProfile().getTownship().getName())
                    .isPostWriter(getOpinionPost.getUser() == opinionComment.getUser())
                    .createdTime(opinionComment.getCreateDate())
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


    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getHottestPosts(Township township) {
//        Users getUser = userRepository.findByEmail(township).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
//        Township getTownship = getUser.getProfile().getTownship();
//        int majorityCounts = township.getProfiles().size()/2;
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

    private APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByKeyWord(String keyWord, Township township) {
//        Users getUser = userRepository.findByEmail(township).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByTownshipAndTitleContainingOrderByCreateDateDesc(township, keyWord).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, allOpinionPostsResponses);
    }

    public APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByFilter(String keyword, String isShow, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Township getTownship = getUser.getProfile().getTownship();
        if(!(keyword == null || keyword.isBlank())) {
            return findByKeyWord(keyword, getTownship);
        }else if (isShow!=null &&isShow.equals("true") ){
            log.info(">>>>>."+isShow);
            return getHottestPosts(getTownship);
        }
        return getAllOpinionPosts(getTownship);
    }
}

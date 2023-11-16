package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.domain.community.comment.OpinionComment;
import eum.backed.server.domain.community.comment.OpinionCommentRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.user.Role;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpinionPostService {
    private final OpinionPostRepository opinionPostRepository;
    private final OpinionCommentRepository opinionCommentRepository;
    private final UsersRepository userRepository;
    private final OpinionResponseDTO opinionResponseDTO;

    public DataResponse create(OpinionPostRequestDTO.Create create, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        if (getUser.getRole() == Role.ROLE_TEMPORARY_USER ) throw new IllegalArgumentException("프로필이 없느 유저");
        Dong getDong =getUser.getProfile().getDong();
        OpinionPost opinionPost = OpinionPost.toEntity(create.getTitle(), create.getContent(), getUser, getDong);
        opinionPostRepository.save(opinionPost);
        return new DataResponse().success("게시글 작성 성공");
    }

    public DataResponse update(OpinionPostRequestDTO.Update update, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(update.getOpinionPostId()).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("수정할 권한이 없습니다");
        getOpinionPost.updateContent(update.getContent());
        getOpinionPost.updateTitle(update.getTitle());
        opinionPostRepository.save(getOpinionPost);
        return new DataResponse().success("수정성공");
    }

    public DataResponse delete(Long opinionPostId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        if(getUser != getOpinionPost.getUser()) throw new IllegalArgumentException("삭제 권한이 없습니다");
        opinionPostRepository.delete(getOpinionPost);
        return new DataResponse().success("삭제 성공");
    }

    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByDongOrderByCreateDateDesc(getUser.getProfile().getDong()).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsRespons = getAllOpinionResponseDTO(opinionPosts);
        return new DataResponse<>(allOpinionPostsRespons).success(allOpinionPostsRespons, "마을 별 게시글 조회");
    }
    public DataResponse<OpinionResponseDTO.OpinionPostWithComment> getOpininonPostWithComment(Long opinionPostId) {
        OpinionPost getOpinionPost = opinionPostRepository.findById(opinionPostId).orElseThrow(() -> new NullPointerException("invalid id"));
        List<OpinionComment> opinionComments = opinionCommentRepository.findByOpinionPostOrderByCreateDateDesc(getOpinionPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> commentResponseDTOS = opinionComments.stream().map(opinionComment -> {
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(opinionPostId)
                    .commentId(opinionComment.getOpinionCommentId())
                    .commentNickName(opinionComment.getUser().getProfile().getNickname())
                    .commentUserAddress(opinionComment.getUser().getProfile().getDong().getDong())
                    .isPostWriter(getOpinionPost.getUser() == opinionComment.getUser())
                    .createdTime(opinionComment.getCreateDate())
                    .commentContent(opinionComment.getComment()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        OpinionResponseDTO.OpinionPostWithComment opinionPostWithComment = opinionResponseDTO.newOpinionPostWithComment(getOpinionPost,commentResponseDTOS);
        return new DataResponse<>(opinionPostWithComment).success(opinionPostWithComment, "의견 게시글 + 댓글 조회");
    }
    private List<OpinionResponseDTO.AllOpinionPostsResponses> getAllOpinionResponseDTO(List<OpinionPost> opinionPosts) {
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = new ArrayList<>();
        for (OpinionPost opinionPost : opinionPosts) {
            OpinionResponseDTO.AllOpinionPostsResponses singleOpinionPostResponse = opinionResponseDTO.newOpinionPostsResponse(opinionPost);
            allOpinionPostsResponses.add(singleOpinionPostResponse);
        }
        return allOpinionPostsResponses;
    }


    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getHottestPosts(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        Dong getDong = getUser.getProfile().getDong();
        int majorityCounts = getDong.getProfiles().size()/2;
        List<OpinionPost> opinionPosts = opinionPostRepository.findByLikeCountGreaterThanOrderByLikeCountDesc(majorityCounts).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return new DataResponse<>(allOpinionPostsResponses).success(allOpinionPostsResponses, "좋아요 과반주 정렬 + 좋아요 순");
    }

    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getMyOpinionPosts(String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByUserOrderByCreateDate(getUser).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return new DataResponse<>(allOpinionPostsResponses).success(allOpinionPostsResponses, "내가 작성한 의견 게시글 조회");
    }

    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByKeyWord(String keyWord, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid argument"));
        List<OpinionPost> opinionPosts = opinionPostRepository.findByDongAndTitleContainingOrderByCreateDateDesc(getUser.getProfile().getDong(), keyWord).orElse(Collections.emptyList());
        List<OpinionResponseDTO.AllOpinionPostsResponses> allOpinionPostsResponses = getAllOpinionResponseDTO(opinionPosts);
        return new DataResponse<>(allOpinionPostsResponses).success(allOpinionPostsResponses, "내가 작성한 의견 게시글 조회");
    }
}

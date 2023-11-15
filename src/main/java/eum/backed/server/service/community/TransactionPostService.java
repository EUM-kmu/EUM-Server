package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.category.TransactionCategory;
import eum.backed.server.domain.community.category.TransactionCategoryRepository;
import eum.backed.server.domain.community.comment.TransactionComment;
import eum.backed.server.domain.community.comment.TransactionCommentRepository;
import eum.backed.server.domain.community.region.DONG.Dong;
import eum.backed.server.domain.community.region.DONG.DongRepository;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.TransactionPostRepository;
import eum.backed.server.domain.community.transactionpost.Status;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionPostService {
    private final TransactionPostRepository transactionPostRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final ScrapRepository scrapRepository;
    private final PostResponseDTO postResponseDTO;
    private final UsersRepository usersRepository;
    private final DongRepository dongRepository;
    private final TransactionCommentRepository transactionCommentRepository;

    public DataResponse<Response> create(PostRequestDTO.Create create,String email) throws Exception {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Dong dong = dongRepository.findByDong(create.getDong()).orElseThrow(() -> new NullPointerException("Invalid address"));
        TransactionCategory getTransactionCategory = transactionCategoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);
        TransactionPost transactionPost = TransactionPost.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startDate(simpleDateFormat.parse(create.getStartTime()))
                .slot(create.getSlot())
                .pay(create.getPay())
                .dong(dong)
                .location(create.getLocation())
                .volunteerTime(create.getVolunteerTime())
                .providingHelp(create.isProvidingHelp())
                .maxNumOfPeople(create.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .transactionCategory(getTransactionCategory)
                .build();
        transactionPostRepository.save(transactionPost);
        return new DataResponse<>().success("게시글 작성 성공");
    }


    public DataResponse<Response> delete(Long postId,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getTransactionPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        transactionPostRepository.delete(getTransactionPost);
        return new DataResponse<>(Response.class).success("게시글 삭제 성공");
    }

    public DataResponse update(PostRequestDTO.Update update,String email) throws ParseException {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Dong dong = dongRepository.findByDong(update.getDong()).orElseThrow(() -> new NullPointerException("Invalid address"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(update.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getTransactionPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.KOREAN);
        getTransactionPost.updateTitle(update.getTitle());
        getTransactionPost.updateContents(update.getContent());
        getTransactionPost.updateSlot(update.getSlot());
        getTransactionPost.updateStartDate(simpleDateFormat.parse(update.getStartDate()));
        getTransactionPost.updateLocation(update.getLocation());
        getTransactionPost.updateDong(dong);
        transactionPostRepository.save(getTransactionPost);
        return new DataResponse<>(Response.class).success("게시글 수정 성공");

    }

    public DataResponse updateState(Long postId,Status status, String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getTransactionPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getTransactionPost.updateStatus(status);
        transactionPostRepository.save(getTransactionPost);
        return new DataResponse<>(Response.class).success("게시글 상태 수정 성공");
    }
    public DataResponse<PostResponseDTO.TransactionPostWithComment> getTransactionPostWithComment(Long postId) {
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        List<TransactionComment> transactionComments = transactionCommentRepository.findByTransactionPostOrderByCreateDateDesc(getTransactionPost).orElse(Collections.emptyList());
        List<CommentResponseDTO.CommentResponse> commentResponses = transactionComments.stream().map(transactionComment -> {
            CommentResponseDTO.CommentResponse commentResponse = CommentResponseDTO.CommentResponse.builder()
                    .postId(postId)
                    .commentId(transactionComment.getTransactionCommentId())
                    .commentNickName(transactionComment.getUser().getProfile().getNickname())
                    .commentUserAddress(transactionComment.getUser().getProfile().getDong().getDong())
                    .isPostWriter(getTransactionPost.getUser() == transactionComment.getUser())
                    .createdTime(transactionComment.getCreateDate())
                    .commentContent(transactionComment.getContent()).build();
            return commentResponse;
        }).collect(Collectors.toList());
        PostResponseDTO.TransactionPostWithComment singlePostResponse = postResponseDTO.newTransactionPostWithComment(getTransactionPost,commentResponses);
        return new DataResponse<>(singlePostResponse).success(singlePostResponse, "id에 따른 게시글 정보조회 + 댓글");

    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByCategory(Long categoryId) {
        TransactionCategory getTransactionCategory = transactionCategoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Invalid categoryId"));
        List<TransactionPost> transactionPosts = transactionPostRepository.findByTransactionCategoryOrderByCreateDateDesc(getTransactionCategory).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(transactionPosts);
        return new DataResponse<>(findByCategories).success(findByCategories,"카테고리 별 데이터 조회 성공");
    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByStatus(Status status) {
        List<TransactionPost> transactionPosts = transactionPostRepository.findByStatusOrderByCreateDateDesc(status).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(transactionPosts);
        return new DataResponse<>(findByCategories).success(findByCategories,"상태별 데이터 조회 성공");

    }
    public DataResponse<List<PostResponseDTO.PostResponse>> findByNeedHelper(Boolean needHelper) {
        List<TransactionPost> transactionPosts = transactionPostRepository.findByProvidingHelpOrderByCreateDateDesc(needHelper).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> findByCategories = getAllPostResponse(transactionPosts);
        return new DataResponse<>(findByCategories).success(findByCategories,"도움주기, 받기 데이터 조회 성공");

    }

    public DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<Scrap> scraps = scrapRepository.findByUserOrderByCreateDateDesc(user).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (Scrap scrap : scraps) {
            TransactionPost transactionPost = scrap.getTransactionPost();
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(transactionPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return new DataResponse<>(postResponseArrayList).success(postResponseArrayList, "내가 스크랩한 게시글 조회)");
    }
    private List<PostResponseDTO.PostResponse> getAllPostResponse(List<TransactionPost> transactionPosts){
        List<PostResponseDTO.PostResponse> postResponseArrayList = new ArrayList<>();
        for (TransactionPost transactionPost : transactionPosts) {
            PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(transactionPost);
            postResponseArrayList.add(singlePostResponse);
        }
        return postResponseArrayList;
    }


    public DataResponse<List<PostResponseDTO.PostResponse>> getMyPosts(String email) {
        Users getUser = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        List<TransactionPost> transactionPosts = transactionPostRepository.findByUserOrderByCreateDateDesc(getUser).orElse(Collections.emptyList());
        List<PostResponseDTO.PostResponse> transactionPostDTOs = getAllPostResponse(transactionPosts);
        return new DataResponse<>(transactionPostDTOs).success(transactionPostDTOs,"내가 작성한 거래게시글 조회 성공");
    }
}

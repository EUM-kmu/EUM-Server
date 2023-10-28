package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.category.TransactionCategory;
import eum.backed.server.domain.community.category.TransactionCategoryRepository;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.TransactionPostRepository;
import eum.backed.server.domain.community.transactionpost.Status;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TransactionPostService {
    private final TransactionPostRepository transactionPostRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final ScrapRepository scrapRepository;
    private final PostResponseDTO postResponseDTO;
    private final UsersRepository usersRepository;

    public DataResponse<Response> create(PostRequestDTO.Create create,String email) throws Exception {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        TransactionCategory getTransactionCategory = transactionCategoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREAN);
        TransactionPost transactionPost = TransactionPost.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startDate(simpleDateFormat.parse(create.getStartTime()))
                .pay(create.getPay())
                .location(create.getLocation())
                .volunteerTime(create.getVolunteerTime())
                .needHelper(create.isNeedHelper())
                .maxNumOfPeople(create.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .transactionCategory(getTransactionCategory)
                .build();
        transactionPostRepository.save(transactionPost);
        return new DataResponse<>(Response.class).success("게시글 작성 성공");
    }


    public DataResponse<Response> delete(Long postId,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getTransactionPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        transactionPostRepository.delete(getTransactionPost);
        return new DataResponse<>(Response.class).success("게시글 삭제 성공");
    }

    public DataResponse update(PostRequestDTO.Update update,String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(update.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getTransactionPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getTransactionPost.updateTitle(update.getTitle());
        getTransactionPost.updateContents(update.getContent());
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
    public DataResponse<PostResponseDTO.PostResponse> findById(Long postId) {
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        PostResponseDTO.PostResponse singlePostResponse = postResponseDTO.newPostResponse(getTransactionPost);
        return new DataResponse<>(singlePostResponse).success(singlePostResponse, "postId로 찾기");

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
        List<TransactionPost> transactionPosts = transactionPostRepository.findByNeedHelperOrderByCreateDateDesc(needHelper).orElse(Collections.emptyList());
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


}

package eum.backed.server.commumityapi.service;

import eum.backed.server.commumityapi.controller.dto.request.PostRequestDTO;
import eum.backed.server.common.DataResponse;
import eum.backed.server.common.Response;
import eum.backed.server.commumityapi.domain.category.Category;
import eum.backed.server.commumityapi.domain.category.CategoryRepository;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.post.PostRepository;
import eum.backed.server.commumityapi.domain.post.Status;
import eum.backed.server.commumityapi.domain.region.GU.Gu;
import eum.backed.server.commumityapi.domain.region.GU.GuRepository;
import eum.backed.server.commumityapi.domain.user.Users;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final GuRepository guRepository;

    public DataResponse<Response> create(PostRequestDTO.Create create, Users user) throws Exception {
        Gu getGu = guRepository.findById(create.getGuId()).orElseThrow(() -> new IllegalArgumentException("없는 구입니다"));
        Category getCategory = categoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd a hh:mm", Locale.KOREAN);
        Post post = Post.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startDate(simpleDateFormat.parse(create.getStartTime()))
                .endDate(simpleDateFormat.parse(create.getEndTime()))
                .pay(create.getPay())
                .location(create.getLocation())
                .volunteerTime(create.getVolunteerTime())
                .isHelper(create.isHelper())
                .maxNumOfPeople(create.getMaxNumOfPeople())
                .status(Status.RECRUITING)
                .user(user)
                .category(getCategory)
                .gu(getGu).build();
        postRepository.save(post);
        return new DataResponse<>(Response.class).success("게시글 작성 성공");
    }


    public DataResponse<Response> delete(Long postId,Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        postRepository.delete(getPost);
        return new DataResponse<>(Response.class).success("게시글 삭제 성공");
    }

    public DataResponse update(PostRequestDTO.Update update,Users user) {
        Post getPost = postRepository.findById(update.getPostId()).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getPost.updateTitle(update.getTitle());
        getPost.updateContents(update.getContent());
        postRepository.save(getPost);
        return new DataResponse<>(Response.class).success("게시글 수정 성공");

    }

    public DataResponse updateState(Long postId,Status status, Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid postId"));
        if(user.getUserId() != getPost.getUser().getUserId()) throw new IllegalArgumentException("잘못된 접근 사용자");
        getPost.updateStatus(status);
        postRepository.save(getPost);
        return new DataResponse<>(Response.class).success("게시글 상태 수정 성공");
    }
}

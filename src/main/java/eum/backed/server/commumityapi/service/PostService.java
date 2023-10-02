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

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final GuRepository guRepository;

    public DataResponse<Response> create(PostRequestDTO.Create create, Users user) throws Exception {
        Gu getGu = guRepository.findById(create.getGuId()).orElseThrow(() -> new IllegalArgumentException("없는 구입니다"));
        Category getCategory = categoryRepository.findById(create.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("없는 카테고리 입니다"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd");
        Post post = Post.builder()
                .title(create.getTitle())
                .contents(create.getContent())
                .startTime(simpleDateFormat.parse(create.getStartTime()))
                .endTime(simpleDateFormat.parse(create.getEndTime()))
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


    
}

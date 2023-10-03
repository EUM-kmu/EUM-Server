package eum.backed.server.commumityapi.service;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.commumityapi.domain.post.Post;
import eum.backed.server.commumityapi.domain.post.PostRepository;
import eum.backed.server.commumityapi.domain.scrap.Scrap;
import eum.backed.server.commumityapi.domain.scrap.ScrapRepository;
import eum.backed.server.commumityapi.domain.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;

    public DataResponse doScrap(Long postId, Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(()->{
            throw new IllegalArgumentException("Invalid postId");});
        if(scrapRepository.existsByUser(user)) throw new IllegalArgumentException("이미 스크랩 한 개시글 입니다");
        Scrap scrap = Scrap.builder().post(getPost).user(user).build();
        scrapRepository.save(scrap);
        return new DataResponse<>(Response.class).success("관심 게시글 등록 성공");
    }

}

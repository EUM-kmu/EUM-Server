package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.domain.community.post.Post;
import eum.backed.server.domain.community.post.PostRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;

    public DataResponse doScrap(Long postId, Users user) {
        Post getPost = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        if(scrapRepository.existsByPostAndUser(getPost,user)) throw new IllegalArgumentException("이미 스크랩 한 개시글 입니다");
        Scrap scrap = Scrap.builder().post(getPost).user(user).build();
        scrapRepository.save(scrap);
        return new DataResponse<>(Response.class).success("관심 게시글 등록 성공");
    }

    public DataResponse undoScrap(Long postId, Users user) throws IllegalAccessException {
        Post getPost = postRepository.findById(postId).orElseThrow(()->{
            throw new IllegalArgumentException("Invalid postId");});
        Scrap getScrap = scrapRepository.findByPostAndUser(getPost, user).orElseThrow(() -> new IllegalAccessException("잘못된 요청. doScrap 요청으로 보내세요"));
        scrapRepository.delete(getScrap);
        return new DataResponse<>(Response.class).success("관심 게시글 취소 성공");
    }
}

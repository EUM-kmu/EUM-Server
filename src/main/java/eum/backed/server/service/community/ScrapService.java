package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MarketPostRepository marketPostRepository;
    private final UsersRepository userRepository;

    public DataResponse scrap(Long postId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        if(scrapRepository.existsByMarketPostAndUser(getMarketPost,getUser)) {
            Scrap getScrap = scrapRepository.findByMarketPostAndUser(getMarketPost, getUser);
            scrapRepository.delete(getScrap);
            return new DataResponse<>().success("관심 게시글 취소 성공");
        }
        Scrap scrap = Scrap.builder().marketPost(getMarketPost).user(getUser).build();
        scrapRepository.save(scrap);
        return new DataResponse<>().success("관심 게시글 등록 성공");
    }

}

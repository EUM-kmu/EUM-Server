package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.marketpost.MarketPostRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.scrap.ScrapRepository;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final MarketPostRepository marketPostRepository;
    private final UsersRepository userRepository;

    public ResponseEntity<APIResponse> scrap(Long postId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        MarketPost getMarketPost = marketPostRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        if(scrapRepository.existsByMarketPostAndUser(getMarketPost,getUser)) {
            Scrap getScrap = scrapRepository.findByMarketPostAndUser(getMarketPost, getUser);
            scrapRepository.delete(getScrap);
            return new ResponseEntity<>(APIResponse.of(SuccessCode.DELETE_SUCCESS, "관심 게시글 삭제"), HttpStatus.OK);
        }
        Scrap scrap = Scrap.builder().marketPost(getMarketPost).user(getUser).build();
        scrapRepository.save(scrap);
        return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS, "관심 게시글 등록"),HttpStatus.CREATED);
    }

}

package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.transactionpost.TransactionPostRepository;
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
    private final TransactionPostRepository transactionPostRepository;
    private final UsersRepository userRepository;

    public DataResponse doScrap(Long postId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        if(scrapRepository.existsByTransactionPostAndUser(getTransactionPost,getUser)) throw new IllegalArgumentException("이미 스크랩 한 개시글 입니다");
        Scrap scrap = Scrap.builder().transactionPost(getTransactionPost).user(getUser).build();
        scrapRepository.save(scrap);
        return new DataResponse<>(Response.class).success("관심 게시글 등록 성공");
    }

    public DataResponse undoScrap(Long postId, String email) throws IllegalAccessException {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        TransactionPost getTransactionPost = transactionPostRepository.findById(postId).orElseThrow(()->{
            throw new IllegalArgumentException("Invalid postId");});
        Scrap getScrap = scrapRepository.findByTransactionPostAndUser(getTransactionPost, getUser).orElseThrow(() -> new IllegalAccessException("잘못된 요청. doScrap 요청으로 보내세요"));
        scrapRepository.delete(getScrap);
        return new DataResponse<>(Response.class).success("관심 게시글 취소 성공");
    }
}

package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPost;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPostRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
import eum.backed.server.domain.community.scrap.Scrap;
import eum.backed.server.domain.community.transactionpost.TransactionPost;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.domain.community.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeOpinionPostService {
    private final LikeOpinionPostRepository likeOpinionPostRepository;
    private final OpinionPostRepository opinionPostRepository;
    private final UsersRepository userRepository;

    public DataResponse like(Long postId, String email) {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        if(likeOpinionPostRepository.existsByUserAndOpinionPost(getUser,getOpinionPost)) throw new IllegalArgumentException("이미 젛이요 한 개시글 입니다");
        LikeOpinionPost likeOpinionPost = LikeOpinionPost.toEntity(getUser, getOpinionPost);
        likeOpinionPostRepository.save(likeOpinionPost);
        getOpinionPost.updateLikeCount(getOpinionPost.getOpinionComments().size());
        return new DataResponse<>(Response.class).success("좋아요 성공");
    }

    public DataResponse unLike(Long postId, String email)  {
        Users getUser = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("invalid email"));
        OpinionPost getOpinionPost = opinionPostRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Invalid postId"));
        LikeOpinionPost getLikeOpinionPost = likeOpinionPostRepository.findByUserAndOpinionPost(getUser,getOpinionPost).orElseThrow(()-> new NullPointerException("좋아요 하지 않았음, 잘못된 요청"));
        likeOpinionPostRepository.delete(getLikeOpinionPost);
        getOpinionPost.updateLikeCount(getOpinionPost.getOpinionComments().size());
        return new DataResponse<>(Response.class).success("좋아요 취소 성공");
    }
}

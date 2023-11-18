package eum.backed.server.service.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPost;
import eum.backed.server.domain.community.likeopinionpost.LikeOpinionPostRepository;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.domain.community.opinionpost.OpinionPostRepository;
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
        if(likeOpinionPostRepository.existsByUserAndOpinionPost(getUser,getOpinionPost)){
            LikeOpinionPost getLikeOpinionPost = likeOpinionPostRepository.findByUserAndOpinionPost(getUser,getOpinionPost);
            likeOpinionPostRepository.delete(getLikeOpinionPost);
            getOpinionPost.updateLikeCount(getOpinionPost.getOpinionComments().size());
            return new DataResponse().success("좋아요 취소 성공");
        }
        LikeOpinionPost likeOpinionPost = LikeOpinionPost.toEntity(getUser, getOpinionPost);
        likeOpinionPostRepository.save(likeOpinionPost);
        getOpinionPost.updateLikeCount(getOpinionPost.getOpinionComments().size());
        return new DataResponse().success("좋아요 성공");
    }

}

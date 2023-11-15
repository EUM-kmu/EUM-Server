package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.LikeOpinionPostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likeOpinion")
@RequiredArgsConstructor
public class LikeOpinionPostController {
    private final LikeOpinionPostService likeOpinionPostService;
    @GetMapping("/like")
    @ApiOperation(value = "관심 설정", notes = "관심 설정")
    public DataResponse doScrap(@RequestParam Long postId, @AuthenticationPrincipal String email) {
        return likeOpinionPostService.like(postId, email);
    }
    @GetMapping("/unlike")
    @ApiOperation(value = "관심 해제", notes = "관심 해제")
    public DataResponse undoScrap(@RequestParam Long postId,@AuthenticationPrincipal String email) throws IllegalAccessException {
        return likeOpinionPostService.unLike(postId,email);
    }
}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor@RequestMapping("/scrap")
@Api(tags = "관심 기능")
public class ScrapController {
    private final ScrapService scrapService;
    @GetMapping("/do")
    @ApiOperation(value = "관심 설정", notes = "관심 설정")
    public DataResponse doScrap(@RequestParam Long postId, @AuthenticationPrincipal Users user) {
        return scrapService.doScrap(postId, user);
    }
    @GetMapping("/undo")
    @ApiOperation(value = "관심 해제", notes = "관심 해제")
    public DataResponse undoScrap(@RequestParam Long postId,@AuthenticationPrincipal Users user) throws IllegalAccessException {
        return scrapService.undoScrap(postId,user);
    }
}

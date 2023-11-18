package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.enums.ServiceType;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.OpinionPostService;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("your-activity")
@RequiredArgsConstructor
@Api(tags = "your activity")
public class YourActivityController {
    private final MarketPostService marketPostService;
    private final OpinionPostService opinionPostService;
    private final VotePostService votePostService;
    @ApiOperation(value = "거래 게시글 관려 내서비스 조회", notes = "내 관심 거래글(scraplist), 내가 작성한 거래 게시글(market) ")
    @GetMapping("/{serviceType}")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByServiceType(@PathVariable ServiceType serviceType, @AuthenticationPrincipal String email){
        return marketPostService.findByServiceType(serviceType,email);
    }
    @GetMapping("/opinion")
    @ApiOperation(value = "내가 작성한 의견 게시물")
    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getMyOpinionPosts(@AuthenticationPrincipal String email){
        return opinionPostService.getMyOpinionPosts(email);
    }
    @GetMapping("/vote")
    @ApiOperation(value = "내가 작성한 투표 게시글")
    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getMyPosts(@AuthenticationPrincipal String email){
        return votePostService.getMyPosts(email);
    }
}

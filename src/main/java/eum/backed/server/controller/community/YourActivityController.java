package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.enums.ServiceType;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.OpinionPostService;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/{serviceType}")
    public ResponseEntity<APIResponse<List<PostResponseDTO.PostResponse>>> findByServiceType(@PathVariable ServiceType serviceType, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.findByServiceType(serviceType,email));
    }
    @GetMapping("/opinion")
    @ApiOperation(value = "내가 작성한 의견 게시물")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>>> getMyOpinionPosts(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(opinionPostService.getMyOpinionPosts(email));
    }
    @GetMapping("/vote")
    @ApiOperation(value = "내가 작성한 투표 게시글")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    public ResponseEntity<APIResponse<List<VotePostResponseDTO.VotePostResponses>>>getMyPosts(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(votePostService.getMyPosts(email));
    }
}

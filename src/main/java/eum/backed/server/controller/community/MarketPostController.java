package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.request.MarketPostRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.controller.community.DTO.request.enums.ServiceType;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.controller.community.DTO.response.PostResponseDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/market/post")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "market")
public class  MarketPostController {
    private final MarketPostService marketPostService;
    private final ScrapService scrapService;
    private final CommentService commentService;
    private final BlockService blockService;
    private final UsersService usersService;

    /**
     * 거래 게시글 작성
     * @param marketCreate : 작성할 게시글 내용
     * @param email : jwt에 담긴 email
     * @return
     * @throws Exception :
     */
    @ApiOperation(value = "게시글 작성", notes = "도움요청, 받기 게시글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping()
    public ResponseEntity<APIResponse<PostResponseDTO.MarketPostResponse>> create(@RequestBody @Validated MarketPostRequestDTO.MarketCreate marketCreate, @AuthenticationPrincipal String email ) throws Exception {
        return new ResponseEntity<>(marketPostService.create(marketCreate, email), HttpStatus.CREATED);
    }
    @ApiOperation(value = "게시글 삭제", notes = "게시글 아이디로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @DeleteMapping("/{postId}")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.delete(postId,email));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 아이디 받고 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PutMapping("/{postId}")
    public  ResponseEntity<APIResponse<PostResponseDTO.MarketPostResponse>> update(@PathVariable Long postId, @RequestBody @Validated MarketPostRequestDTO.MarketUpdate marketUpdate, @AuthenticationPrincipal String email) throws ParseException {
        return ResponseEntity.ok(marketPostService.update(postId,marketUpdate,email));
    }
    @ApiOperation(value = "게시글 상태 수정", notes = "게시글 아이디받고 거래 상태 상태 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PutMapping("/{postId}/status")
    public  ResponseEntity<APIResponse> updateState(@PathVariable Long postId, @RequestBody MarketPostRequestDTO.UpdateStatus status, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.updateState(postId,status.getStatus(), email));
    }
    @ApiOperation(value = "단일 게시글 조회", notes = "게시글 정보 + 댓글  조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("{postId}")
    public  ResponseEntity<APIResponse<PostResponseDTO.TransactionPostWithComment>> findById(@PathVariable Long postId,@AuthenticationPrincipal String email, Pageable pageable){
        List<CommentResponseDTO.CommentResponse> commentResponses = commentService.getComments(postId, email, CommentType.TRANSACTION,pageable);
        return ResponseEntity.ok(marketPostService.getTransactionPostWithComment(postId,email,commentResponses));
    }
    @ApiOperation(value = "필터 조회", notes = "필터 별 게시글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("")
    public  ResponseEntity<APIResponse<List<PostResponseDTO.PostResponse>>> findByFilter(@RequestParam(name = "search",required = false) String keyword, @RequestParam(name = "category",required = false) String category,
                                                                                         @RequestParam(name = "marketType",required = false) MarketType marketType, @RequestParam(name = "status",required = false) Status status,
                                                                                         @PageableDefault Pageable pageable, @AuthenticationPrincipal String email){
        Users getUser = usersService.findByEmail(email);
        List<Users> blockedUsrs = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(marketPostService.findByFilter(keyword,category,marketType,status,email,pageable,blockedUsrs
        ));
    }

    @GetMapping("/{postId}/scrap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "201", description = "관심 등록 성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @ApiOperation(value = "거래 게시글 관심설정", notes = "관심 설정")
    public  ResponseEntity<APIResponse> doScrap(@PathVariable Long postId, @AuthenticationPrincipal String email) {
        return scrapService.scrap(postId, email);
    }
    @ApiOperation(value = "거래 게시글 관려 내서비스 조회", notes = "내 관심 거래글(scrap), 내가 작성한 거래 게시글(postlist) , 지원한 게시글(apply)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/user-activity/{serviceType}")
    public ResponseEntity<APIResponse<List<PostResponseDTO.PostResponse>>> findByServiceType(@PathVariable ServiceType serviceType, @AuthenticationPrincipal String email){
        Users getUser = usersService.findByEmail(email);
        List<Users> blockedUser = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(marketPostService.findByServiceType(serviceType,email,blockedUser));
    }






}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.ErrorResponse;
import eum.backed.server.controller.community.DTO.request.MarketPostRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.MarketType;
import eum.backed.server.controller.community.DTO.request.enums.ServiceType;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.controller.community.DTO.response.MarketPostResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.domain.auth.user.Users;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.service.FileService;
import eum.backed.server.service.auth.UsersService;
import eum.backed.server.service.community.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/market/post")
@RequiredArgsConstructor
@Slf4j
public class  MarketPostController {
    private final MarketPostService marketPostService;
    private final ScrapService scrapService;
    private final CommentService commentService;
    private final BlockService blockService;
    private final UsersService usersService;
    private final FileService fileService;

    /**
     * 거래 게시글 작성
     * @param marketCreate : 작성할 게시글 내용
     * @return
     * @throws ParseException : 활동 날짜 parsing 에러 처리
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(consumes =  {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<APIResponse<MarketPostResponseDTO.MarketPostResponse>> create(@RequestPart(value = "request") @Validated MarketPostRequestDTO.MarketCreate marketCreate, @RequestPart(value = "files") List<MultipartFile> multipartFiles, @AuthenticationPrincipal  CustomUserDetails customUserDetails ) throws ParseException {
        fileService.uploadFiles(multipartFiles);
        return new ResponseEntity<>(marketPostService.create(marketCreate, Long.valueOf(customUserDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 게시글 삭제
     * @param postId : 게시글 id
     * @param customUserDetails : jwt에 담긴 email
     * @retur
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @DeleteMapping("/{postId}")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long postId,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(marketPostService.delete(postId,Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 게시글 수정
     * @param postId : 게시글 Id
     * @param marketUpdate : 수정할 내용
     * @return
     * @throws ParseException : 활동 날짜 parsing 에러
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/{postId}")
    public  ResponseEntity<APIResponse<MarketPostResponseDTO.MarketPostResponse>> update(@PathVariable Long postId, @RequestBody @Validated MarketPostRequestDTO.MarketUpdate marketUpdate, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws ParseException {
        return ResponseEntity.ok(marketPostService.update(postId,marketUpdate,Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 게시글 상태 수정
     * RECRUITING, RECRUITMENT_COMPLETED, TRANSACTION_COMPLETED
     * @param postId : 게시글 id
     * @param status : 바꿀 상태 프론트에서는 (모집중, 모집완료)만 요청 예정
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping("/{postId}/status")
    public  ResponseEntity<APIResponse> updateState(@PathVariable Long postId, @RequestBody MarketPostRequestDTO.UpdateStatus status, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(marketPostService.updateState(postId,status.getStatus(), Long.valueOf(customUserDetails.getUsername())));
    }

    /**
     * 게시글 id로 조회
     * @param postId : 게시글 id
     * @param pageable : 페이지네이션
     * @return : 게시글 정보 + 댓글들
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("{postId}")
    public  ResponseEntity<APIResponse<MarketPostResponseDTO.MarketPostWithComment>> findById(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        List<CommentResponseDTO.CommentResponse> commentResponses = commentService.getComments(postId, Long.valueOf(customUserDetails.getUsername()),pageable);
        return ResponseEntity.ok(marketPostService.getMarketPostWithComment(postId,Long.valueOf(customUserDetails.getUsername()),commentResponses));
    }

    /**
     *
     * @param keyword : 검색어 파라미터
     * @param category : 카테고리 명
     * @param marketType : REQUEST_HELP,PROVIDE_HELP (도움요청, 도움제공)
     * @param status :RECRUITING (모집중)
     * @param pageable : 페이지네이젼
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("")
    public  ResponseEntity<APIResponse<List<MarketPostResponseDTO.MarketPostResponse>>> findByFilter(@RequestParam(value = "search",required = false) String keyword, @RequestParam(name = "category",required = false) String category,
                                                                                               @RequestParam(name = "marketType",required = false) MarketType marketType, @RequestParam(name = "status",required = false) Status status,
                                                                                                     @PageableDefault Pageable pageable, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Users getUser = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
        List<Users> blockedUsers = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(marketPostService.findByFilter(keyword,category,marketType,status,pageable,blockedUsers));
    }


    /**
     * 게시글 관심 설정
     * @param postId : 게시글 Id
     * @return
     */
    @GetMapping("/{postId}/scrap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public  ResponseEntity<APIResponse> doScrap(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return scrapService.scrap(postId, Long.valueOf(customUserDetails.getUsername()));
    }

    /**
     * 내활동 타입별 게시글 조회 : 내가 스크랩 한 글, 내가 작성한 게시글, 내 지원 글
     * @param serviceType : scrap, postlist,apply
     * @param customUserDetails : jwt에 담긴 email
     * @return : 게시글 정보
     */

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/user-activity/{serviceType}")
    public ResponseEntity<APIResponse<List<MarketPostResponseDTO.MarketPostResponse>>> findByServiceType(@PathVariable ServiceType serviceType, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Users getUser = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
        List<Users> blockedUser = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(marketPostService.findByServiceType(serviceType,Long.valueOf(customUserDetails.getUsername()),blockedUser));
    }






}

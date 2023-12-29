package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.DTO.response.CommentResponseDTO;
import eum.backed.server.controller.community.DTO.response.OpinionResponseDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/opinion/post")
@RequiredArgsConstructor
@Api(tags = "opinion")
@CrossOrigin("*")
public class OpinionPostController {
    private final OpinionPostService opinionPostService;
    private final LikeOpinionPostService likeOpinionPostService;
    private final BlockService blockService;
    private final UsersService usersService;
    private final CommentService commentService;
    @ApiOperation(value = "의견 게시글 작성", notes = "의견 게시글 작성")
    @PostMapping()
    public ResponseEntity<APIResponse<OpinionResponseDTO.SavedOpinionResponse>> create(@RequestBody @Validated  OpinionPostRequestDTO.Create create , @AuthenticationPrincipal String email){
        return ResponseEntity.ok(opinionPostService.create(create, email));
    }
    @ApiOperation(value = "의견 게시글 수정", notes = "의견 게시글 수정")
    @PutMapping("/{postId}")
    public  ResponseEntity<APIResponse<OpinionResponseDTO.SavedOpinionResponse>> update(@PathVariable Long postId, @RequestBody OpinionPostRequestDTO.Update update, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(opinionPostService.update(postId,update, email));
    }
    @ApiOperation(value = "의견 게시글 삭제", notes = "의견 게시글 삭제")
    @DeleteMapping("/{postId}")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(opinionPostService.delete(postId, email));
    }
    @GetMapping
    @ApiOperation(value = "전체 의견 게시글 조회 및 필터 ", notes = "전체 의견게시글 조회 동한정, 뜨거운마을, 검색어")
    public  ResponseEntity<APIResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>>>findByFilter(@RequestParam(name = "search",required = false) String keyword,@RequestParam(name = "hottest",required = false ) String isShow, @AuthenticationPrincipal String email){
        Users getUser = usersService.findByEmail(email);
        List<Users> blockedUsers = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(opinionPostService.findByFilter(keyword,isShow,getUser,blockedUsers));
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "단일 의견 게시글 조회",notes = "입력받은 게시글의 정보 + 댓글")
    public  ResponseEntity<APIResponse<OpinionResponseDTO.OpinionPostWithComment>> getOpinionPostWithComment(@PathVariable Long postId, @AuthenticationPrincipal String email, Pageable pageable){
        List<CommentResponseDTO.CommentResponse> commentResponses = commentService.getComments(postId, email, CommentType.OPINION,pageable);
        return ResponseEntity.ok(opinionPostService.getOpininonPostWithComment(postId,email,commentResponses));
    }
    @GetMapping("/{postId}/like")
    @ApiOperation(value = "좋아요/좋아요 취소", notes = "db 에 좋아요 유무에 따른 처리")
    public  ResponseEntity<APIResponse> like(@PathVariable Long postId, @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(likeOpinionPostService.like(postId, email));
    }

    @GetMapping("/user-activity/postlist")
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



}

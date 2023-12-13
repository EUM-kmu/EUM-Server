package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.CommentResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.domain.community.comment.CommentType;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.BlockService;
import eum.backed.server.service.community.CommentService;
import eum.backed.server.service.community.UsersService;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vote/post")
@RequiredArgsConstructor
@Api(tags = "vote")
@CrossOrigin("*")
public class VotePostController {
    private final VotePostService votePostService;
    private final CommentService commentService;
    private final UsersService usersService;
    private final BlockService blockService;
    @PostMapping
    @ApiOperation(value = "투표 게시글 작성")
    public ResponseEntity<APIResponse<VotePostResponseDTO.SavedVotePost>> create(@RequestBody VotePostRequestDTO.Create create, @AuthenticationPrincipal String email) throws ParseException {
        return new ResponseEntity<>(votePostService.create(create, email), HttpStatus.CREATED);
    }
    @PutMapping("{postId}")
    @ApiOperation(value = "투표 게시글 수정")
    public  ResponseEntity<APIResponse<VotePostResponseDTO.SavedVotePost>> update(@PathVariable Long postId, @RequestBody VotePostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return ResponseEntity.ok(votePostService.update(postId,update,email));
    }
    @DeleteMapping("/{postId}")
    @ApiOperation(value = "투표 게시글 삭제")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long postId, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(votePostService.delete(postId, email));
    }
//    @GetMapping()
//    @ApiOperation(value = "전체 투표 게시글 조회")
//    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(@AuthenticationPrincipal String email){
//        return votePostService.getAllVotePosts(email);
//    }
    @GetMapping()
    @ApiOperation(value = "전체 투표 게시글 조회 , 검색 필터")
    public  ResponseEntity<APIResponse<List<VotePostResponseDTO.VotePostResponses>>> findByFilter(@RequestParam(name = "search",required = false) String keyword,@AuthenticationPrincipal String email){
        Users getUser = usersService.findByEmail(email);
        List<Users> blockedUsers = blockService.getBlockedUser(getUser);
        return ResponseEntity.ok(votePostService.findByFilter(keyword,getUser,blockedUsers));
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "게시글아이디별 출력", notes = "게시글 정보 + 댓글")
    public  ResponseEntity<APIResponse<VotePostResponseDTO.VotePostWithComment>> getVotePostWithComment(@PathVariable Long postId, @AuthenticationPrincipal String email, Pageable pageable){
        List<CommentResponseDTO.CommentResponse> commentResponses = commentService.getComments(postId, email,CommentType.VOTE, pageable);
        return ResponseEntity.ok(votePostService.getVotePostWithComment(postId, email,commentResponses));
    }
    @PostMapping("/{postId}/voting")
    @ApiOperation(value = "투표하기")
    public  ResponseEntity<APIResponse> voting(@PathVariable Long postId,@RequestBody VotePostRequestDTO.Voting voting,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(votePostService.voting(postId,voting, email));
    }
    @GetMapping("/user-activity/postlist")
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

//    @GetMapping("/search")
//    @ApiOperation(value = "투표 게시글 키워드 검색")
//    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByKeyWord(@RequestParam String keyWord, @AuthenticationPrincipal String email) {
//        return votePostService.findByKeyWord(keyWord, email);
//    }



}

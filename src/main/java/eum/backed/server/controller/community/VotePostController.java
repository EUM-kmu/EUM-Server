package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post/vote")
@RequiredArgsConstructor
@Api(tags = "vote")
@CrossOrigin("*")
public class VotePostController {
    private final VotePostService votePostService;
    @PostMapping

    @ApiOperation(value = "투표 게시글 작성")
    public ResponseEntity<APIResponse> create(@RequestBody VotePostRequestDTO.Create create, @AuthenticationPrincipal String email) throws ParseException {
        return new ResponseEntity<>(votePostService.create(create, email), HttpStatus.CREATED);
    }
    @PutMapping("{postId}")
    @ApiOperation(value = "투표 게시글 수정")
    public  ResponseEntity<APIResponse> update(@PathVariable Long postId,@RequestBody VotePostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
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
        return ResponseEntity.ok(votePostService.findByFilter(keyword,email));
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "게시글아이디별 출력", notes = "게시글 정보 + 댓글")
    public  ResponseEntity<APIResponse<VotePostResponseDTO.VotePostWithComment>> getVotePostWithComment(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(votePostService.getVotePostWithComment(postId, email));
    }
    @PostMapping("/{postId}/voting")
    @ApiOperation(value = "투표하기")
    public  ResponseEntity<APIResponse> voting(@PathVariable Long postId,@RequestBody VotePostRequestDTO.Voting voting,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(votePostService.voting(postId,voting, email));
    }


//    @GetMapping("/search")
//    @ApiOperation(value = "투표 게시글 키워드 검색")
//    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByKeyWord(@RequestParam String keyWord, @AuthenticationPrincipal String email) {
//        return votePostService.findByKeyWord(keyWord, email);
//    }



}

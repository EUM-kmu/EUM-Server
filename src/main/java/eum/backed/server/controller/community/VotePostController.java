package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post/vote")
@RequiredArgsConstructor
@Api(tags = "vote")
public class VotePostController {
    private final VotePostService votePostService;
    @PostMapping
    @ApiOperation(value = "투표 게시글 작성")
    public DataResponse create(@RequestBody VotePostRequestDTO.Create create, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.create(create, email);
    }
    @PutMapping("{postId}")
    @ApiOperation(value = "투표 게시글 수정")
    public DataResponse update(@PathVariable Long postId,@RequestBody VotePostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.update(postId,update,email);
    }
    @DeleteMapping("/{postId}")
    @ApiOperation(value = "투표 게시글 삭제")
    public DataResponse delete(@PathVariable Long postId, @AuthenticationPrincipal String email){
        return votePostService.delete(postId, email);
    }
//    @GetMapping()
//    @ApiOperation(value = "전체 투표 게시글 조회")
//    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(@AuthenticationPrincipal String email){
//        return votePostService.getAllVotePosts(email);
//    }
    @GetMapping()
    @ApiOperation(value = "전체 투표 게시글 조회 , 검색 필터")
    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByFilter(@RequestParam(name = "search",required = false) String keyword,@AuthenticationPrincipal String email){
        return votePostService.findByFilter(keyword,email);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "게시글아이디별 출력", notes = "게시글 정보 + 댓글")
    public DataResponse<VotePostResponseDTO.VotePostWithComment> getVotePostWithComment(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return votePostService.getVotePostWithComment(postId, email);
    }
    @PostMapping("/{postId}/voting")
    @ApiOperation(value = "투표하기")
    public DataResponse voting(@PathVariable Long postId,@RequestBody VotePostRequestDTO.Voting voting,@AuthenticationPrincipal String email){
        return votePostService.voting(postId,voting, email);
    }


//    @GetMapping("/search")
//    @ApiOperation(value = "투표 게시글 키워드 검색")
//    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> findByKeyWord(@RequestParam String keyWord, @AuthenticationPrincipal String email) {
//        return votePostService.findByKeyWord(keyWord, email);
//    }



}

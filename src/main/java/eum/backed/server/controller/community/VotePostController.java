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
@RequestMapping("/votepost")
@RequiredArgsConstructor
@Api(tags = "투표 게시글 작성")
public class VotePostController {
    private final VotePostService votePostService;
    @PostMapping("/create")
    @ApiOperation(value = "투표 게시글 작성")
    public DataResponse create(@RequestBody VotePostRequestDTO.Create create, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.create(create, email);
    }
    @PutMapping("/update")
    @ApiOperation(value = "투표 게시글 수정")
    public DataResponse update(@RequestBody VotePostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.update(update,email);
    }
    @DeleteMapping("/delete")
    @ApiOperation(value = "투표 게시글 삭제")
    public DataResponse delete(@RequestParam Long votePostId, @AuthenticationPrincipal String email){
        return votePostService.delete(votePostId, email);
    }
    @GetMapping("/getAllPosts")
    @ApiOperation(value = "전체 투표 게시글 조회")
    public DataResponse<List<VotePostResponseDTO.VotePostResponses>> getAllVotePosts(@AuthenticationPrincipal String email){
        return votePostService.getAllVotePosts(email);
    }

    @GetMapping("/getPostWithComments")
    @ApiOperation(value = "게시글아이디별 출력", notes = "게시글 정보 + 댓글")
    public DataResponse<VotePostResponseDTO.VotePostWithComment> getVotePostWithComment(@RequestParam Long postId,@AuthenticationPrincipal String email){
        return votePostService.getVotePostWithComment(postId, email);
    }
    @PostMapping("/voting")
    @ApiOperation(value = "투표하기")
    public DataResponse voting(@RequestParam VotePostRequestDTO.Voting voting,@AuthenticationPrincipal String email){
        return votePostService.voting(voting, email);
    }



}

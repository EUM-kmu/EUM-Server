package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.service.community.OpinionPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opinion")
@RequiredArgsConstructor
@Api(tags = "의견 게시글 api")
public class OpinionPostController {
    private final OpinionPostService opinionPostService;
    @ApiOperation(value = "의견 게시글 작성", notes = "의견 게시글 작성")
    @PostMapping("/create")
    public DataResponse create(@RequestBody OpinionPostRequestDTO.Create create , @AuthenticationPrincipal String email){
        return opinionPostService.create(create, email);
    }
    @ApiOperation(value = "의견 게시글 수정", notes = "의견 게시글 수정")
    @PutMapping
    public DataResponse update(@RequestBody OpinionPostRequestDTO.Update update,@AuthenticationPrincipal String email){
        return opinionPostService.update(update, email);
    }
    @ApiOperation(value = "의견 게시글 삭제", notes = "의견 게시글 삭제")
    @DeleteMapping
    public DataResponse delete(@RequestParam Long opinionPostId,@AuthenticationPrincipal String email){
        return opinionPostService.delete(opinionPostId, email);
    }
    @GetMapping
    @ApiOperation(value = "전체 의견 게시글 조회", notes = "전체 의견게시글 조회(동한정)")
    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(@AuthenticationPrincipal String email){
        return opinionPostService.getAllOpinionPosts(email);
    }


}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.service.community.LikeOpinionPostService;
import eum.backed.server.service.community.OpinionPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/opinion")
@RequiredArgsConstructor
@Api(tags = "opinion")
public class OpinionPostController {
    private final OpinionPostService opinionPostService;
    private final LikeOpinionPostService likeOpinionPostService;
    @ApiOperation(value = "의견 게시글 작성", notes = "의견 게시글 작성")
    @PostMapping()
    public DataResponse create(@RequestBody OpinionPostRequestDTO.Create create , @AuthenticationPrincipal String email){
        return opinionPostService.create(create, email);
    }
    @ApiOperation(value = "의견 게시글 수정", notes = "의견 게시글 수정")
    @PutMapping("/{postId}")
    public DataResponse update(@PathVariable Long postId,@RequestBody OpinionPostRequestDTO.Update update,@AuthenticationPrincipal String email){
        return opinionPostService.update(postId,update, email);
    }
    @ApiOperation(value = "의견 게시글 삭제", notes = "의견 게시글 삭제")
    @DeleteMapping("/{postId}")
    public DataResponse delete(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return opinionPostService.delete(postId, email);
    }
//    @GetMapping
//    @ApiOperation(value = "전체 의견 게시글 조회", notes = "전체 의견게시글 조회(동한정)")
//    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(@AuthenticationPrincipal String email){
//        return opinionPostService.getAllOpinionPosts(email);
//    }
    @GetMapping
    @ApiOperation(value = "전체 의견 게시글 조회 및 필터 ", notes = "전체 의견게시글 조회 동한정, 뜨거운마을, 검색어")
    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByFilter(@RequestParam(name = "search",required = false) String keyword,@RequestParam(name = "hottest",required = false ) String isShow, @AuthenticationPrincipal String email){
        return opinionPostService.findByFilter(keyword,isShow,email);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "단일 의견 게시글 조회",notes = "입력받은 게시글의 정보 + 댓글")
    public DataResponse<OpinionResponseDTO.OpinionPostWithComment> getOpinionPostWithComment(@PathVariable Long postId){
        return opinionPostService.getOpininonPostWithComment(postId);
    }
    @GetMapping("/{postId}/like")
    @ApiOperation(value = "좋아요/좋아요 취소", notes = "db 에 좋아요 유무에 따른 처리")
    public DataResponse like(@PathVariable Long postId, @AuthenticationPrincipal String email) {
        return likeOpinionPostService.like(postId, email);
    }
//    @GetMapping("/hottest")
//    @ApiOperation(value = "상위 좋아요 개수",notes = "주민의 과반수 이상")
//    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getHottestPosts(@AuthenticationPrincipal String email){
//        return opinionPostService.getHottestPosts(email);
//    }

//    @GetMapping("/search")
//    @ApiOperation(value = "의견 게시글 키워드 검색")
//    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> findByKeyword(@RequestParam String keyword, @AuthenticationPrincipal String email){
//        return opinionPostService.findByKeyWord(keyword, email);
//    }



}

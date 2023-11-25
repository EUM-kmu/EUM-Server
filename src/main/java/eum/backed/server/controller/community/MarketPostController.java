package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.MarketType;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.marketpost.Status;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post/market")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "market")
public class MarketPostController {
    private final MarketPostService marketPostService;
    private final ScrapService scrapService;


    @ApiOperation(value = "게시글 작성", notes = "도움요청, 받기 게시글 작성")
    @PostMapping()
    public ResponseEntity<APIResponse> create(@RequestBody PostRequestDTO.Create create, @AuthenticationPrincipal String email ) throws Exception {
        return ResponseEntity.ok(marketPostService.create(create, email));
    }
    @ApiOperation(value = "게시글 삭제", notes = "게시글 아이디로 삭제")
    @DeleteMapping("/{postId}")
    public  ResponseEntity<APIResponse> delete(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.delete(postId,email));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 아이디 받고 수정")
    @PutMapping("/{postId}")
    public  ResponseEntity<APIResponse> update(@RequestBody PostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return ResponseEntity.ok(marketPostService.update(update,email));
    }
    @ApiOperation(value = "게시글 상태 수정", notes = "게시글 아이디받고 거래 상태 상태 수정")
    @PutMapping("/{postId}/state")
    public  ResponseEntity<APIResponse> updateState(@PathVariable Long postId,@RequestParam Status status, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.updateState(postId,status, email));
    }
    @ApiOperation(value = "단일 게시글 조회", notes = "게시글 정보 + 댓글  조회")
    @GetMapping("{postId}")
    public  ResponseEntity<APIResponse<PostResponseDTO.TransactionPostWithComment>> findById(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.getTransactionPostWithComment(postId,email));
    }
    @ApiOperation(value = "필터 조회", notes = "필터 별 게시글 리스트 조회")
    @GetMapping("")
    public  ResponseEntity<APIResponse<List<PostResponseDTO.PostResponse>>> findByFilter(@RequestParam(name = "search",required = false) String keyword,@RequestParam(name = "category",required = false) Long categoryId, @RequestParam(name = "type",required = false) MarketType marketType, @RequestParam(name = "status",required = false) Status status, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(marketPostService.findByFilter(keyword,categoryId,marketType,status,email));
    }
//    @ApiOperation(value = "카테고리 별 게시글 조회", notes = "카테고리별 게시글 최신정렬")
//    @GetMapping("")
//    public DataResponse<List<PostResponseDTO.PostResponse>> findByCategory(@RequestParam Long categoryId,@AuthenticationPrincipal String email){
//        log.info(email);
//        return marketPostService.findByCategory(categoryId,email);
//    }
//    @ApiOperation(value = "상태 별 게시글 조회", notes = "상태별 게시글 최신 정렬")
//    @GetMapping("/findByStatus")
//    public DataResponse<List<PostResponseDTO.PostResponse>> findByStatus(@RequestParam Status status){
//        return marketPostService.findByStatus(status);
//    }
//    @ApiOperation(value = "도움 주기,받기 구분", notes = "도움 주기 , 받기에 따른 게시글 최신 정렬")
//    @GetMapping("/findByProvidingHelp")
//    public DataResponse<List<PostResponseDTO.PostResponse>> findByIsHelper(@RequestParam Boolean providingHelp){
//        return marketPostService.findByNeedHelper(providingHelp);
//    }
    @GetMapping("/{postId}/scrap")
    @ApiOperation(value = "거래 게시글 관심설정", notes = "관심 설정")
    public  ResponseEntity<APIResponse> doScrap(@PathVariable Long postId, @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(scrapService.scrap(postId, email));
    }

//
//    @GetMapping("/search")
//    @ApiOperation(value = "거래게시글 검색")
//    public DataResponse<List<PostResponseDTO.PostResponse>> findByKeyWord(@RequestParam String keyword, @AuthenticationPrincipal String email){
//        return marketPostService.findByKeyWord(keyword, email);
//    }





}

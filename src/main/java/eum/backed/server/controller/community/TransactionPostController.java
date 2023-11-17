package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.transactionpost.Status;
import eum.backed.server.service.community.TransactionPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/post/transaction")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "게시글")
public class TransactionPostController {
    private final TransactionPostService transactionPostService;


    @ApiOperation(value = "게시글 작성", notes = "도움요청, 받기 게시글 작성")
    @PostMapping()
    public DataResponse create(@RequestBody PostRequestDTO.Create create, @AuthenticationPrincipal String email ) throws Exception {
        return transactionPostService.create(create, email);
    }
    @ApiOperation(value = "게시글 삭제", notes = "게시글 아이디로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400",description = "잘못된 인자 접근")
    })
    @DeleteMapping("/{postId}")
    public DataResponse delete(@PathVariable Long postId,@AuthenticationPrincipal String email){
        return transactionPostService.delete(postId,email);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 아이디 받고 수정")
    @PutMapping("/{postId}")
    public DataResponse update(@RequestBody PostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return transactionPostService.update(update,email);
    }
    @ApiOperation(value = "게시글 상태 수정", notes = "게시글 아이디받고 거래 상태 상태 수정")
    @PutMapping("/updateStatus")
    public DataResponse updateState(@RequestParam Long postId,@RequestParam Status status, @AuthenticationPrincipal String email){
        return transactionPostService.updateState(postId,status, email);
    }
    @ApiOperation(value = "단일 게시글 조회", notes = "게시글 정보 + 댓글  조회")
    @GetMapping("{postId}")
    public DataResponse<PostResponseDTO.TransactionPostWithComment> findById(@PathVariable Long postId){
        return transactionPostService.getTransactionPostWithComment(postId);
    }
    @ApiOperation(value = "카테고리 별 게시글 조회", notes = "카테고리별 게시글 최신정렬")
    @GetMapping("/filter[cat]={categoryId}&filter[type]={providingHelp}&filter[status]={status}")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByFilter(@PathVariable Long categoryId,@PathVariable Boolean providingHelp,@PathVariable Status status){
        return transactionPostService.findByFilter(categoryId,providingHelp,status);
    }
    @ApiOperation(value = "카테고리 별 게시글 조회", notes = "카테고리별 게시글 최신정렬")
    @GetMapping("/filter[cat]={categoryId}&filter[type]={providingHelp}&filter[status]={status}")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByCategory(@RequestParam Long categoryId,@AuthenticationPrincipal String email){
        log.info(email);
        return transactionPostService.findByCategory(categoryId);
    }
    @ApiOperation(value = "상태 별 게시글 조회", notes = "상태별 게시글 최신 정렬")
    @GetMapping("/findByStatus")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByStatus(@RequestParam Status status){
        return transactionPostService.findByStatus(status);
    }
    @ApiOperation(value = "도움 주기,받기 구분", notes = "도움 주기 , 받기에 따른 게시글 최신 정렬")
    @GetMapping("/findByProvidingHelp")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByIsHelper(@RequestParam Boolean providingHelp){
        return transactionPostService.findByNeedHelper(providingHelp);
    }

    @ApiOperation(value = "관심게시글 목록 조회", notes = "나의 관심 게시글 목록 최신 정렬")
    @GetMapping("/findByScrap")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(@AuthenticationPrincipal String email){
        return transactionPostService.findByScrap(email);
    }

    @GetMapping("/myposts")
    @ApiOperation(value = "내가 작성한 거래 게시글")
    public DataResponse<List<PostResponseDTO.PostResponse>> getMyPosts(@AuthenticationPrincipal String email){
        return transactionPostService.getMyPosts(email);
    }
    @GetMapping("/search")
    @ApiOperation(value = "거래게시글 검색")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByKeyWord(@RequestParam String keyWord,@AuthenticationPrincipal String email){
        return transactionPostService.findByKeyWord(keyWord, email);
    }





}

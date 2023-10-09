package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.PostRequestDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.domain.community.post.Status;
import eum.backed.server.service.community.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;


    @ApiOperation(value = "게시글 작성", notes = "도움요청, 받기 게시글 작성")
    @PostMapping("/create")
    public DataResponse create(@RequestBody PostRequestDTO.Create create, @AuthenticationPrincipal String email ) throws Exception {
        System.out.println(create.isNeedHelper());
        return postService.create(create, email);
    }
    @ApiOperation(value = "게시글 삭제", notes = "게시글 아이디로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400",description = "잘못된 인자 접근")
    })
    @DeleteMapping
    public DataResponse delete(@RequestParam Long postId,@AuthenticationPrincipal String email){
        return postService.delete(postId,email);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 아이디 받고 수정")
    @PutMapping
    public DataResponse update(@RequestBody PostRequestDTO.Update update, @AuthenticationPrincipal String email){
        return postService.update(update,email);
    }
    @ApiOperation(value = "게시글 상태 수정", notes = "게시글 아이디받고 상태 수정")
    @PutMapping("/updateStatus")
    public DataResponse updateState(@RequestParam Long postId,@RequestParam Status status, @AuthenticationPrincipal String email){
        return postService.updateState(postId,status, email);
    }
    @ApiOperation(value = "단일 게시글 조회", notes = "게시글 아이디 받고 조회")
    @GetMapping("/findById")
    public DataResponse<PostResponseDTO.PostResponse> findById(@RequestParam Long postId){
        return postService.findById(postId);
    }
    @ApiOperation(value = "카테고리 별 게시글 조회", notes = "카테고리별 게시글 최신정렬")
    @GetMapping("/findByCategory")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByCategory(@RequestParam Long categoryId,@AuthenticationPrincipal String email){
        log.info(email);
        return postService.findByCategory(categoryId);
    }
    @ApiOperation(value = "상태 별 게시글 조회", notes = "상태별 게시글 최신 정렬")
    @GetMapping("/findByStatus")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByStatus(@RequestParam Status status){
        return postService.findByStatus(status);
    }
//    @ApiOperation(value = "상태 별 게시글 조회", notes = "상태별 게시글 최신 정렬")
//    @GetMapping("/findByStatus")
//    public DataResponse findByStatus(@RequestParam Status status){
//        try{
//            return postService.findByStatus(status);
//        }catch (Exception e){
//            return new DataResponse<>(Object.class).fail(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
    @ApiOperation(value = "도움 주기,받기 구분", notes = "도움 주기 , 받기에 따른 게시글 최신 정렬")
    @GetMapping("/findByNeedHelper")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByIsHelper(@RequestParam Boolean needHelper){
        return postService.findByNeedHelper(needHelper);
    }

    @ApiOperation(value = "관심게시글 목록 조회", notes = "나의 관심 게시글 목록 최신 정렬")
    @GetMapping("/findByScrap")
    public DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(String email){
        return postService.findByScrap(email);
    }





}

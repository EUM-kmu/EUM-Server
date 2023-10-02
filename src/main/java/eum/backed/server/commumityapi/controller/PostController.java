package eum.backed.server.commumityapi.controller;

import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.request.PostRequestDTO;
import eum.backed.server.common.DataResponse;
import eum.backed.server.commumityapi.controller.dto.response.UsersResponseDTO;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.service.PostService;
import eum.backed.server.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;


    @ApiOperation(value = "게시글 작성", notes = "도움요청, 받기 게시글 작성")
    @PostMapping("/create")
    public DataResponse create(@RequestBody PostRequestDTO.Create create, @AuthenticationPrincipal Users user ) throws Exception {
        return postService.create(create, user);
    }
    @ApiOperation(value = "게시글 삭제", notes = "게시글 아이디로 삭제")
    @DeleteMapping
    public DataResponse delete(@RequestParam Long postId,@AuthenticationPrincipal Users user){
        return postService.delete(postId);
    }


}

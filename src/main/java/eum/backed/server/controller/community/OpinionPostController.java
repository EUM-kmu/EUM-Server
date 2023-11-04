package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.OpinionPostRequestDTO;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.domain.community.opinionpost.OpinionPost;
import eum.backed.server.service.community.OpinionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/opinion")
@RequiredArgsConstructor
public class OpinionPostController {
    private final OpinionPostService opinionPostService;

    @PostMapping("/create")
    public DataResponse create(@RequestBody OpinionPostRequestDTO.Create create , @AuthenticationPrincipal String email){
        return opinionPostService.create(create, email);
    }
    @PutMapping
    public DataResponse update(@RequestBody OpinionPostRequestDTO.Update update,@AuthenticationPrincipal String email){
        return opinionPostService.update(update, email);
    }
    @DeleteMapping
    public DataResponse delete(@RequestParam Long opinionPostId,@AuthenticationPrincipal String email){
        return opinionPostService.delete(opinionPostId, email);
    }
    @GetMapping
    public DataResponse<List<OpinionResponseDTO.AllOpinionPostsResponses>> getAllOpinionPosts(@AuthenticationPrincipal String email){
        return opinionPostService.getAllOpinionPosts(email);
    }


}

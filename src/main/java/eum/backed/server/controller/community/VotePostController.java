package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.VotePostRequestDTO;
import eum.backed.server.service.community.VotePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/votepost")
@RequiredArgsConstructor
public class VotePostController {
    private final VotePostService votePostService;
    @PostMapping("/create")
    public DataResponse create(@RequestBody VotePostRequestDTO.Create create, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.create(create, email);
    }
    @PutMapping("/update")
    public DataResponse update(@RequestBody VotePostRequestDTO.Update update, @AuthenticationPrincipal String email) throws ParseException {
        return votePostService.update(update,email);
    }
    @DeleteMapping("/delete")
    public DataResponse delete(@RequestParam Long votePostId, @AuthenticationPrincipal String email){
        return votePostService.delete(votePostId, email);
    }

}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.service.community.ApplyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;
    @ApiOperation(value = "지원하기", notes = "도움 게시글에 지원")
    @PostMapping
    public DataResponse apply(@RequestBody ApplyRequestDTO.Apply apply, @AuthenticationPrincipal String email){
        return applyService.doApply(apply, email);
    }
    @ApiOperation(value ="지원리스트 조회", notes = "지원리스트 조회, 현재는 게시글 통합해서 나옴 수정예정")
    @GetMapping
    public DataResponse<List<ApplyResponseDTO.ApplyListResponse>> getApplyList(@RequestParam Long postId,@AuthenticationPrincipal String email){
        return applyService.getApplyList(postId,email);
    }
    @ApiOperation(value = "선정하기", notes = "게시글 선정")
    @GetMapping("/accept")
    public DataResponse accept(@RequestParam Long applyId,@AuthenticationPrincipal String email){
        return applyService.accept(applyId,email);
    }

}

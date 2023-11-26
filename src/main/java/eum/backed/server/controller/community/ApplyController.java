package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.service.community.ApplyService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
@Api(tags = "apply")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping
    @ApiOperation(value = "지원하기", notes = "도움 게시글에 지원")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer your-token-here")
    })
    public ResponseEntity<APIResponse> apply(@RequestBody ApplyRequestDTO.Apply apply, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(applyService.doApply(apply, email));
    }
    @GetMapping
    @ApiOperation(value ="지원리스트 조회", notes = "게시글 당 지원리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer your-token-here")
    })
    public ResponseEntity<APIResponse<List<ApplyResponseDTO.ApplyListResponse>>> getApplyList(@ApiParam(value = "게시글 ID", required = true) @RequestParam Long postId){
        return ResponseEntity.ok(applyService.getApplyList(postId));
    }
    @ApiOperation(value = "선정하기", notes = "해당 신청자 선정")
    @PostMapping("/{applyIds}")
    public ResponseEntity<APIResponse> accept(@PathVariable List<Long> applyIds,@AuthenticationPrincipal String email){
        return ResponseEntity.ok(applyService.accept(applyIds,email));
    }

}

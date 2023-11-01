package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ApplyRequestDTO;
import eum.backed.server.controller.community.dto.response.ApplyResponseDTO;
import eum.backed.server.service.community.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    @PostMapping
    public DataResponse apply(@RequestBody ApplyRequestDTO.Apply apply, @AuthenticationPrincipal String email){
        return applyService.doApply(apply, email);
    }
    @GetMapping
    public DataResponse<List<ApplyResponseDTO.ApplyListResponse>> getApplyList(@AuthenticationPrincipal String email){
        return applyService.getApplyList(email);
    }
    @GetMapping("/accept")
    public DataResponse accept(@RequestParam Long applyId){
        return applyService.accept(applyId);
    }

}

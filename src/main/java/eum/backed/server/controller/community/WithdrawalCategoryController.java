package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.response.InitialResponseDTO;
import eum.backed.server.service.community.WithdrawalCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "user")
@RequestMapping("/withdrawal")
@RequiredArgsConstructor
public class WithdrawalCategoryController {
    private final WithdrawalCategoryService withdrawalCategoryService;
    @GetMapping("/category")
    @ApiOperation(value = "탈퇴 카테고리 조회")
    public ResponseEntity<APIResponse<List<InitialResponseDTO.WithdrawalCategoryResponse>>> getCategories(){
        return ResponseEntity.ok(withdrawalCategoryService.getCategories());
    }
}

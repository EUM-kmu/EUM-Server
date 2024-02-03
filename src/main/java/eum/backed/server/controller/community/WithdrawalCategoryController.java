package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.response.InitialResponseDTO;
import eum.backed.server.service.community.WithdrawalCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/withdrawal")
@RequiredArgsConstructor
public class WithdrawalCategoryController {
    private final WithdrawalCategoryService withdrawalCategoryService;
    @GetMapping("/category")
    public ResponseEntity<APIResponse<List<InitialResponseDTO.WithdrawalCategoryResponse>>> getCategories(){
        return ResponseEntity.ok(withdrawalCategoryService.getCategories());
    }
}

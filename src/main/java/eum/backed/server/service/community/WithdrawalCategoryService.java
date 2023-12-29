package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.response.InitialResponseDTO;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WithdrawalCategoryService {
    private final WithdrawalCategoryRepository withdrawalCategoryRepository;
    public APIResponse<List<InitialResponseDTO.WithdrawalCategoryResponse>> getCategories(){
        List<WithdrawalCategory> withdrawalCategories = withdrawalCategoryRepository.findAll();
        List<InitialResponseDTO.WithdrawalCategoryResponse> withdrawalCategoryResponses = withdrawalCategories.stream().map(InitialResponseDTO.WithdrawalCategoryResponse::new).collect(Collectors.toList());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, withdrawalCategoryResponses);

    }
}

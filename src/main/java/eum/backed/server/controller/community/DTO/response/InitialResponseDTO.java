package eum.backed.server.controller.community.DTO.response;

import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import lombok.Getter;
import lombok.Setter;

public class InitialResponseDTO {

    @Getter
    @Setter
    public static class WithdrawalCategoryResponse{
        private Long categoryId;
        private String content;

        public WithdrawalCategoryResponse(WithdrawalCategory withdrawalCategory) {
            this.categoryId = withdrawalCategory.getWithdrawalCategoryId();
            this.content = withdrawalCategory.getContent();
        }
    }



}

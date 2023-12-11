package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.withdrawalcategory.WithdrawalCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class InitialResponseDTO {
    @Getter
    @Setter
    public static class Region{
        private Long regionId;
        private String name;
        private Long parentId;
        private RegionType regionType;

        public Region(Regions regions) {
            Long parentId = (regions.getParent() == null ) ? null : regions.getParent().getRegionId();
            this.regionId = regions.getRegionId();
            this.name = regions.getName();
            this.parentId = parentId;
            this.regionType = regions.getRegionType();
        }
    }
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

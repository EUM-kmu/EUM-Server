package eum.backed.server.controller.community.dto.response;

import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.domain.community.region.Regions;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class RegionResponseDTO {
    @Getter
    @Setter
    public static class Region{
        private Long regionId;
        private String name;
        private Long parentId;
        private RegionType regionType;

        public Region(Regions regions) {
            Long parentId = (regions.getParent() == null ) ? -1L : regions.getParent().getRegionId();
            this.regionId = regions.getRegionId();
            this.name = regions.getName();
            this.parentId = parentId;
            this.regionType = regions.getRegionType();
        }
    }


}

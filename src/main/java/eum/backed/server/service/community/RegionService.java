package eum.backed.server.service.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.domain.community.region.Regions;
import eum.backed.server.domain.community.region.RegionsRepository;
import eum.backed.server.domain.community.region.RegionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionsRepository regionsRepository;

    public APIResponse<RegionResponseDTO.Region> getRegionByType(RegionType regionType) {
        if(!(regionType == null)){
            List<Regions> regions = regionsRepository.findByRegionType(regionType).orElse(Collections.emptyList());
            return getRegions(regions);
        }
        List<Regions> regions = regionsRepository.findAll();
        return getRegions(regions);
    }

    public APIResponse<RegionResponseDTO.Region> getRegionByParent(Long regionId) {
        Regions parentRegion = regionsRepository.findById(regionId).orElseThrow(() -> new IllegalArgumentException("Invalid regionId"));
        List<Regions> regions = regionsRepository.findByParent(parentRegion).orElse(Collections.emptyList());
        return getRegions(regions);

    }


    private APIResponse<RegionResponseDTO.Region> getRegions(List<Regions> regions){
        List<Map<Long, String>> nameList = regions.stream()
                .map(region -> {
                    Map < Long, String > map = new HashMap<>();
                    map.put(region.getRegionId(), region.getName());
                    return map;}
                ).toList();
        RegionResponseDTO.Region region = RegionResponseDTO.Region.builder().region(nameList).build();
        return APIResponse.of(SuccessCode.SELECT_SUCCESS,region);
    }

}

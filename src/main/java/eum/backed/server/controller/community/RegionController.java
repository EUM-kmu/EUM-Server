package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.enums.RegionType;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.service.community.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
@Api(tags = "region")
public class RegionController {
    private final RegionService regionService;
    @ApiOperation(value = "지역 정보 조회")
    @GetMapping("")
    public DataResponse<RegionResponseDTO.Region> getRegionByType(@RequestParam(name = "si",required = false) String si,@RequestParam(name = "gu",required = false) String gu){
        return regionService.getRegionByType(si, gu);
    }
}

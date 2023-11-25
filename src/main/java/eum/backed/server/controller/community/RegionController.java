package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.service.community.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
@Api(tags = "region")
public class RegionController {
    private final RegionService regionService;
    @ApiOperation(value = "지역 정보 조회")
    @GetMapping("")
    public ResponseEntity<APIResponse<RegionResponseDTO.Region>> getRegionByType(@RequestParam(name = "si",required = false) String si, @RequestParam(name = "gu",required = false) String gu){
        return ResponseEntity.ok(regionService.getRegionByType(si, gu));
    }
}

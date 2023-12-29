package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.response.InitialResponseDTO;
import eum.backed.server.domain.community.region.RegionType;
import eum.backed.server.service.community.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
@Api(tags = "region")
@CrossOrigin("*")
public class RegionController {
    private final RegionService regionService;
    @ApiOperation(value = "지역 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("")
    public ResponseEntity<APIResponse<List<InitialResponseDTO.Region>>> getRegionByType(@RequestParam(name = "type",required = false) RegionType regionType){
        return ResponseEntity.ok(regionService.getRegionByType(regionType));
    }
    @ApiOperation(value = "지역 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/{regionId}/subregions")
    public ResponseEntity<APIResponse<List<InitialResponseDTO.Region>>> getRegionByParent(@PathVariable Long regionId){
        return ResponseEntity.ok(regionService.getRegionByParent(regionId));
    }
}

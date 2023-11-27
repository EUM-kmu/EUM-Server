package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.response.RegionResponseDTO;
import eum.backed.server.service.community.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("")
    public ResponseEntity<APIResponse<RegionResponseDTO.Region>> getRegionByType(@RequestParam(name = "si",required = false) Long siId, @RequestParam(name = "gu",required = false) Long guId){
        return ResponseEntity.ok(regionService.getRegionByType(siId, guId));
    }
}

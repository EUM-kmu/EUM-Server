package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.ErrorResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.service.community.AvatarService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avatars")
@RequiredArgsConstructor
@Slf4j
public class AvatarController {
    private final AvatarService avatarService;
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<List<ProfileResponseDTO.AvatarInfo>>> getAvatars(){
        List<ProfileResponseDTO.AvatarInfo> avatarInfos = avatarService.getAvatars();
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, avatarInfos));
    }

}

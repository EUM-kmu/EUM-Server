package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.ErrorResponse;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.service.community.ProfileService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "ProfileController", description = "프로필 관련 api")
@CrossOrigin("*")
public class   ProfileController {
    private final ProfileService profileService;

    /**
     * 프로필 작성
     * @param createProfile : 작성할 프로필 정보
     * @param userDetails : jwt에 담긴 유저 객체
     * @return : 성공 여부
     */
    @PostMapping("/profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> createProfile(@RequestBody @Validated ProfileRequestDTO.CreateProfile createProfile, @AuthenticationPrincipal CustomUserDetails userDetails){
        return new ResponseEntity<>(profileService.create(createProfile, Long.valueOf(userDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 프로필 조회
     * @return
     */
    @GetMapping("/profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(profileService.getMyProfile(userDetails.getCustomUserInfo().getEmail()));
    }

    @PutMapping("/profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> updateMyProfile(@RequestBody @Validated ProfileRequestDTO.UpdateProfile updateProfile, @AuthenticationPrincipal CustomUserDetails customUserDetails){

        return ResponseEntity.ok(profileService.updateMyProfile(updateProfile, Long.valueOf(customUserDetails.getUsername())));
    }


}

package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.Response;
import eum.backed.server.controller.community.DTO.request.ProfileRequestDTO;
import eum.backed.server.controller.community.DTO.response.ProfileResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.service.community.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Api(tags = "profile")
@CrossOrigin("*")
public class   ProfileController {
    private final ProfileService profileService;

    /**
     * 프로필 작성
     * @param createProfile : 작성할 프로필 정보
     * @param userDetails : jwt에 담긴 유저 객체
     * @return : 성공 여부
     */
    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @ApiOperation(value = "프로필 생성", notes = "프로필 생성, ROLE_TEMPORARY_USER -> ROLE_USER 전환")
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> createProfile(@RequestBody @Validated ProfileRequestDTO.CreateProfile createProfile, @AuthenticationPrincipal CustomUserDetails userDetails){
        return new ResponseEntity<>(profileService.create(createProfile, Long.valueOf(userDetails.getUsername())), HttpStatus.CREATED);
    }

    /**
     * 프로필 조회
     * @return
     */
    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @ApiOperation(value = "내 프로필 조회")
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(profileService.getMyProfile(userDetails.getCustomUserInfo().getEmail()));
    }

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @ApiOperation(value = "내 프로필 조회")
    public ResponseEntity<Response<ProfileResponseDTO.ProfileResponse>> getpro(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(profileService.getMyProfile(userDetails.getCustomUserInfo().getEmail()));
    }
    @PutMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @ApiOperation(value = "프로필 수정")
    public ResponseEntity<APIResponse> updateMyProfile(@RequestBody @Validated ProfileRequestDTO.UpdateProfile updateProfile, @AuthenticationPrincipal CustomUserDetails customUserDetails){

        return ResponseEntity.ok(profileService.updateMyProfile(updateProfile, Long.valueOf(customUserDetails.getUsername())));
    }


}

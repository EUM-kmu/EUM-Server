package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.controller.community.dto.response.ProfileResponseDTO;
import eum.backed.server.service.community.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("profile")
@RequiredArgsConstructor
@Api(tags = "profile")
public class ProfileController {
    private final ProfileService profileService;


    @PostMapping("")
    @ApiOperation(value = "프로필 생성", notes = "프로필 생성, ROLE_TEMPORARY_USER -> ROLE_USER 전환")
    public ResponseEntity<APIResponse> createProfile(@Valid @RequestBody ProfileRequestDTO.CreateProfile createProfile, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(profileService.create(createProfile, email));
    }
    @GetMapping("")
    @ApiOperation(value = "내 프로필 조회")
    public ResponseEntity<APIResponse<ProfileResponseDTO.AllProfile>> getMyProfile(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(profileService.getMyProfile(email));
    }
    @PutMapping("")
    @ApiOperation(value = "프로필 수정")
    public ResponseEntity<APIResponse> updateMyProfile(@RequestBody ProfileRequestDTO.UpdateProfile updateProfile, @AuthenticationPrincipal String email){
        return ResponseEntity.ok(profileService.updateMyProfile(updateProfile,email));
    }
//    @ApiOperation(value = "관심게시글 목록 조회", notes = "나의 관심 게시글 목록 최신 정렬")
//    @GetMapping("/{serviceName}")
//    public DataResponse<List<PostResponseDTO.PostResponse>> findByScrap(@PathVariable String serviceName,@AuthenticationPrincipal String email){
//        return marketPostService.findByScrap(email);
//    }


//    @GetMapping("/market-post")
//    @ApiOperation(value = "내가 작성한 거래 게시글")
//    public DataResponse<List<PostResponseDTO.PostResponse>> getMyPosts(@AuthenticationPrincipal String email){
//        return marketPostService.getMyPosts(email);
//    }


}

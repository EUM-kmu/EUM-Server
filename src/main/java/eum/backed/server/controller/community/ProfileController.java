package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.ProfileRequestDTO;
import eum.backed.server.service.community.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/profile")
@RequiredArgsConstructor
@Api(tags = "프로필")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/create")
    @ApiOperation(value = "프로필 생성", notes = "프로필 생성, ROLE_TEMPORARY_USER -> ROLE_USER 전환")
    public DataResponse createProfile(@Valid @RequestBody ProfileRequestDTO.CreateProfile createProfile, @AuthenticationPrincipal String email){
        log.info(email);
        return profileService.create(createProfile, email);

    }
}

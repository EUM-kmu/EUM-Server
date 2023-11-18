package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.response.Response;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.domain.community.profile.Profile;
import eum.backed.server.lib.Helper;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "user")
public class UsersController {
    private final UsersService usersService;
    private final Response response;
    @ApiOperation(value = "자체 회원가입", notes = "자체 회원가입")
    @PostMapping("/auth/signup")
    public DataResponse signup(@RequestBody @Validated UsersRequestDTO.SignUp signUp) {
        return usersService.signUp(signUp);
    }
    @ApiOperation(value = "자체로그인", notes = "자체 앱 로그인")
    @PostMapping("/auth/signin")
    public DataResponse<UsersResponseDTO.TokenInfo> signIn(@RequestBody @Validated UsersRequestDTO.SignIn signIn){
        return usersService.signIn(signIn);
    }
    @ApiOperation(value = "토근 갱신", notes = "토큰 갱신")
    @PostMapping("/reissue")
    public DataResponse<UsersResponseDTO.TokenInfo> reissue(@RequestBody @Validated UsersRequestDTO.Reissue reissue){
        return usersService.reissue(reissue);
    }
    @ApiOperation(value = "로그아웃", notes = "엑세스 토큰 삭제")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Validated UsersRequestDTO.Logout logout, Errors errors){
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.logout(logout);
    }
}

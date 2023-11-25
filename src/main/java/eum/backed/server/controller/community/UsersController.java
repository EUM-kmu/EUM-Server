package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.ErrorResponse;
import eum.backed.server.common.DTO.enums.ErrorCode;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.SignInType;
import eum.backed.server.controller.community.dto.response.Response;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.lib.Helper;
import eum.backed.server.service.community.KakaoService;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "user")
public class UsersController {
    private final UsersService usersService;
    private final KakaoService kakaoService;
    private final Response response;
    @ApiOperation(value = "자체 회원가입", notes = "자체 회원가입")
    @PostMapping("/auth/signup")
    public ResponseEntity<APIResponse> signup(@RequestBody @Validated UsersRequestDTO.SignUp signUp) {
        return ResponseEntity.ok(usersService.signUp(signUp));
    }
    @ApiOperation(value = "자체로그인", notes = "자체 앱 로그인")
    @PostMapping("/auth/signin")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> signIn(@RequestBody @Validated UsersRequestDTO.SignIn signIn){
        return ResponseEntity.ok(usersService.signIn(signIn));
    }
    @ApiOperation(value = "토근 갱신", notes = "토큰 갱신")
    @PostMapping("/reissue")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> reissue(@RequestBody @Validated UsersRequestDTO.Reissue reissue){
        return ResponseEntity.ok(usersService.reissue(reissue));
    }
    @ApiOperation(value = "로그아웃", notes = "엑세스 토큰 삭제")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Validated UsersRequestDTO.Logout logout, Errors errors){
        if (errors.hasErrors()) {
            ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,Helper.refineErrors(errors));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(usersService.logout(logout));
    }
    @ApiOperation(value = "소셜 타입별 로그인",notes = "kakao,google,apple")
    @PostMapping("/auth/{type}")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@PathVariable SignInType signInType ,@RequestBody @Validated UsersRequestDTO.Token token) throws IOException, FirebaseAuthException {
//        String access = kakaoService.getKakaoAccessT(code);
        String email = "";
        if(signInType == SignInType.kakao){
            email = kakaoService.createKakaoUser(token.getToken());
        } else if (signInType == SignInType.firebase) {
            email = FirebaseAuth.getInstance().verifyIdToken(token.getToken()).getEmail();
        }
//        log.info(token.);
        return ResponseEntity.ok(usersService.getToken(email));
    }
}

package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.SignInType;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.service.community.KakaoService;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping()
@RestController
@Api(tags = "user")
public class UsersController {
    private final UsersService usersService;
    private final KakaoService kakaoService;
    @ApiOperation(value = "자체 회원가입", notes = "자체 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/auth/signup")
    public ResponseEntity<APIResponse> signup(@RequestBody @Validated UsersRequestDTO.SignUp signUp) {
        return new ResponseEntity<>(usersService.signUp(signUp), HttpStatus.CREATED);
    }
    @ApiOperation(value = "자체로그인", notes = "자체 앱 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/auth/signin/local")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> signIn(@RequestBody @Validated UsersRequestDTO.SignIn signIn){
        return ResponseEntity.ok(usersService.signIn(signIn));
    }
    @ApiOperation(value = "토근 갱신", notes = "토큰 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 만료 및 토큰 형식이 올바르지 않을 때,이미 로그아웃한 유저일 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/auth/reissue")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> reissue(@RequestBody @Validated UsersRequestDTO.Reissue reissue){
        return ResponseEntity.ok(usersService.reissue(reissue));
    }
    @ApiOperation(value = "로그아웃", notes = "엑세스 토큰 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "토큰 만료 및 토큰 형식이 올바르지 않을 때,이미 로그아웃한 유저일 경우"),
            @ApiResponse(responseCode = "403", description = "헤더에 토큰이 들어가있지 않은 경우"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Extract Bearer token from Authorization header
        String bearerToken = extractBearerToken(authorizationHeader);

        // Pass the Bearer token to the logout method
        return ResponseEntity.ok(usersService.logout(bearerToken));
    }

    @ApiOperation(value = "소셜 타입별 로그인",notes = "kakao,firebase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "sdk토큰 형식이 잘못되거나 시간이 만료 되었을때"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/auth/signin/{type}")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@PathVariable SignInType type ,@RequestBody @Validated UsersRequestDTO.Token token) throws IOException, FirebaseAuthException {
//        String access = kakaoService.getKakaoAccessT(code);
        String email = "";
        if(type == SignInType.kakao){
            email = kakaoService.createKakaoUser(token.getToken());
        } else if (type == SignInType.firebase) {
            email = FirebaseAuth.getInstance().verifyIdToken(token.getToken()).getEmail();
        }
//        log.info(token.);
        return ResponseEntity.ok(usersService.getToken(email));
    }

    // Helper method to extract Bearer token from Authorization header
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}

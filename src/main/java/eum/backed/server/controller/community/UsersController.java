package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.request.enums.SignInType;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.domain.community.marketpost.MarketPost;
import eum.backed.server.domain.community.user.SocialType;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.*;
import eum.backed.server.service.community.DTO.KakaoDTO;
import eum.backed.server.service.community.bank.BankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.error.Mark;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Api(tags = "user")
@CrossOrigin("*")
public class UsersController {
    private final UsersService usersService;
    private final KakaoService kakaoService;
    private final BankAccountService bankAccountService;
    private final ChatService chatService;
    private final BlockService blockService;
    private final MarketPostService marketPostService;
    private final ApplyService applyService;
    @ApiOperation(value = "토큰 검증", notes = "유저 타입 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/token")
    public ResponseEntity<APIResponse<UsersResponseDTO.UserRole>>validateToken(@AuthenticationPrincipal String email) {
        return new ResponseEntity<>(usersService.validateToken(email), HttpStatus.OK);
    }
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
    @PostMapping("/logOut")
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
        String uid = "";
        SocialType socialType = null;
        if(type == SignInType.kakao){
            KakaoDTO.KaKaoInfo kaKaoInfo= kakaoService.createKakaoUser(token.getToken());
            email = kaKaoInfo.getEmail();
            uid = kaKaoInfo.getUid();
            socialType = SocialType.KAKAO;
        } else if (type == SignInType.firebase) {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token.getToken());
            email = firebaseToken.getEmail();
            uid = firebaseToken.getUid();
            socialType = SocialType.FIREBASE;
        }
//        log.info(token.);
        return ResponseEntity.ok(usersService.getToken(email,uid,socialType));
    }

    // Helper method to extract Bearer token from Authorization header
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    @PostMapping("/withdrawal")
    @ApiOperation(value = "탈퇴하기")
    public ResponseEntity<APIResponse> withdrawal(@RequestBody UsersRequestDTO.Withdrawal withdrawal,@AuthenticationPrincipal String email) throws FirebaseAuthException {
        Users getUser = usersService.findByEmail(email);
//        계좌동결
        bankAccountService.freezeAccount(getUser);
        chatService.blockedChat(getUser);
        applyService.withdrawalApply(getUser);
//        탈퇴 사유 등록
        if(getUser.getSocialType() == SocialType.KAKAO){
            kakaoService.WithdralKakao(getUser.getUid());
        } else if (getUser.getSocialType() == SocialType.FIREBASE) {
            FirebaseAuth.getInstance().deleteUser(getUser.getUid());
        }
        usersService.withdrawal(withdrawal,getUser);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS, "탈퇴성공"));
    }
   @PostMapping("/block")
   @ApiOperation(value = "차단하기",notes = "동일한 메소드 한번더 보낼땐 차단해제, 클라이언트에는 현재 없는 기능이지만 테스트용으로 존재")
    public ResponseEntity<APIResponse> blockedAction(@RequestBody UsersRequestDTO.BlockedAction blockedAction, @AuthenticationPrincipal String email){
       Users blocker = usersService.findByEmail(email);
       Users blocked = usersService.findById(blockedAction.getUserId());

       Boolean isBlocked = blockService.blockedAction(blocker, blocked);
       chatService.blockedAction(isBlocked,blocker, blocked);
       applyService.blockedAction( blocker, blocked);

       String msg = isBlocked ? "차단 성공" : "차단 해제";
       return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS,msg),HttpStatus.CREATED);


   }

}

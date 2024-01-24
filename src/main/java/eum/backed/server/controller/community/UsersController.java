package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.common.DTO.enums.SuccessCode;
import eum.backed.server.controller.community.DTO.request.UsersRequestDTO;
import eum.backed.server.controller.community.DTO.request.enums.SignInType;
import eum.backed.server.controller.community.DTO.response.UsersResponseDTO;
import eum.backed.server.domain.auth.CustomUserDetails;
import eum.backed.server.domain.community.user.SocialType;
import eum.backed.server.domain.community.user.Users;
import eum.backed.server.service.community.*;
import eum.backed.server.service.community.DTO.KakaoDTO;
import eum.backed.server.service.bank.BankAccountService;
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
    private final ApplyService applyService;


    /**
     *
     * @param customUserDetails : jwt 토큰에 담겨있는 customUserDetails
     * @return :
     * 기능 :
     *  프로필 작성 중단하고 서비스를 재사용할때 상태 판별을 위한 토큰 조회 controller
     *  ROLE_UNPROFILE_USER : 새유저, 소셜로그인만 한 상태
     *  ROLE_UNPASSWORD_USER : 프로필만 만들고 계좌 생성이 안된 상태
     *  ROLE_USER : 최종 활동이 가능한 상태
     */

    @ApiOperation(value = "토큰 검증", notes = "유저 타입 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @GetMapping("/token")
    public ResponseEntity<APIResponse<UsersResponseDTO.UserRole>>validateToken(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new ResponseEntity<>(usersService.validateToken(Long.valueOf(customUserDetails.getUsername())), HttpStatus.OK);
    }

    /**
     *
     * @param signUp : email, password
     * @return
     */
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

    /**
     *
     * @param signIn  : email, password
     * @return : jwt 토큰
     */
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

    /**
     *
     * @param authorizationHeader : 헤더에 들어온 토큰
     * @return
     * 기능 : redis에서 토큰 정보 삭제
     */
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
        usersService.logout(bearerToken);

        // Pass the Bearer token to the logout method
        return ResponseEntity.ok( APIResponse.of(SuccessCode.UPDATE_SUCCESS,"로그아웃 되었습니다."));
    }

    /**
     *
     * @param type : kakao,firebase
     * @param token : 프론트에서 넘어오는 소셜로그인 토큰
     * @return : jwt 토큰
     * @throws IOException
     * @throws FirebaseAuthException
     */
    @ApiOperation(value = "소셜 타입별 로그인",notes = "kakao,firebase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,"),
            @ApiResponse(responseCode = "401", description = "sdk토큰 형식이 잘못되거나 시간이 만료 되었을때"),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,"),
    })
    @PostMapping("/auth/signin/{type}")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@PathVariable SignInType type ,@RequestBody @Validated UsersRequestDTO.Token token) throws IOException, FirebaseAuthException {
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

    // 헤더에서 토큰 추출 위한 코드
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     *
     * @param authorizationHeader : redis에 유저 정보(customUserDetails)을 삭제하기 위해 받는 헤더 정보
     * @param withdrawal : 탈퇴 사유를 위한 body
     * @param customUserDetails : jwt 토큰에 담겨있는 customUserDetails
     * @return
     * @throws FirebaseAuthException
     *
     * 로직 : 계좌 동결, 탈퇴 유저와의 채팅 block 처리, 유저정보 빈값넣기, 지원 취소 시키기
     */
    @PostMapping("/withdrawal")
    @ApiOperation(value = "탈퇴하기")
    public ResponseEntity<APIResponse> withdrawal(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UsersRequestDTO.Withdrawal withdrawal,@AuthenticationPrincipal CustomUserDetails customUserDetails) throws FirebaseAuthException {
        Users getUser = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
        bankAccountService.freezeAccount(getUser); //계좌 동결
        chatService.blockedChatInWithdrawal(getUser); //탈퇴 유저와의 채팅 block
        applyService.withdrawalApply(getUser); //탈퇴 유저 지원취소 처리

//        소셜 로그인 타입별 정보 제거
        if(getUser.getSocialType() == SocialType.KAKAO){
            kakaoService.WithdralKakao(getUser.getUid()); //카카오와 연결 끊기
        } else if (getUser.getSocialType() == SocialType.FIREBASE) {
            FirebaseAuth.getInstance().deleteUser(getUser.getUid()); //파이어베이스에 저장된 유저정보 제거
        }
        usersService.withdrawal(withdrawal,getUser); //탈퇴 사유 저장
        String bearerToken = extractBearerToken(authorizationHeader);
        usersService.logout(bearerToken);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS, "탈퇴성공"));
    }

    /**
     * 차단하기 , 차단해제 같은 controller에 담은 -> 프론트 요청으로 다른 컨트롤러로 분리해야함
     * @param blockedAction : 차단할 유저 id
     * @param customUserDetails : jwt에 담긴 customUserDetails
     * @return
     */
   @PostMapping("/block")
   @ApiOperation(value = "차단하기",notes = "동일한 메소드 한번더 보낼땐 차단해제, 클라이언트에는 현재 없는 기능이지만 테스트용으로 존재")
    public ResponseEntity<APIResponse> blockedAction(@RequestBody UsersRequestDTO.BlockedAction blockedAction, @AuthenticationPrincipal CustomUserDetails customUserDetails){
       Users blocker = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
       Users blocked = usersService.findById(blockedAction.getUserId()); //차단할 유저 객체

       Boolean isBlocked = blockService.blockedAction(blocker, blocked); //차단•해제에 대한 판별
       chatService.blockedAction(isBlocked,blocker, blocked); // 차단한/된 유저와의 채팅 block
       applyService.blockedAction( blocker, blocked); // 지원취소

       String msg = isBlocked ? "차단 성공" : "차단 해제";
       return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS,msg),HttpStatus.CREATED);


   }

}

package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuthException;
import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.request.UsersRequestDTO;
import eum.backed.server.controller.community.DTO.response.UsersResponseDTO;
import eum.backed.server.domain.auth.user.SocialType;
import eum.backed.server.service.auth.FirebaseAuthService;
import eum.backed.server.service.community.DTO.KakaoDTO;
import eum.backed.server.service.community.KakaoService;
import eum.backed.server.service.community.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class KakaoController {
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private UsersService usersService;
    private final FirebaseAuthService firebaseAuthService;

    //    테스트용 카카오 로그인 사용 x
    @GetMapping("/api/v1/auth/kakao")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@RequestParam String code) throws IOException, FirebaseAuthException {
        String access = kakaoService.getKakaoAccessT(code);
        KakaoDTO.KaKaoInfo kaKaoInfo = kakaoService.createKakaoUser(access);
//        log.info(access);
//        return ResponseEntity.ok(usersService.getToken(kaKaoInfo.getEmail(), kaKaoInfo.getUid(), SocialType.KAKAO));
        SocialType socialType = SocialType.KAKAO;
        String email = kaKaoInfo.getEmail();
        String uid = kaKaoInfo.getUid();
        log.info(firebaseAuthService.checkAuth(email).toString());
        if (firebaseAuthService.checkAuth(email)) {
            return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
        } else {
            firebaseAuthService.createAccountInFirebase(email,uid);
            return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
        }
    }

    @PostMapping("/kakao")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@RequestBody @Validated UsersRequestDTO.Token token) throws IOException, FirebaseAuthException
        {
            SocialType socialType = SocialType.KAKAO;
            KakaoDTO.KaKaoInfo kaKaoInfo = kakaoService.createKakaoUser(token.getToken());
            String email = kaKaoInfo.getEmail();
            String uid = kaKaoInfo.getUid();
            if (!firebaseAuthService.checkAuth(uid)) {
               return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
            } else {
                firebaseAuthService.createAccountInFirebase(email,uid);
                return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
            }
//        log.info(token.);
        }

    @PostMapping("/api/v1/auth/fire")
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> firebaseauth() throws IOException, FirebaseAuthException{

        String email = "test@email.com";
        String uid = "skjfalskdjflka;sdf";
        if (!firebaseAuthService.checkAuth(email)) {
            firebaseAuthService.createAccountInFirebase(email,uid);
        }
        return ResponseEntity.ok(usersService.getToken(email, uid, SocialType.KAKAO));


//        log.info(token.);
    }


}
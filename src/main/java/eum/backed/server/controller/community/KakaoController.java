package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.DTO.response.UsersResponseDTO;
import eum.backed.server.domain.community.user.SocialType;
import eum.backed.server.service.community.DTO.KakaoDTO;
import eum.backed.server.service.community.KakaoService;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "user")
@CrossOrigin("*")
@ApiIgnore
public class KakaoController {
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private UsersService usersService;
    @GetMapping("/api/v1/auth/kakao")

    @ApiOperation(value = "카카오 로그인", notes = "카카오 restapi 버전, 리다이렉트 uri 수정 귀찮아서 엔드포인트 통일 아직 안해놓음")
//    테스트용 카카오 로그인 사용 x
    public ResponseEntity<APIResponse<UsersResponseDTO.TokenInfo>> getToken(@RequestParam String code) throws IOException {
        String access = kakaoService.getKakaoAccessT(code);
        KakaoDTO.KaKaoInfo kaKaoInfo = kakaoService.createKakaoUser(access);
        log.info(access);
        return ResponseEntity.ok(usersService.getToken(kaKaoInfo.getEmail(), kaKaoInfo.getUid(), SocialType.KAKAO));
    }
}
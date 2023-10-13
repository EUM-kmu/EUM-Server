package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.service.community.KakaoService;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "user")
public class KakaoController {
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private UsersService usersService;
    @GetMapping("/user/auth/kakao")

    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인")
    public DataResponse<UsersResponseDTO.TokenInfo> getToken(@RequestParam String code) throws IOException {
        String access = kakaoService.getKakaoAccessT(code);
        String email = kakaoService.createKakaoUser(access);
        log.info(access);
        return usersService.getToken(email);
    }
}

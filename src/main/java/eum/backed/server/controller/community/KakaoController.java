//package eum.backed.server.controller.community;
//
//import eum.backed.server.common.DTO.APIResponse;
//import eum.backed.server.common.DTO.DataResponse;
//import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
//import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
//import eum.backed.server.service.community.KakaoService;
//import eum.backed.server.service.community.UsersService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@Api(tags = "user")
//public class KakaoController {
//    @Autowired
//    private KakaoService kakaoService;
//    @Autowired
//    private UsersService usersService;
//    @GetMapping("/user/auth/kakao")
//
//    @ApiOperation(value = "카카오 로그인", notes = "카카오 로그인")
//    public APIResponse<UsersResponseDTO.TokenInfo> getToken(@RequestBody @Validated UsersRequestDTO.Token token) throws IOException {
////        String access = kakaoService.getKakaoAccessT(code);
//        String email = kakaoService.createKakaoUser(token.getToken());
////        log.info(token.);
//        return usersService.getToken(email);
//    }
//}

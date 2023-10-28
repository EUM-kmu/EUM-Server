package eum.backed.server.controller.community;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.request.UsersRequestDTO;
import eum.backed.server.controller.community.dto.response.UsersResponseDTO;
import eum.backed.server.service.community.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "user")
public class GoogleController {
    @Autowired
    private UsersService usersService;

    @ApiResponse(code = 200,message = "ok",response = UsersResponseDTO.TokenInfo.class)
    @ApiOperation(value = "구글 로그인", notes = "구글 로그인")
    @PostMapping("/auth/google")
    public DataResponse<UsersResponseDTO.TokenInfo> signIn(@RequestBody UsersRequestDTO.AuthSignin authSignin) throws FirebaseAuthException {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authSignin.getIdtoken());
            return usersService.getToken( decodedToken.getEmail());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }
}

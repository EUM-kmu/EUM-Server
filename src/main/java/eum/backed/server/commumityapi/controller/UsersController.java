package eum.backed.server.commumityapi.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.request.UsersRequestDTO;
import eum.backed.server.commumityapi.controller.dto.response.UsersResponseDTO;
import eum.backed.server.commumityapi.service.CustomUsersDetailsService;
import eum.backed.server.commumityapi.service.UsersService;
import eum.backed.server.lib.Helper;
import eum.backed.server.util.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CustomUsersDetailsService customUsersDetailsService;
    FirebaseAuth firebaseAuth;
//    @ApiOperation(value = "signup", notes = "자체 회원가입")
//    @ApiResponses(value={
//            @ApiResponse(responseCode = "200", description = "가게 상세정보 조회 성공"),
//
//    })
//    @ApiOperation(summary = "자체 회원 가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated UsersRequestDTO.SignUp signUp, Errors errors) {
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signUp(signUp);
    }
    @PostMapping("/auth")
    public ResponseEntity<?> register(@RequestBody UsersRequestDTO.Test test) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(test.getIdtoken());
            return usersService.register(test, decodedToken);
        } catch ( FirebaseAuthException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/authsignin")
    public ResponseEntity<?>authsignin(@RequestBody UsersRequestDTO.AuthSignin authSignin) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authSignin.getIdtoken());
            return usersService.authsignin( decodedToken);
        } catch ( FirebaseAuthException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @ApiResponse(code = 200,message = "ok",response = UsersResponseDTO.TokenInfo.class)
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Validated UsersRequestDTO.SignIn signIn, Errors errors){
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signIn(signIn);
    }
    @ApiResponse(code = 200,message = "ok",  response = UsersResponseDTO.TokenInfo.class)
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody @Validated UsersRequestDTO.Reissue reissue, Errors errors){
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.reissue(reissue);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Validated UsersRequestDTO.Logout logout, Errors errors){
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.logout(logout);
    }
}

package eum.backed.server.commumityapi.controller;

import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.request.UsersRequestDTO;
import eum.backed.server.commumityapi.controller.dto.response.UsersResponseDTO;
import eum.backed.server.commumityapi.service.UsersService;
import eum.backed.server.lib.Helper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Api(tags = "user")
public class UsersController {
    private final UsersService usersService;
    private final Response response;
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

package eum.backed.server.commumityapi.controller;

import eum.backed.server.commumityapi.controller.dto.Response;
import eum.backed.server.commumityapi.controller.dto.request.UsersRequestDTO;
import eum.backed.server.commumityapi.service.UsersService;
import eum.backed.server.lib.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UsersController {
    private final UsersService usersService;
    private final Response response;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated UsersRequestDTO.SignUp signUp, Errors errors) {
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signUp(signUp);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Validated UsersRequestDTO.SignIn signIn, Errors errors){
        if(errors.hasErrors()){
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signIn(signIn);
    }
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

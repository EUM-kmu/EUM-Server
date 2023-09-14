package eum.backed.server.controller;

import eum.backed.server.controller.dto.Response;
import eum.backed.server.controller.dto.request.UsersRequestDTO;
import eum.backed.server.service.UsersService;
import eum.backed.server.lib.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

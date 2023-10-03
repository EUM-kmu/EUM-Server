package eum.backed.server.commumityapi.controller;

import eum.backed.server.common.DataResponse;
import eum.backed.server.commumityapi.controller.dto.request.CommentRequestDTO;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    DataResponse create(@RequestBody @Validated CommentRequestDTO.Create create, @AuthenticationPrincipal Users user){
        return commentService.create(create, user);
    }
}

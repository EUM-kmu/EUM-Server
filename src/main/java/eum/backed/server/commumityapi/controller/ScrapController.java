package eum.backed.server.commumityapi.controller;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.commumityapi.domain.user.Users;
import eum.backed.server.commumityapi.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    @GetMapping("/do")
    public DataResponse doScrap(@RequestParam Long postId, @AuthenticationPrincipal Users user) {
        return scrapService.doScrap(postId, user);
    }
    @GetMapping("/undo")
    public DataResponse undoScrap(@RequestParam Long postId,@AuthenticationPrincipal Users user) throws IllegalAccessException {
        return scrapService.undoScrap(postId,user);
    }
}

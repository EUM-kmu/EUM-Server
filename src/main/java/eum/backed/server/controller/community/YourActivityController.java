package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.APIResponse;
import eum.backed.server.controller.community.dto.request.enums.ServiceType;
import eum.backed.server.controller.community.dto.response.OpinionResponseDTO;
import eum.backed.server.controller.community.dto.response.PostResponseDTO;
import eum.backed.server.controller.community.dto.response.VotePostResponseDTO;
import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.OpinionPostService;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/api/v1/your-activity")
@RequiredArgsConstructor
@Api(tags = "your activity")
@CrossOrigin("*")
public class YourActivityController {
    private final MarketPostService marketPostService;
    private final OpinionPostService opinionPostService;
    private final VotePostService votePostService;


}

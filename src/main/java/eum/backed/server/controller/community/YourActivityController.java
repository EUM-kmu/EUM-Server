package eum.backed.server.controller.community;

import eum.backed.server.service.community.MarketPostService;
import eum.backed.server.service.community.OpinionPostService;
import eum.backed.server.service.community.VotePostService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

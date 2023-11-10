package eum.backed.server.controller.community;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.community.dto.response.ChatRoomResponseDTO;
import eum.backed.server.service.community.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
//
//    @GetMapping
//    public DataResponse createChat(@RequestParam Long applyId){
//        return chatService.createChatRoom(applyId);
//    }

    @GetMapping("/getChatListInMypost")
    public DataResponse<List<ChatRoomResponseDTO>> getChatListInMyPost(@AuthenticationPrincipal String email){
        return chatService.getChatListInMyPost(email);
    }
    @GetMapping("/getChatListInOtherPost")
    public DataResponse<List<ChatRoomResponseDTO>> getCheckListInOtherPost(@AuthenticationPrincipal String email){
        return chatService.getChatListInOtherPost(email);
    }
}

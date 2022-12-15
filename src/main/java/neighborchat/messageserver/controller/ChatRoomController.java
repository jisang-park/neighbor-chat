package neighborchat.messageserver.controller;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import neighborchat.messageserver.domain.dto.MessageResponseDto;
import neighborchat.messageserver.service.ChatRoomService;
import neighborchat.messageserver.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final MessageService messageService;
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public Map<String, String> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        String roomId = chatRoomService.createChatRoom(ChatRoomRequestDto.convert(chatRoomRequestDto));
        return Map.of("roomId", roomId);
    }

    @GetMapping("/chat-room/{roomId}")
    public List<MessageResponseDto> readMessages(@PathVariable String roomId, @RequestParam(required = false, defaultValue = "0") int page) {
        return messageService.readMessages(roomId, page)
                .stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }

}

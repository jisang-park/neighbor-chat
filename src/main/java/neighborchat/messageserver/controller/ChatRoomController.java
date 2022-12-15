package neighborchat.messageserver.controller;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import neighborchat.messageserver.domain.dto.ChatRoomResponseDto;
import neighborchat.messageserver.domain.dto.MessageResponseDto;
import neighborchat.messageserver.service.ChatRoomService;
import neighborchat.messageserver.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public List<MessageResponseDto> readMessages(@PathVariable String roomId) {
        return messageService.readMessages(roomId, 0)
                .stream()
                .map(MessageResponseDto::new)
                .toList();
    }

    @GetMapping(value = "/chat-room/{roomId}", params = "page")
    public List<MessageResponseDto> readMessages(@PathVariable String roomId, @RequestParam int page) {
        return messageService.readMessages(roomId, page)
                .stream()
                .map(MessageResponseDto::new)
                .toList();
    }

    @GetMapping("/chat-room/search")
    public List<ChatRoomResponseDto> searchChatRooms(@RequestParam Double longitude, @RequestParam Double latitude,
                                                     @RequestParam Double distance) {
        return chatRoomService.searchChatRooms(longitude, latitude, distance)
                .stream()
                .map(ChatRoomResponseDto::new)
                .toList();
    }

    @GetMapping(value = "/chat-room/search", params = "subject")
    public List<ChatRoomResponseDto> searchChatRooms(@RequestParam Double longitude, @RequestParam Double latitude,
                                                     @RequestParam Double distance, @RequestParam String subject) {
        return chatRoomService.searchChatRooms(subject, longitude, latitude, distance)
                .stream()
                .map(ChatRoomResponseDto::new)
                .toList();
    }

}

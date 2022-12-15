package neighborchat.messageserver.controller;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.dto.MessageRequestDto;
import neighborchat.messageserver.service.MessageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message/{roomId}")
    public Map<String, String> sendMessage(@RequestBody MessageRequestDto messageRequestDto, @PathVariable String roomId) {
        return Map.of("messageId", messageService.sendMessage(MessageRequestDto.convert(messageRequestDto, roomId)));
    }

}

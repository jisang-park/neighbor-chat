package neighborchat.messageserver.service;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.dto.MessageResponseDto;
import neighborchat.messageserver.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public String sendMessage(Message message) {
        return messageRepository.save(message).getId();
    }

    public List<MessageResponseDto> readMessages(String roomId, int page) {
        return messageRepository.findAllByRoomId(roomId, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "time")))
                .stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }

}

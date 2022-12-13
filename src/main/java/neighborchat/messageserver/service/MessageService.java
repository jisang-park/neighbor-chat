package neighborchat.messageserver.service;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public String sendMessage(Message message) {
        return messageRepository.save(message).getId();
    }

}

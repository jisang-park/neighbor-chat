package neighborchat.messageserver.domain.dto;

import lombok.Getter;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private String userId;
    private String content;
    private MessageType type;
    private LocalDateTime time;

    public MessageResponseDto(Message message) {
        this.userId = message.getUserId();
        this.content = message.getContent();
        this.type = message.getType();
        this.time = message.getTime();
    }

}

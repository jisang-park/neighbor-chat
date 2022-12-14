package neighborchat.messageserver.domain.dto;

import lombok.Getter;
import neighborchat.messageserver.domain.Message;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private String userId;
    private String content;
    private LocalDateTime time;

    public MessageResponseDto(Message message) {
        this.userId = message.getUserId();
        this.content = message.getContent();
        this.time = message.getTime();
    }

}

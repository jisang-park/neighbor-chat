package neighborchat.messageserver.domain.dto;

import lombok.Builder;
import lombok.Getter;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponseDto {

    private String userId;
    private String content;
    private MessageType type;
    private LocalDateTime time;

    public static MessageResponseDto convert(Message message) {
        return MessageResponseDto.builder()
                .userId(message.getUserId())
                .content(message.getContent())
                .type(message.getType())
                .time(message.getTime())
                .build();
    }

}

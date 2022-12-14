package neighborchat.messageserver.domain.dto;

import lombok.Getter;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;

import java.time.LocalDateTime;

@Getter
public class MessageRequestDto {

    private String userId;
    private String content;
    private String type;

    public static Message convert(MessageRequestDto messageRequestDto, String roomId) {
        return Message.builder()
                .roomId(roomId)
                .userId(messageRequestDto.getUserId())
                .content(messageRequestDto.getContent())
                .type(MessageType.valueOf(messageRequestDto.getType()))
                .time(LocalDateTime.now())
                .build();
    }

}

package neighborchat.messageserver.domain;

import lombok.Getter;
import neighborchat.messageserver.domain.dto.MessageRequestDto;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Document("message")
public class Message {

    @Id
    private String id;
    private String roomId;
    private String userId;
    private String content;
    private MessageType type;
    private LocalDateTime time;

    public Message(MessageRequestDto messageRequestDto, String roomId) {
        this.roomId = roomId;
        this.userId = messageRequestDto.getUserId();
        this.content = messageRequestDto.getContent();
        this.type = MessageType.valueOf(messageRequestDto.getType());
        this.time = LocalDateTime.now();
    }

}

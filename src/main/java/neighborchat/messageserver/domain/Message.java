package neighborchat.messageserver.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Document("message")
public class Message {

    @Id
    private String id;
    private String roomId;
    private String userId;
    private String content;
    private MessageType type;
    private LocalDateTime time;

}

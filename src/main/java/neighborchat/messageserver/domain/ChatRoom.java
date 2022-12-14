package neighborchat.messageserver.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Document("chat-room")
public class ChatRoom {

    @Id
    private String id;
    private String leaderId;
    private String subject;
    private String name;
    private GeoJsonPoint location;
    private LocalDateTime createdTime;
    private LocalDateTime lastChatTime;
    private List<String> banned;

}

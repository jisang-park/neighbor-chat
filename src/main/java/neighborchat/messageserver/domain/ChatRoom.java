package neighborchat.messageserver.domain;

import lombok.Getter;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
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

    public ChatRoom(ChatRoomRequestDto chatRoomRequestDto) {
        this.leaderId = chatRoomRequestDto.getLeaderId();
        this.subject = chatRoomRequestDto.getSubject();
        this.name = chatRoomRequestDto.getName();
        this.location = new GeoJsonPoint(chatRoomRequestDto.getLongitude(), chatRoomRequestDto.getLatitude());
        this.createdTime = LocalDateTime.now();
        this.lastChatTime = LocalDateTime.now();
        this.banned = new ArrayList<>();
    }

}

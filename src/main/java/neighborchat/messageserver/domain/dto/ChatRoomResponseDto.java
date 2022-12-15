package neighborchat.messageserver.domain.dto;

import lombok.Getter;
import neighborchat.messageserver.domain.ChatRoom;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponseDto {

    private String name;
    private String subject;
    private double longitude;
    private double latitude;
    private LocalDateTime lastChatTime;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.name = chatRoom.getName();
        this.subject = chatRoom.getSubject();
        this.longitude = chatRoom.getLocation().getX();
        this.latitude = chatRoom.getLocation().getY();
        this.lastChatTime = chatRoom.getLastChatTime();
    }

}

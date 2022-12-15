package neighborchat.messageserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neighborchat.messageserver.domain.ChatRoom;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDto {

    private String leaderId;
    private String subject;
    private String name;
    private Double longitude;
    private Double latitude;

    public static ChatRoom convert(ChatRoomRequestDto chatRoomRequestDto) {
        return ChatRoom.builder()
                .leaderId(chatRoomRequestDto.getLeaderId())
                .subject(chatRoomRequestDto.getSubject())
                .name(chatRoomRequestDto.getName())
                .location(new GeoJsonPoint(chatRoomRequestDto.getLongitude(), chatRoomRequestDto.getLatitude()))
                .createdTime(LocalDateTime.now())
                .lastChatTime(LocalDateTime.now())
                .banned(new ArrayList<>())
                .build();
    }

}

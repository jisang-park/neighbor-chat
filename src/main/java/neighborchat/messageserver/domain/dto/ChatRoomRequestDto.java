package neighborchat.messageserver.domain.dto;

import lombok.Getter;

@Getter
public class ChatRoomRequestDto {

    private String leaderId;
    private String subject;
    private String name;
    private Double longitude;
    private Double latitude;

}

package neighborchat.messageserver.domain.dto;

import lombok.Getter;

@Getter
public class MessageRequestDto {

    private String userId;
    private String content;
    private String type;

}

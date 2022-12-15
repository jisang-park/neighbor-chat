package neighborchat.messageserver.integration;

import neighborchat.messageserver.controller.ChatRoomController;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest
class ChatRoomIntegrationTest {

    @Autowired
    ChatRoomController chatRoomController;

    @Autowired
    MongoTemplate mongoTemplate;

    @AfterEach
    void clear() {
        mongoTemplate.remove(new Query(), "chat-room");
        mongoTemplate.remove(new Query(), "message");
    }

    @Test
    void createChatRoomTest() {
        ChatRoomRequestDto request = ChatRoomRequestDto.builder()
                .leaderId("leaderId")
                .subject("음식")
                .name("서울시청 근처 맛집")
                .longitude(126.9779451)
                .latitude(37.5662952)
                .build();
        Map<String, String> response = chatRoomController.createChatRoom(request);

        Assertions.assertThat(response.get("roomId")).isNotNull();
    }

}

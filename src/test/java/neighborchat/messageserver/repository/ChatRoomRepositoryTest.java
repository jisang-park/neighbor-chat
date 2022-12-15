package neighborchat.messageserver.repository;

import neighborchat.messageserver.domain.ChatRoom;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

@ActiveProfiles("test")
@DataMongoTest
class ChatRoomRepositoryTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @AfterEach
    void clear() {
        chatRoomRepository.deleteAll();
    }

    @Test
    void saveTest() {
        ChatRoom chatRoom = ChatRoom.builder()
                .leaderId("leaderId")
                .subject("subject")
                .name("name")
                .location(new GeoJsonPoint(123.12345, 37.653213))
                .createdTime(LocalDateTime.now())
                .lastChatTime(LocalDateTime.now())
                .banned(new ArrayList<>(Collections.singleton("bannedUserId")))
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        Assertions.assertThat(savedChatRoom.getId()).isNotNull();
        Assertions.assertThat(savedChatRoom.getLeaderId()).isEqualTo(chatRoom.getLeaderId());
        Assertions.assertThat(savedChatRoom.getSubject()).isEqualTo(chatRoom.getSubject());
        Assertions.assertThat(savedChatRoom.getName()).isEqualTo(chatRoom.getName());
        Assertions.assertThat(savedChatRoom.getLocation()).isEqualTo(chatRoom.getLocation());
        Assertions.assertThat(savedChatRoom.getCreatedTime()).isEqualTo(chatRoom.getCreatedTime());
        Assertions.assertThat(savedChatRoom.getLastChatTime()).isEqualTo(chatRoom.getLastChatTime());
        Assertions.assertThat(savedChatRoom.getBanned().size()).isEqualTo(chatRoom.getBanned().size());
    }

}

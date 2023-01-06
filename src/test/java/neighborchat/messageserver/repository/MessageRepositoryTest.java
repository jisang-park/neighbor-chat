package neighborchat.messageserver.repository;

import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;
import neighborchat.messageserver.repository.mongodb.MessageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ActiveProfiles("test")
@DataMongoTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        Message message1 = Message.builder().userId("user1").roomId("room1").content("content").type(MessageType.MESSAGE).time(now.minus(7L, ChronoUnit.SECONDS)).build();
        Message message2 = Message.builder().userId("user2").roomId("room1").content("content").type(MessageType.MESSAGE).time(now.minus(6L, ChronoUnit.SECONDS)).build();
        Message message3 = Message.builder().userId("user3").roomId("room1").content("content").type(MessageType.IMAGE_URL).time(now.minus(5L, ChronoUnit.SECONDS)).build();
        Message message4 = Message.builder().userId("user4").roomId("room1").content("content").type(MessageType.IMAGE_URL).time(now.minus(4L, ChronoUnit.SECONDS)).build();
        Message message5 = Message.builder().userId("user1").roomId("room2").content("content").type(MessageType.MESSAGE).time(now.minus(3L, ChronoUnit.SECONDS)).build();
        Message message6 = Message.builder().userId("user2").roomId("room2").content("content").type(MessageType.MESSAGE).time(now.minus(2L, ChronoUnit.SECONDS)).build();
        Message message7 = Message.builder().userId("user3").roomId("room2").content("content").type(MessageType.IMAGE_URL).time(now.minus(1L, ChronoUnit.SECONDS)).build();
        Message message8 = Message.builder().userId("user4").roomId("room2").content("content").type(MessageType.IMAGE_URL).time(now).build();

        messageRepository.saveAll(List.of(message1, message2, message3, message4, message5, message6, message7, message8));
    }

    @AfterEach
    void clear() {
        messageRepository.deleteAll();
    }

    @Test
    void findByRoomIdTest() {
        List<Message> page0 = messageRepository.findAllByRoomId("room1", PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "time")));
        List<Message> page1 = messageRepository.findAllByRoomId("room1", PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "time")));
        List<Message> page2 = messageRepository.findAllByRoomId("room1", PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "time")));

        Assertions.assertThat(page0.size()).isEqualTo(2);
        Assertions.assertThat(page0.get(0).getUserId()).isEqualTo("user4");
        Assertions.assertThat(page0.get(1).getUserId()).isEqualTo("user3");
        Assertions.assertThat(page0.get(0).getType()).isEqualTo(MessageType.IMAGE_URL);
        Assertions.assertThat(page0.get(1).getType()).isEqualTo(MessageType.IMAGE_URL);

        Assertions.assertThat(page1.size()).isEqualTo(2);
        Assertions.assertThat(page1.get(0).getUserId()).isEqualTo("user2");
        Assertions.assertThat(page1.get(1).getUserId()).isEqualTo("user1");
        Assertions.assertThat(page1.get(0).getType()).isEqualTo(MessageType.MESSAGE);
        Assertions.assertThat(page1.get(1).getType()).isEqualTo(MessageType.MESSAGE);

        Assertions.assertThat(page2.size()).isEqualTo(0);
    }

}
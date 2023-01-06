package neighborchat.messageserver.repository;

import neighborchat.messageserver.domain.ChatRoom;
import neighborchat.messageserver.repository.mongodb.ChatRoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("test")
@DataMongoTest
class ChatRoomRepositoryTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        ChatRoom room1 = ChatRoom.builder().leaderId("leader1").subject("subject1").name("서울시청").location(new GeoJsonPoint(126.9779451, 37.5662952)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room2 = ChatRoom.builder().leaderId("leader2").subject("subject1").name("서울광장").location(new GeoJsonPoint(126.9780141, 37.5655675)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room3 = ChatRoom.builder().leaderId("leader3").subject("subject1").name("덕수궁").location(new GeoJsonPoint(126.9751461, 37.5658049)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room4 = ChatRoom.builder().leaderId("leader4").subject("subject1").name("숭례문").location(new GeoJsonPoint(126.9753071, 37.559984)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room5 = ChatRoom.builder().leaderId("leader5").subject("subject1").name("광화문").location(new GeoJsonPoint(126.9768121, 37.5758772)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room6 = ChatRoom.builder().leaderId("leader6").subject("subject2").name("서울시청").location(new GeoJsonPoint(126.9779451, 37.5662952)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room7 = ChatRoom.builder().leaderId("leader7").subject("subject2").name("서울광장").location(new GeoJsonPoint(126.9780141, 37.5655675)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room8 = ChatRoom.builder().leaderId("leader8").subject("subject2").name("덕수궁").location(new GeoJsonPoint(126.9751461, 37.5658049)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room9 = ChatRoom.builder().leaderId("leader9").subject("subject2").name("숭례문").location(new GeoJsonPoint(126.9753071, 37.559984)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        ChatRoom room10 = ChatRoom.builder().leaderId("leader10").subject("subject2").name("광화문").location(new GeoJsonPoint(126.9768121, 37.5758772)).createdTime(now).lastChatTime(now).banned(List.of()).build();

        chatRoomRepository.saveAll(List.of(room1, room2, room3, room4, room5, room6, room7, room8, room9, room10));
    }

    @AfterEach
    void clear() {
        chatRoomRepository.deleteAll();
    }

    @Test
    void saveTest() {
        ChatRoom chatRoom = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.12345, 37.653213)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(List.of("bannedUserId")).build();

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

    @Test
    void searchWithoutSubjectTest() {
        GeoJsonPoint cityHall = new GeoJsonPoint(126.9779451, 37.5662952);
        List<ChatRoom> within100 = chatRoomRepository.findByLocationNear(cityHall, new Distance(0.1, Metrics.KILOMETERS));
        List<ChatRoom> within500 = chatRoomRepository.findByLocationNear(cityHall, new Distance(0.5, Metrics.KILOMETERS));
        List<ChatRoom> within1000 = chatRoomRepository.findByLocationNear(cityHall, new Distance(1.0, Metrics.KILOMETERS));

        Assertions.assertThat(within100.size()).isEqualTo(4);
        Assertions.assertThat(within100.stream().map(ChatRoom::getSubject).toList()).contains("subject1", "subject2");
        Assertions.assertThat(within500.size()).isEqualTo(6);
        Assertions.assertThat(within500.stream().map(ChatRoom::getSubject).toList()).contains("subject1", "subject2");
        Assertions.assertThat(within1000.size()).isEqualTo(8);
        Assertions.assertThat(within1000.stream().map(ChatRoom::getSubject).toList()).contains("subject1", "subject2");
    }

    @Test
    void searchWithSubjectTest() {
        GeoJsonPoint cityHall = new GeoJsonPoint(126.9779451, 37.5662952);
        List<ChatRoom> within100subject1 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(0.1, Metrics.KILOMETERS), "subject1");
        List<ChatRoom> within500subject1 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(0.5, Metrics.KILOMETERS), "subject1");
        List<ChatRoom> within1000subject1 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(1.0, Metrics.KILOMETERS), "subject1");

        List<ChatRoom> within100subject2 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(0.1, Metrics.KILOMETERS), "subject2");
        List<ChatRoom> within500subject2 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(0.5, Metrics.KILOMETERS), "subject2");
        List<ChatRoom> within1000subject2 = chatRoomRepository.findByLocationNearAndSubject(cityHall, new Distance(1.0, Metrics.KILOMETERS), "subject2");

        Assertions.assertThat(within100subject1.size()).isEqualTo(2);
        Assertions.assertThat(within100subject1.stream().map(ChatRoom::getSubject).toList()).contains("subject1");
        Assertions.assertThat(within100subject1.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject2");
        Assertions.assertThat(within500subject1.size()).isEqualTo(3);
        Assertions.assertThat(within500subject1.stream().map(ChatRoom::getSubject).toList()).contains("subject1");
        Assertions.assertThat(within500subject1.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject2");
        Assertions.assertThat(within1000subject1.size()).isEqualTo(4);
        Assertions.assertThat(within1000subject1.stream().map(ChatRoom::getSubject).toList()).contains("subject1");
        Assertions.assertThat(within1000subject1.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject2");

        Assertions.assertThat(within100subject2.size()).isEqualTo(2);
        Assertions.assertThat(within100subject2.stream().map(ChatRoom::getSubject).toList()).contains("subject2");
        Assertions.assertThat(within100subject2.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject1");
        Assertions.assertThat(within500subject2.size()).isEqualTo(3);
        Assertions.assertThat(within500subject2.stream().map(ChatRoom::getSubject).toList()).contains("subject2");
        Assertions.assertThat(within500subject2.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject1");
        Assertions.assertThat(within1000subject2.size()).isEqualTo(4);
        Assertions.assertThat(within1000subject2.stream().map(ChatRoom::getSubject).toList()).contains("subject2");
        Assertions.assertThat(within1000subject2.stream().map(ChatRoom::getSubject).toList()).doesNotContain("subject1");
    }

}

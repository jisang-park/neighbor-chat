package neighborchat.messageserver.service;

import neighborchat.messageserver.domain.ChatRoom;
import neighborchat.messageserver.repository.ChatRoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Test
    void createChatRoomTest() {
        ChatRoom chatRoom = ChatRoom.builder()
                .leaderId("leaderId")
                .subject("subject")
                .name("name")
                .location(new GeoJsonPoint(123.12345, 37.653213))
                .createdTime(LocalDateTime.now())
                .lastChatTime(LocalDateTime.now())
                .banned(new ArrayList<>())
                .build();

        ChatRoom savedChatRoom = ChatRoom.builder()
                .id("chatRoomId")
                .leaderId("leaderId")
                .subject("subject")
                .name("name")
                .location(new GeoJsonPoint(123.12345, 37.653213))
                .createdTime(LocalDateTime.now())
                .lastChatTime(LocalDateTime.now())
                .banned(new ArrayList<>())
                .build();

        when(chatRoomRepository.save(chatRoom)).thenReturn(savedChatRoom);

        String chatRoomId = chatRoomService.createChatRoom(chatRoom);

        Assertions.assertThat(chatRoomId).isEqualTo("chatRoomId");
    }

}

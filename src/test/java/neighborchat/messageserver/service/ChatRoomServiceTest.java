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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Test
    void createChatRoomTest() {
        ChatRoom chatRoom = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();
        ChatRoom savedChatRoom = ChatRoom.builder().id("chatRoomId").leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();

        when(chatRoomRepository.save(chatRoom)).thenReturn(savedChatRoom);

        String chatRoomId = chatRoomService.createChatRoom(chatRoom);

        Assertions.assertThat(chatRoomId).isEqualTo("chatRoomId");
    }

    @Test
    void searchChatRoomsWithoutSubjectTest() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();

        ChatRoom chatRoom1 = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();
        ChatRoom chatRoom2 = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();

        chatRooms.add(chatRoom1);
        chatRooms.add(chatRoom2);

        when(chatRoomRepository.findByLocationNear(any(), any())).thenReturn(chatRooms);

        List<ChatRoom> results = chatRoomService.searchChatRooms(123.456789, 37.123456, 0.5);

        Assertions.assertThat(results).containsExactly(chatRoom1, chatRoom2);
        verify(chatRoomRepository, only()).findByLocationNear(any(), any());
        verify(chatRoomRepository, never()).findByLocationNearAndSubject(any(), any(), anyString());
    }

    @Test
    void searchChatRoomsWithSubjectTest() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();

        ChatRoom chatRoom1 = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();
        ChatRoom chatRoom2 = ChatRoom.builder().leaderId("leaderId").subject("subject").name("name").location(new GeoJsonPoint(123.456789, 37.123456)).createdTime(LocalDateTime.now()).lastChatTime(LocalDateTime.now()).banned(new ArrayList<>()).build();

        chatRooms.add(chatRoom1);
        chatRooms.add(chatRoom2);

        when(chatRoomRepository.findByLocationNearAndSubject(any(), any(), eq("subject"))).thenReturn(chatRooms);

        List<ChatRoom> results = chatRoomService.searchChatRooms("subject", 123.456789, 37.123456, 0.5);

        Assertions.assertThat(results).containsExactly(chatRoom1, chatRoom2);
        verify(chatRoomRepository, never()).findByLocationNear(any(), any());
        verify(chatRoomRepository, only()).findByLocationNearAndSubject(any(), any(), eq("subject"));
    }

}

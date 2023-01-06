package neighborchat.messageserver.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import neighborchat.messageserver.domain.ChatRoom;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import neighborchat.messageserver.repository.mongodb.ChatRoomRepository;
import neighborchat.messageserver.repository.mongodb.MessageRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ChatRoomIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @AfterEach
    void clear() {
        chatRoomRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void createChatRoomTest() throws Exception {
        ChatRoomRequestDto request = new ChatRoomRequestDto();
        ReflectionTestUtils.setField(request, "leaderId", "leaderId");
        ReflectionTestUtils.setField(request, "subject", "음식");
        ReflectionTestUtils.setField(request, "name", "서울시청 근처 맛집");
        ReflectionTestUtils.setField(request, "longitude", 126.9779451);
        ReflectionTestUtils.setField(request, "latitude", 37.5662952);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/chat-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..roomId").isNotEmpty());
    }

    @Test
    void readMessagesTest() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        ChatRoom room = ChatRoom.builder().leaderId("leader1").subject("subject1").name("서울시청").location(new GeoJsonPoint(126.9779451, 37.5662952)).createdTime(now).lastChatTime(now).banned(List.of()).build();
        String roomId = chatRoomRepository.save(room).getId();

        Message message1 = Message.builder().userId("user1").roomId(roomId).content("content").type(MessageType.MESSAGE).time(now).build();
        Message message2 = Message.builder().userId("user2").roomId(roomId).content("content").type(MessageType.MESSAGE).time(now).build();
        Message message3 = Message.builder().userId("user3").roomId(roomId).content("content").type(MessageType.IMAGE_URL).time(now).build();
        Message message4 = Message.builder().userId("user4").roomId(roomId).content("content").type(MessageType.IMAGE_URL).time(now).build();
        Message message5 = Message.builder().userId("user1").roomId("anotherRoomId").content("content").type(MessageType.MESSAGE).time(now).build();
        Message message6 = Message.builder().userId("user2").roomId("anotherRoomId").content("content").type(MessageType.MESSAGE).time(now).build();
        Message message7 = Message.builder().userId("user3").roomId("anotherRoomId").content("content").type(MessageType.IMAGE_URL).time(now).build();
        Message message8 = Message.builder().userId("user4").roomId("anotherRoomId").content("content").type(MessageType.IMAGE_URL).time(now).build();
        messageRepository.saveAll(List.of(message1, message2, message3, message4, message5, message6, message7, message8));

        mockMvc.perform(get(String.format("/chat-room/%s", roomId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()", Matchers.is(4)))
                .andExpect(jsonPath("$..userId", Matchers.everyItem(Matchers.startsWith("user"))));
    }

    @Test
    void searchChatRoomTest() throws Exception {
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

        mockMvc.perform(get("/chat-room/search")
                        .param("subject", "subject1")
                        .param("longitude", "126.9779451")
                        .param("latitude", "37.5662952")
                        .param("distance", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()", Matchers.is(4)));

        mockMvc.perform(get("/chat-room/search")
                        .param("longitude", "126.9779451")
                        .param("latitude", "37.5662952")
                        .param("distance", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()", Matchers.is(8)));
    }

}

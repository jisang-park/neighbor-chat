package neighborchat.messageserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;
import neighborchat.messageserver.domain.dto.ChatRoomRequestDto;
import neighborchat.messageserver.domain.dto.MessageResponseDto;
import neighborchat.messageserver.service.ChatRoomService;
import neighborchat.messageserver.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MessageService messageService;

    @MockBean
    ChatRoomService chatRoomService;

    @Test
    void createChatRoomTest() throws Exception {
        ChatRoomRequestDto request = ChatRoomRequestDto.builder()
                .leaderId("leaderId")
                .subject("음식")
                .name("서울시청 근처 맛집")
                .longitude(126.9779451)
                .latitude(37.5662952)
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        when(chatRoomService.createChatRoom(any())).thenReturn("newChatRoomId");

        mockMvc.perform(post("/chat-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("roomId", "newChatRoomId"))));
    }

    @Test
    void readMessagesTest() throws Exception {
        String roomId = "roomId";
        ArrayList<Message> page0 = new ArrayList<>();
        ArrayList<Message> page1 = new ArrayList<>();

        page0.add(Message.builder().id("message1").userId("userId").roomId(roomId).type(MessageType.MESSAGE).time(LocalDateTime.now()).content("content1").build());
        page0.add(Message.builder().id("message2").userId("userId").roomId(roomId).type(MessageType.MESSAGE).time(LocalDateTime.now()).content("content2").build());
        page0.add(Message.builder().id("message3").userId("userId").roomId(roomId).type(MessageType.MESSAGE).time(LocalDateTime.now()).content("content3").build());
        page0.add(Message.builder().id("message4").userId("userId").roomId(roomId).type(MessageType.MESSAGE).time(LocalDateTime.now()).content("content4").build());
        page0.add(Message.builder().id("message5").userId("userId").roomId(roomId).type(MessageType.MESSAGE).time(LocalDateTime.now()).content("content5").build());

        page1.add(Message.builder().id("message6").userId("userId").roomId(roomId).type(MessageType.IMAGE_URL).time(LocalDateTime.now()).content("content6").build());
        page1.add(Message.builder().id("message7").userId("userId").roomId(roomId).type(MessageType.IMAGE_URL).time(LocalDateTime.now()).content("content7").build());
        page1.add(Message.builder().id("message8").userId("userId").roomId(roomId).type(MessageType.IMAGE_URL).time(LocalDateTime.now()).content("content8").build());
        page1.add(Message.builder().id("message9").userId("userId").roomId(roomId).type(MessageType.IMAGE_URL).time(LocalDateTime.now()).content("content9").build());
        page1.add(Message.builder().id("message10").userId("userId").roomId(roomId).type(MessageType.IMAGE_URL).time(LocalDateTime.now()).content("content10").build());

        List<MessageResponseDto> response0 = page0.stream().map(MessageResponseDto::convert).toList();
        List<MessageResponseDto> response1 = page1.stream().map(MessageResponseDto::convert).toList();

        when(messageService.readMessages(roomId, 0)).thenReturn(page0);
        when(messageService.readMessages(roomId, 1)).thenReturn(page1);

        mockMvc.perform(get(String.format("/chat-room/%s", roomId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response0)));

        mockMvc.perform(get(String.format("/chat-room/%s", roomId)).queryParam("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response0)));

        mockMvc.perform(get(String.format("/chat-room/%s", roomId)).queryParam("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response1)));
    }

}
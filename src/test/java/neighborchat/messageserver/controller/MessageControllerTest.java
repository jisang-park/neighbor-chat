package neighborchat.messageserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import neighborchat.messageserver.domain.dto.MessageRequestDto;
import neighborchat.messageserver.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MessageService messageService;

    @Test
    void sendMessageTest() throws Exception {
        MessageRequestDto request = MessageRequestDto.builder()
                .userId("userId")
                .content("content")
                .type("MESSAGE")
                .build();

        when(messageService.sendMessage(any())).thenReturn("newMessageId");

        mockMvc.perform(post("/message/roomId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("messageId", "newMessageId"))));
    }

}

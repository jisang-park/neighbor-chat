package neighborchat.messageserver.service;

import neighborchat.messageserver.domain.Message;
import neighborchat.messageserver.domain.MessageType;
import neighborchat.messageserver.repository.mongodb.MessageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Test
    void sendMessageTest() {
        Message message = Message.builder()
                .roomId("roomId")
                .userId("userId")
                .content("content")
                .type(MessageType.MESSAGE)
                .time(LocalDateTime.now())
                .build();

        Message savedMessage = Message.builder()
                .id("messageId")
                .roomId("roomId")
                .userId("userId")
                .content("content")
                .type(MessageType.MESSAGE)
                .time(LocalDateTime.now())
                .build();

        when(messageRepository.save(any())).thenReturn(savedMessage);

        String result = messageService.sendMessage(message);

        Assertions.assertThat(result).isEqualTo("messageId");
    }

    @Test
    void readMessagesTest() {
        String roomId = "roomId";

        Message message1 = Message.builder()
                .id("message1")
                .roomId(roomId)
                .userId("userId")
                .content("content")
                .type(MessageType.MESSAGE)
                .time(LocalDateTime.now())
                .build();

        Message message2 = Message.builder()
                .id("message2")
                .roomId(roomId)
                .userId("userId")
                .content("content")
                .type(MessageType.MESSAGE)
                .time(LocalDateTime.now())
                .build();

        Message message3 = Message.builder()
                .id("message3")
                .roomId(roomId)
                .userId("userId")
                .content("content")
                .type(MessageType.MESSAGE)
                .time(LocalDateTime.now())
                .build();

        when(messageRepository.findAllByRoomId(eq(roomId), any())).thenReturn(List.of(message1, message2, message3));

        List<Message> result = messageService.readMessages(roomId, 0);

        Assertions.assertThat(result).allMatch(message -> message.getRoomId().equals(roomId));
    }

}
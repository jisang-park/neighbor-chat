package neighborchat.messageserver.service;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.ChatRoom;
import neighborchat.messageserver.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom).getId();
    }

}

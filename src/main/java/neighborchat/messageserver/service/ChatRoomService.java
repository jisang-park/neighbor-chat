package neighborchat.messageserver.service;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.ChatRoom;
import neighborchat.messageserver.repository.mongodb.ChatRoomRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public String createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom).getId();
    }

    public List<ChatRoom> searchChatRooms(double longitude, double latitude, double distance) {
        GeoJsonPoint center = new GeoJsonPoint(longitude, latitude);
        Distance radius = new Distance(distance, Metrics.KILOMETERS);
        return chatRoomRepository.findByLocationNear(center, radius);
    }

    public List<ChatRoom> searchChatRooms(String subject, double longitude, double latitude, double distance) {
        GeoJsonPoint center = new GeoJsonPoint(longitude, latitude);
        Distance radius = new Distance(distance, Metrics.KILOMETERS);
        return chatRoomRepository.findByLocationNearAndSubject(center, radius, subject);
    }

}

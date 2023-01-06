package neighborchat.messageserver.repository.mongodb;

import neighborchat.messageserver.domain.ChatRoom;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByLocationNear(Point location, Distance distance);
    List<ChatRoom> findByLocationNearAndSubject(Point location, Distance distance, String subject);

}

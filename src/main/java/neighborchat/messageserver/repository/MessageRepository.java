package neighborchat.messageserver.repository;

import neighborchat.messageserver.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByRoomId(String roomId, Pageable pageable);

}

package neighborchat.messageserver.repository;

import neighborchat.messageserver.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}

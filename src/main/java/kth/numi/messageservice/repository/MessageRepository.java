package kth.numi.messageservice.repository;

import kth.numi.messageservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findMessagesBySenderIDAndReceiverIDOrderByTimestamp(Integer senderId, Integer receiverId);
}


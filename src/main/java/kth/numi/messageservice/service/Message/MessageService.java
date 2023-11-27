package kth.numi.messageservice.service.Message;

import kth.numi.messageservice.model.Message;
import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<?> sendMessage(Message message);

    ResponseEntity<?> getMessagesBySenderAndReceiverOrderedByTimestamp(Integer senderId, Integer receiverId);
}

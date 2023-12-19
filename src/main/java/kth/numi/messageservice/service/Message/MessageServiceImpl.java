package kth.numi.messageservice.service.Message;

import kth.numi.messageservice.model.Message;
import kth.numi.messageservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public ResponseEntity<?> sendMessage(Message message) {
        try {
            message = messageRepository.save(message);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            e.printStackTrace(); // Gives details of the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @Override
    public ResponseEntity<?> getMessagesBySenderAndReceiverOrderedByTimestamp(Integer senderId, Integer receiverId) {
        try {
            List<Message> messages = messageRepository.
                    findMessagesBySenderIDAndReceiverIDOrderByTimestamp(senderId, receiverId);
            if (!messages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(messages);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Did not find any messages");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred");
        }
    }
}
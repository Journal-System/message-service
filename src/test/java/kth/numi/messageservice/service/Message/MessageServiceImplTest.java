package kth.numi.messageservice.service.Message;

import kth.numi.messageservice.model.Message;
import kth.numi.messageservice.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(MessageServiceImpl.class)
class MessageServiceImplTest {

    @Autowired private MessageServiceImpl messageServiceImpl;
    @MockBean MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        messageServiceImpl = new MessageServiceImpl(messageRepository);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testToSendMessageIfValid() {
        Message mockMessage = createMessage();
        when(messageRepository.save(mockMessage))
                .thenReturn(mockMessage);

        ResponseEntity<?> response = messageServiceImpl.sendMessage(mockMessage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessage, response.getBody());
    }
    @Test
    void testToSendMessageIfItThrowsException() {
        when(messageRepository.save(null))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = messageServiceImpl.sendMessage(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred", response.getBody());
    }
    @Test
    void testToGetMessagesBySenderAndReceiverOrderedByTimestampIfValid() {
        Message mockMessage1 = createMessage();
        Message mockMessage2 = createMessage2();
        List<Message> mockMessageList = new ArrayList<>();
        mockMessageList.add(mockMessage1);
        mockMessageList.add(mockMessage2);

        when(messageRepository.findMessagesBySenderIDAndReceiverIDOrderByTimestamp(1, 2))
                .thenReturn(mockMessageList);

        ResponseEntity<?> response = messageServiceImpl.getMessagesBySenderAndReceiverOrderedByTimestamp(1, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessageList, response.getBody());
    }
    @Test
    void testToGetMessagesBySenderAndReceiverOrderedByTimestampIfItThrowsException() {
        when(messageRepository.findMessagesBySenderIDAndReceiverIDOrderByTimestamp(1,2))
                .thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<?> response = messageServiceImpl.getMessagesBySenderAndReceiverOrderedByTimestamp(1,2);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred", response.getBody());
    }

    @Test
    void testToGetMessagesBySenderAndReceiverOrderedByTimestampIfNotValid() {
        when(messageRepository.findMessagesBySenderIDAndReceiverIDOrderByTimestamp(1, 2))
                .thenReturn(null);

        ResponseEntity<?> response = messageServiceImpl.getMessagesBySenderAndReceiverOrderedByTimestamp(12, 2);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Did not find any messages", response.getBody());
    }
    private Message createMessage() {
        Message message = new Message();
        message.setId(1);
        message.setContent("Hello World");
        message.setTimestamp(LocalDateTime.parse("2023-11-27T15:03:57.247493"));
        message.setSenderID(1);
        message.setReceiverID(2);
        return message;

    }
    private Message createMessage2() {
        Message message = new Message();
        message.setId(1);
        message.setContent("Hello World Two");
        message.setTimestamp(LocalDateTime.parse("2023-11-27T15:03:57.247493"));
        message.setSenderID(1);
        message.setReceiverID(2);
        return message;

    }
}
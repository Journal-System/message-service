package kth.numi.messageservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kth.numi.messageservice.model.Message;
import kth.numi.messageservice.service.Message.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {
    @Autowired private MockMvc mvc;
    @MockBean private MessageService messageService;
    @Autowired ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void testToGetMessagesBySenderAndReceiverIfValid() throws Exception {
        Message mockMessage = createMessage();

        given(messageService.getMessagesBySenderAndReceiverOrderedByTimestamp(1,2))
                .willAnswer(invocation -> ResponseEntity.ok(mockMessage));

        mvc.perform(get("/message/getMessagesBySenderAndReceiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("senderId", "1")
                        .param("receiverId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("senderID").value(1))
                .andExpect(jsonPath("receiverID").value(2))
                .andExpect(jsonPath("content").value("Hello World"))
                .andExpect(jsonPath("timestamp").value("2023-11-27T15:03:57"));

        verify(messageService, times(1)).getMessagesBySenderAndReceiverOrderedByTimestamp(1, 2);
    }
    @Test
    void testToGetMessagesBySenderAndReceiverIfItThrowsException() throws Exception {
        given(messageService.getMessagesBySenderAndReceiverOrderedByTimestamp(1, 2))
                .willAnswer(invocation -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));

        mvc.perform(get("/message/getMessagesBySenderAndReceiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("senderId", "1")
                        .param("receiverId", "2"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred"));

        verify(messageService, times(1)).getMessagesBySenderAndReceiverOrderedByTimestamp(1, 2);
    }
    @Test
    void testToGetMessagesBySenderAndReceiverIfNotValid() throws Exception {
        given(messageService.getMessagesBySenderAndReceiverOrderedByTimestamp(1, 2))
                .willAnswer(invocation -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Did not find any messages"));

        mvc.perform(get("/message/getMessagesBySenderAndReceiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("senderId", "1")
                        .param("receiverId", "2"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Did not find any messages"));

        verify(messageService, times(1)).getMessagesBySenderAndReceiverOrderedByTimestamp(1,2);
    }
    @Test
    void testToSendMessageIfValid() throws Exception {
        Message mockMessage = createMessage();
        String mockMessageJson = objectMapper.writeValueAsString(mockMessage);

        given(messageService.sendMessage(mockMessage))
                .willAnswer(invocation -> ResponseEntity.ok(mockMessage));

        mvc.perform(post("/message/addMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mockMessageJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("senderID").value(1))
                .andExpect(jsonPath("receiverID").value(2))
                .andExpect(jsonPath("content").value("Hello World"))
                .andExpect(jsonPath("timestamp").value("2023-11-27T15:03:57"));

        verify(messageService, times(1)).sendMessage(mockMessage);
    }
    @Test
    void testToSendMessageIfItThrowsException() throws Exception {
        Message mockMessage = createMessage();
        String mockMessageJson = objectMapper.writeValueAsString(mockMessage);

        given(messageService.sendMessage(mockMessage))
                .willAnswer(invocation -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"));

        mvc.perform(post("/message/addMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mockMessageJson))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred"));

        verify(messageService, times(1)).sendMessage(mockMessage);
    }
    private Message createMessage() {
        Message message = new Message();
        message.setId(1);
        message.setContent("Hello World");
        message.setTimestamp(LocalDateTime.parse("2023-11-27T15:03:57"));
        message.setSenderID(1);
        message.setReceiverID(2);
        return message;

    }
}
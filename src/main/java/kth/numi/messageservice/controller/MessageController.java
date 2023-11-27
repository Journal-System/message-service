package kth.numi.messageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kth.numi.messageservice.model.Message;
import kth.numi.messageservice.service.Message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@Tag(name = "Message Controller", description = "Manage message data")
public class MessageController {
    final private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/getMessagesBySenderAndReceiver")
    @Operation(summary = "Get messages between sender and receiver",
            description = "Get all messages between a specific sender and receiver")
    public ResponseEntity<?> getMessagesBySenderAndReceiver(@RequestParam Integer senderId,
                                                            @RequestParam Integer receiverId) {
        return messageService.getMessagesBySenderAndReceiverOrderedByTimestamp(senderId, receiverId);
    }

    @PostMapping("/addMessage")
    @Operation(summary = "Send a message",
            description = "Send a message to patient, doctor or staff")
    public ResponseEntity<?> add(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }
}


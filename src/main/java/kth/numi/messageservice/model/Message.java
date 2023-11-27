package kth.numi.messageservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private int id;

    @Schema(example = "2")
    @Column(name = "SenderID", nullable = false, columnDefinition = "INT")
    private Integer senderID;

    @Schema(example = "4")
    @Column(name = "ReceiverID", nullable = false, columnDefinition = "INT")
    private Integer receiverID;

    @Schema(example = "Hey! How are you?")
    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Schema(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "Timestamp", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime timestamp = LocalDateTime.now();
}

package fakepay_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String blockId;          // SHA-256 Hash
    private String prevBlockId;      // Previous block ID in chain
    private String upcomingBlockId;  // Next block ID in chain

    private String senderPhone;
    private String senderBankName;   // Added Sender Bank Name

    private String receiverPhone;
    private String receiverBankName; // Added Receiver Bank Name

    private double money;
    private LocalDateTime time;
}
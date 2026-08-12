package quickpay_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bank_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUser {
    @Id
    private String id;
    private String userName;
    private String userPhoneNumber;
    private String upiId;
    private String upiPin;
    private double balance = 5000.0;

    private String startingBlockHashId;
    private String endingBlockHashId;
}
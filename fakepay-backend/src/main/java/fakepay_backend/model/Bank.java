package fakepay_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "banks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    private String id;
    private String bankId;
    private String bankName;
    private String email;
    private String password;
    private String upiId;
}
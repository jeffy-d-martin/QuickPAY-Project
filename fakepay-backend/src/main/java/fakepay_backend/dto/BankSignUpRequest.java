package fakepay_backend.dto;

import lombok.Data;

@Data
public class BankSignUpRequest {
    private String bankId;
    private String bankName;
    private String email;
    private String password;
    private String upiId;
}
package fakepay_backend.dto;

import lombok.Data;

@Data
public class BankLoginRequest {
    private String upiId;
    private String password;
}
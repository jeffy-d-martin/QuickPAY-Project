package quickpay_backend.dto;

import lombok.Data;

@Data
public class ConnectBankRequest {
    private String phoneNo;    // QuickPAY user's phone number
    private String bankName;   // Bank name searched/selected by user (e.g., "HDFC")
    private String upiPin;     // The new UPI PIN entered by the user
}
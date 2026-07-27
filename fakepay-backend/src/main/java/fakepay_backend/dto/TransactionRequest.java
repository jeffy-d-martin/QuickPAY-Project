package fakepay_backend.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String senderPhone;
    private String senderBankName;   // e.g., "State Bank of India" or "HDFC"
    private String receiverPhone;
    private String receiverBankName; // e.g., "ICICI Bank"
    private double money;
    private String upiPin;           // Payment PIN for validation
}
package fakepay_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class JeffyEncryptionUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPin(String rawPin) {
        return encoder.encode(rawPin);
    }


    public boolean verifyPin(String rawInputPin, String storedHash) {
        return encoder.matches(rawInputPin, storedHash);
    }

    public String generateBlockHash(double money, String receiverPhone, String senderPhone, String time) {
        String rawData = money + ":" + receiverPhone + ":" + senderPhone + ":" + time;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating transaction block hash", e);
        }
    }
}

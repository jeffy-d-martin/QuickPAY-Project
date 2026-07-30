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

    public String getTimeString(java.time.LocalDateTime time) {
        if (time == null) return "";
        return time.truncatedTo(java.time.temporal.ChronoUnit.MILLIS).toString();
    }

    public String formatMoney(double money) {
        if (money == (long) money) {
            return String.valueOf((long) money);
        }
        return String.valueOf(money);
    }

    public String generateBlockHash(String senderPhone, String receiverPhone, String time, double money) {
        String moneyStr = formatMoney(money);
        String rawData = senderPhone + receiverPhone + time + moneyStr;
        return rawData;
    }
}

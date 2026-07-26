package fakepay_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JeffyEncryptionUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPin(String rawPin) {
        return encoder.encode(rawPin);
    }


    public boolean verifyPin(String rawInputPin, String storedHash) {
        return encoder.matches(rawInputPin, storedHash);
    }
}

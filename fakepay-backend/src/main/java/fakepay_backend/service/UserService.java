package fakepay_backend.service;

import fakepay_backend.dto.LoginRequest;
import fakepay_backend.dto.SignUpRequest;
import fakepay_backend.exception.UserAlreadyExistsException;
import fakepay_backend.exception.UserNotFoundException;
import fakepay_backend.model.User;
import fakepay_backend.repository.UserRepository;
import fakepay_backend.util.JeffyEncryptionUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private JeffyEncryptionUtil jeffyEncryptionUtil;

    public User signUp(SignUpRequest signUpRequest) {
        String cleanPhone = signUpRequest.getPhoneNo() != null ? signUpRequest.getPhoneNo().replaceAll("\\D", "") : "";
        String phoneToSave = !cleanPhone.isEmpty() ? cleanPhone : signUpRequest.getPhoneNo();

        userRepository.findByPhoneNo(phoneToSave).ifPresent(user -> {
            throw new UserAlreadyExistsException("Phone number " + signUpRequest.getPhoneNo() + " is already registered!");
        });
        User user = new User();
        user.setUserName(signUpRequest.getName());
        user.setPhoneNo(phoneToSave);
        return userRepository.save(user);
    }

    public User loginWithPhone(LoginRequest loginRequest) {
        String rawPhone = loginRequest.getPhoneNo();
        String cleanPhone = rawPhone != null ? rawPhone.replaceAll("\\D", "") : "";

        return userRepository.findByPhoneNo(cleanPhone)
                .orElseGet(() -> userRepository.findByPhoneNo(rawPhone)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with phone number " + loginRequest.getPhoneNo() + " is not registered. Please sign up first."
                )));
    }


    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

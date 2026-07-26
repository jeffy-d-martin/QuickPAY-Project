package fakepay_backend.service;

import fakepay_backend.dto.SignUpRequest;
import fakepay_backend.exception.UserAlreadyExistsException;
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

        userRepository.findByPhoneNo(signUpRequest.getPhoneNo()).ifPresent(user -> {
            throw new UserAlreadyExistsException("Phone number " + signUpRequest.getPhoneNo() + " is already registered!");
        });
        User user = new User();
        user.setUserName(signUpRequest.getName());
        user.setPhoneNo(signUpRequest.getPhoneNo());
        return userRepository.save(user);
    }


}

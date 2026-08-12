package quickpay_backend.controller;

import quickpay_backend.dto.LoginRequest;
import quickpay_backend.dto.SignUpRequest;
import quickpay_backend.model.User;
import quickpay_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/homepage")
public class UserController {

    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.loginWithPhone(loginRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

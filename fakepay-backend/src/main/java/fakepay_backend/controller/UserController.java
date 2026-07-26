package fakepay_backend.controller;

import fakepay_backend.dto.LoginRequest;
import fakepay_backend.dto.SignUpRequest;
import fakepay_backend.model.User;
import fakepay_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
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
}

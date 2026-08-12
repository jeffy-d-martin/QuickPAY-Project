package quickpay_backend.controller;

import quickpay_backend.dto.BankLoginRequest;
import quickpay_backend.dto.BankSignUpRequest;
import quickpay_backend.model.Bank;
import quickpay_backend.service.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    @PostMapping("/signup")
    public ResponseEntity<Bank> signUp(@RequestBody BankSignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.signUpBank(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Bank> login(@RequestBody BankLoginRequest request) {
        return ResponseEntity.ok(bankService.loginBank(request));
    }

    @GetMapping("/all-names")
    public ResponseEntity<List<String>> getAllBankNames() {
        List<String> bankNames = bankService.getAllBankNames();
        return ResponseEntity.ok(bankNames);
    }
}
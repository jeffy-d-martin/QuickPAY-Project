package quickpay_backend.controller;

import quickpay_backend.dto.ConnectBankRequest;
import quickpay_backend.model.BankUser;
import quickpay_backend.service.BankUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/bank-user")
public class BankUserController {

    private final BankUserService bankUserService;

    @PostMapping("/connect-bank")
    public ResponseEntity<BankUser> connectBankAndSetPin(@RequestBody ConnectBankRequest request) {
        BankUser linkedAccount = bankUserService.connectBankAndSetPin(request);
        return ResponseEntity.ok(linkedAccount);
    }

    @GetMapping("/details/{phoneNo}")
    public ResponseEntity<BankUser> getBankUserDetails(@PathVariable String phoneNo) {
        BankUser bankUser = bankUserService.getBankUserDetails(phoneNo);
        if (bankUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankUser);
    }

    @GetMapping("/balance/{phoneNo}")
    public ResponseEntity<quickpay_backend.dto.BankBalanceResponse> getBankBalance(@PathVariable String phoneNo) {
        quickpay_backend.dto.BankBalanceResponse response = bankUserService.getBankUserBalance(phoneNo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-balance")
    public ResponseEntity<?> checkBalanceWithPin(@RequestBody quickpay_backend.dto.CheckBalanceRequest request) {
        try {
            quickpay_backend.dto.BankBalanceResponse response = bankUserService.verifyPinAndGetBalance(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Map.of("message", e.getMessage() != null ? e.getMessage() : "Invalid 4-Digit UPI PIN!"));
        }
    }
}
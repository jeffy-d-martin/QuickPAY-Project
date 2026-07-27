package fakepay_backend.controller;

import fakepay_backend.dto.TransactionRequest;
import fakepay_backend.model.Transaction;
import fakepay_backend.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransactionRequest request) {
        try {
            Transaction tx = transactionService.processTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(tx);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("message", e.getMessage() != null ? e.getMessage() : "Transfer failed. Please try again."));
        }
    }

    @GetMapping("/history/{phoneNo}")
    public ResponseEntity<java.util.List<Transaction>> getUserTransactions(@PathVariable String phoneNo) {
        return ResponseEntity.ok(transactionService.getUserTransactions(phoneNo));
    }
}
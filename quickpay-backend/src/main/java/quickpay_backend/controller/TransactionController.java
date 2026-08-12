package quickpay_backend.controller;

import quickpay_backend.dto.TransactionRequest;
import quickpay_backend.model.Transaction;
import quickpay_backend.service.TransactionService;
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

    @GetMapping("/chain/{phoneNo}")
    public ResponseEntity<java.util.List<Transaction>> getTransactionChain(@PathVariable String phoneNo) {
        return ResponseEntity.ok(transactionService.getUserTransactions(phoneNo));
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/verify/{phoneNo}")
    public ResponseEntity<quickpay_backend.dto.BlockchainVerificationResponse> verifyChain(@PathVariable String phoneNo) {
        return ResponseEntity.ok(transactionService.verifyBlockchainChain(phoneNo));
    }

    @PostMapping("/verify-list")
    public ResponseEntity<quickpay_backend.dto.BlockchainVerificationResponse> verifyList(@RequestBody java.util.List<Transaction> transactions) {
        return ResponseEntity.ok(transactionService.verifyTransactionListIntegrity(transactions));
    }

    @GetMapping("/recalculate-verify/{phoneNo}")
    public ResponseEntity<quickpay_backend.dto.BlockchainVerificationResponse> recalculateAndVerifyChain(@PathVariable String phoneNo) {
        return ResponseEntity.ok(transactionService.recalculateAndVerifyChain(phoneNo));
    }

    @PostMapping("/recalculate-block")
    public ResponseEntity<quickpay_backend.dto.BlockchainVerificationResponse.BlockVerificationResult> recalculateSingleBlock(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.recalculateAndVerifySingleBlock(transaction));
    }
}
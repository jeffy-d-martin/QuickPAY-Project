package fakepay_backend.service;

import fakepay_backend.dto.TransactionRequest;
import fakepay_backend.exception.UserNotFoundException;
import fakepay_backend.model.Bank;
import fakepay_backend.model.BankUser;
import fakepay_backend.model.Transaction;
import fakepay_backend.repository.BankRepository;
import fakepay_backend.repository.BankUserRepository;
import fakepay_backend.repository.TransactionRepository;
import fakepay_backend.util.JeffyEncryptionUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionService {

    private final BankUserRepository bankUserRepository;
    private final BankRepository bankRepository; // Added BankRepository
    private final TransactionRepository transactionRepository;
    private final JeffyEncryptionUtil jeffyEncryptionUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Transaction processTransaction(TransactionRequest request) {
        String cleanSenderPhone = request.getSenderPhone() != null ? request.getSenderPhone().replaceAll("\\D", "") : "";
        String cleanReceiverPhone = request.getReceiverPhone() != null ? request.getReceiverPhone().replaceAll("\\D", "") : "";

        // 1. Fetch Sender and Receiver BankUsers
        BankUser sender = bankUserRepository.findByUserPhoneNumber(cleanSenderPhone)
                .orElseGet(() -> bankUserRepository.findByUserPhoneNumber(request.getSenderPhone())
                .orElseThrow(() -> new UserNotFoundException("Sender bank account not found for phone: " + request.getSenderPhone())));

        BankUser receiver = bankUserRepository.findByUserPhoneNumber(cleanReceiverPhone)
                .orElseGet(() -> bankUserRepository.findByUserPhoneNumber(request.getReceiverPhone())
                .orElseThrow(() -> new UserNotFoundException("Receiver user not found with phone: " + request.getReceiverPhone())));

        // 2. Fetch Sender & Receiver Bank names safely
        String senderBankName = request.getSenderBankName();
        if (sender.getUpiId() != null) {
            senderBankName = bankRepository.findByBankId(sender.getUpiId())
                    .map(Bank::getBankName)
                    .orElse(senderBankName != null ? senderBankName : "State Bank of India");
        } else if (senderBankName == null || senderBankName.isEmpty()) {
            senderBankName = "State Bank of India";
        }

        String receiverBankName = request.getReceiverBankName();
        if (receiver.getUpiId() != null) {
            receiverBankName = bankRepository.findByBankId(receiver.getUpiId())
                    .map(Bank::getBankName)
                    .orElse(receiverBankName != null ? receiverBankName : "HDFC Bank");
        } else if (receiverBankName == null || receiverBankName.isEmpty() || "Bank".equalsIgnoreCase(receiverBankName)) {
            receiverBankName = "HDFC Bank";
        }

        // 3. Validate PIN & Balance
        if (sender.getUpiPin() == null || !passwordEncoder.matches(request.getUpiPin(), sender.getUpiPin())) {
            throw new IllegalArgumentException("Invalid 4-digit UPI PIN! Access denied.");
        }

        if (sender.getBalance() < request.getMoney()) {
            throw new IllegalArgumentException(String.format("Insufficient Bank Balance! Available balance is ₹%.2f, but requested transfer is ₹%.2f.", sender.getBalance(), request.getMoney()));
        }

        LocalDateTime now = LocalDateTime.now();

        // 4. Generate Hash
        String blockId = jeffyEncryptionUtil.generateBlockHash(
                request.getMoney(), cleanReceiverPhone, cleanSenderPhone, now.toString()
        );

        // 5. Build Transaction Node
        Transaction newTx = new Transaction();
        newTx.setBlockId(blockId);

        newTx.setSenderPhone(cleanSenderPhone);
        newTx.setSenderBankName(senderBankName);

        newTx.setReceiverPhone(cleanReceiverPhone);
        newTx.setReceiverBankName(receiverBankName);

        newTx.setMoney(request.getMoney());
        newTx.setTime(now);
        newTx.setUpcomingBlockId(null);

        // 6. Handle Blockchain Links
        if (sender.getEndingBlockHashId() == null) {
            newTx.setPrevBlockId(null);
            sender.setStartingBlockHashId(blockId);
        } else {
            String prevBlockId = sender.getEndingBlockHashId();
            newTx.setPrevBlockId(prevBlockId);

            transactionRepository.findByBlockId(prevBlockId).ifPresent(prevTx -> {
                prevTx.setUpcomingBlockId(blockId);
                transactionRepository.save(prevTx);
            });
        }

        // 7. Save Updates
        sender.setBalance(sender.getBalance() - request.getMoney());
        receiver.setBalance(receiver.getBalance() + request.getMoney());
        sender.setEndingBlockHashId(blockId);

        bankUserRepository.save(sender);
        bankUserRepository.save(receiver);

        return transactionRepository.save(newTx);
    }

    public java.util.List<Transaction> getUserTransactions(String phoneNo) {
        if (phoneNo == null || phoneNo.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        String cleanPhone = phoneNo.replaceAll("\\D", "");
        java.util.List<Transaction> list = transactionRepository.findBySenderPhoneOrReceiverPhoneOrderByTimeDesc(cleanPhone, cleanPhone);
        if ((list == null || list.isEmpty()) && !cleanPhone.equals(phoneNo)) {
            list = transactionRepository.findBySenderPhoneOrReceiverPhoneOrderByTimeDesc(phoneNo, phoneNo);
        }
        return list != null ? list : java.util.Collections.emptyList();
    }
}
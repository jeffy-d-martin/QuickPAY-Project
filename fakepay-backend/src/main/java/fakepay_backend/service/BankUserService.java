package fakepay_backend.service;

import fakepay_backend.dto.ConnectBankRequest;
import fakepay_backend.exception.UserNotFoundException;
import fakepay_backend.model.Bank;
import fakepay_backend.model.BankUser;
import fakepay_backend.model.User;
import fakepay_backend.repository.BankRepository;
import fakepay_backend.repository.BankUserRepository;
import fakepay_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankUserService {

    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final BankUserRepository bankUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Connects a User to the Bank and establishes their UPI ID & Hashed UPI PIN
     */
    public BankUser connectBankAndSetPin(ConnectBankRequest request) {
        // 1. Verify user exists in FakePay core system
        User fakePayUser = userRepository.findByPhoneNo(request.getPhoneNo())
                .orElseThrow(() -> new UserNotFoundException("FakePay user not found with phone: " + request.getPhoneNo()));

        // 2. Search for Bank by Bank Name
        Bank bank = bankRepository.findByBankName(request.getBankName())
                .orElseThrow(() -> new UserNotFoundException("Bank '" + request.getBankName() + "' not found!"));

        // 3. Find existing BankUser record or create a new linked record
        BankUser bankUser = bankUserRepository.findByUserPhoneNumber(request.getPhoneNo())
                .orElseGet(() -> {
                    BankUser newUser = new BankUser();
                    newUser.setUserName(fakePayUser.getUserName());
                    newUser.setUserPhoneNumber(fakePayUser.getPhoneNo());
                    newUser.setBalance(5000.0); // Default bank opening balance
                    return newUser;
                });

        // 4. Connect Bank UPI ID and hash the newly created UPI PIN
        bankUser.setUpiId(bank.getBankId()); // Links Bank_ID as the upiId
        bankUser.setUpiPin(passwordEncoder.encode(request.getUpiPin()));

        return bankUserRepository.save(bankUser);
    }

    public BankUser getBankUserDetails(String phoneNo) {
        if (phoneNo == null || phoneNo.trim().isEmpty()) return null;
        String cleanPhone = phoneNo.replaceAll("\\D", "");
        BankUser user = bankUserRepository.findByUserPhoneNumber(cleanPhone).orElse(null);
        if (user == null && !cleanPhone.equals(phoneNo)) {
            user = bankUserRepository.findByUserPhoneNumber(phoneNo).orElse(null);
        }
        return user;
    }

    public fakepay_backend.dto.BankBalanceResponse getBankUserBalance(String phoneNo) {
        if (phoneNo == null || phoneNo.trim().isEmpty()) {
            throw new UserNotFoundException("Phone number is required");
        }
        String cleanPhone = phoneNo.replaceAll("\\D", "");
        BankUser bankUser = bankUserRepository.findByUserPhoneNumber(cleanPhone)
                .orElseGet(() -> bankUserRepository.findByUserPhoneNumber(phoneNo)
                .orElseThrow(() -> new UserNotFoundException("Bank user not found with phone: " + phoneNo)));

        String bankName = bankUser.getUpiId() != null ? bankUser.getUpiId() : "Primary Bank Account";
        return new fakepay_backend.dto.BankBalanceResponse(bankUser.getUserPhoneNumber(), bankName, bankUser.getBalance());
    }

    public fakepay_backend.dto.BankBalanceResponse verifyPinAndGetBalance(fakepay_backend.dto.CheckBalanceRequest request) {
        if (request.getPhoneNo() == null || request.getPhoneNo().trim().isEmpty()) {
            throw new UserNotFoundException("Phone number is required");
        }
        if (request.getUpiPin() == null || request.getUpiPin().trim().isEmpty()) {
            throw new IllegalArgumentException("UPI PIN is required!");
        }

        String cleanPhone = request.getPhoneNo().replaceAll("\\D", "");
        BankUser bankUser = bankUserRepository.findByUserPhoneNumber(cleanPhone)
                .orElseGet(() -> bankUserRepository.findByUserPhoneNumber(request.getPhoneNo())
                .orElseThrow(() -> new UserNotFoundException("Bank user not found with phone: " + request.getPhoneNo())));

        if (!passwordEncoder.matches(request.getUpiPin(), bankUser.getUpiPin())) {
            throw new IllegalArgumentException("Invalid 4-digit UPI PIN! Access denied.");
        }

        String bankName = bankUser.getUpiId() != null ? bankUser.getUpiId() : "Primary Bank Account";
        return new fakepay_backend.dto.BankBalanceResponse(bankUser.getUserPhoneNumber(), bankName, bankUser.getBalance());
    }
}
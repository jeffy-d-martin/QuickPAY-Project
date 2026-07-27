package fakepay_backend.service;

import fakepay_backend.dto.BankLoginRequest;
import fakepay_backend.dto.BankSignUpRequest;
import fakepay_backend.exception.UserAlreadyExistsException;
import fakepay_backend.exception.UserNotFoundException;
import fakepay_backend.model.Bank;
import fakepay_backend.repository.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final PasswordEncoder passwordEncoder;

    public Bank signUpBank(BankSignUpRequest request) {
        bankRepository.findByBankId(request.getBankId()).ifPresent(b -> {
            throw new UserAlreadyExistsException("Bank with ID " + request.getBankId() + " already exists!");
        });

        Bank bank = new Bank();
        bank.setBankId(request.getBankId());
        bank.setBankName(request.getBankName());
        bank.setEmail(request.getEmail());
        bank.setUpiId(request.getUpiId());

        // Encrypt the bank admin password before saving
        bank.setPassword(passwordEncoder.encode(request.getPassword()));

        return bankRepository.save(bank);
    }

    /**
     * Authenticates a Bank using bankId and raw password.
     */
    public Bank loginBank(BankLoginRequest request) {
        // Find bank by upiId (or bankId depending on your repository method name)
        Bank bank = bankRepository.findByUpiId(request.getUpiId())
                .orElseThrow(() -> new UserNotFoundException("Bank with UPI ID " + request.getUpiId() + " not found!"));

        // Validate the password
        if (!passwordEncoder.matches(request.getPassword(), bank.getPassword())) {
            throw new RuntimeException("Invalid UPI ID or Password!");
        }

        return bank;
    }

    /**
     * Fetches a list of all bank names in the collection for dropdown selection
     */
    public List<String> getAllBankNames() {
        return bankRepository.findAll()
                .stream()
                .map(Bank::getBankName)
                .collect(Collectors.toList());
    }
}
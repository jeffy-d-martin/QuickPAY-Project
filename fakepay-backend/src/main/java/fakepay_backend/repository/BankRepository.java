package fakepay_backend.repository;

import fakepay_backend.model.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {

    // 1. If you also search by bankId:
    Optional<Bank> findByBankId(String bankId);

    // 2. Add this line to fix the error in BankService:
    Optional<Bank> findByUpiId(String upiId);

    // 3. Optional (if searching banks by name):
    Optional<Bank> findByBankName(String bankName);
}
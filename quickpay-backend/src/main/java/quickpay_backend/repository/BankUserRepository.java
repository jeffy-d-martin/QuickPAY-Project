package quickpay_backend.repository;

import quickpay_backend.model.BankUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface BankUserRepository extends MongoRepository<BankUser, String> {
    Optional<BankUser> findByUserPhoneNumber(String userPhoneNumber);

}
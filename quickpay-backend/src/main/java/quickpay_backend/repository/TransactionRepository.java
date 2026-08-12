package quickpay_backend.repository;

import quickpay_backend.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Optional<Transaction> findByBlockId(String blockId);
    List<Transaction> findBySenderPhoneOrReceiverPhoneOrderByTimeDesc(String senderPhone, String receiverPhone);
}
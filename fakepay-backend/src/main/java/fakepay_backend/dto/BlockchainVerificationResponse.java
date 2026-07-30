package fakepay_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainVerificationResponse {
    private boolean isValidChain;
    private int totalBlocks;
    private int verifiedBlocks;
    private int tamperedBlocksCount;
    private String statusMessage;
    private List<BlockVerificationResult> blockResults;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlockVerificationResult {
        private String blockId;
        private int blockIndex;
        private boolean isValid;
        private String expectedHash;
        private String actualHash;
        private boolean isPrevHashValid;
        private String expectedPrevHash;
        private String actualPrevHash;
        private String errorMessage;
        private double money;
        private String senderPhone;
        private String receiverPhone;
        private String time;
    }
}

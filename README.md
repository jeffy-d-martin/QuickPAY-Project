# ⚡ QuickPAY - Blockchain-Powered UPI Payment Engine

QuickPAY is a full-stack payment processing application modeled after modern UPI platforms like Google Pay. Beyond standard payment workflows, QuickPAY incorporates a custom-built, linked-list cryptographic blockchain engine designed to audit, detect, and flag database record tampering in real time.

---

## 🚀 Key Features

* **Instant Payment Processing:** Simulates real-time UPI transfers including user registration, bank account linking, and transaction history.
* **Cryptographic Block Hashing:** Generates unique transaction block IDs dynamically based on transaction parameters (`timestamp + senderPhone + receiverPhone + amount`).
* **Linked-List Blockchain Ledger:** Each transaction node maintains a reference pointer (`prevBlockId`) to its predecessor, creating an immutable cryptographic chain.
* **Live Tamper Inspection Console:** Interactive diagnostic dashboard that recalculates block hash formulas on the fly and immediately flags altered database records with granular diagnostic reports.

---

## 🛠️ Tech Stack

* **Backend:** Java 17+, Spring Boot 3, REST APIs
* **Database:** MongoDB
* **Frontend:** HTML5, CSS3, JavaScript (Vanilla ES6+)
* **Core Concepts:** Linked List Data Structures, Cryptographic Hashing, NoSQL Document Storage, Tamper Detection Logic

---

## ⚙️ How the Blockchain Verification Works

1. **Block Creation:** When a payment is made, a block is instantiated with the transaction details and linked to the previous block's hash.
2. **Hash Generation:** A hash formula calculates the block ID based on the transaction metadata.
3. **Integrity Audit:** When inspecting a user's ledger, the system recalculates the block ID using stored record values and compares it against the original hash.
4. **Tamper Detection:** If a database value (e.g., transaction amount) is manually altered in MongoDB, the live formula produces a mismatch, immediately triggering a visual tampering alert.

---

## 💻 Getting Started

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* Maven 3.8+
* MongoDB running locally (`localhost:27017`) or a MongoDB Atlas URI

### Installation & Run

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/jeffy-d-martin/QuickPAY-Project.git](https://github.com/jeffy-d-martin/QuickPAY-Project.git)
   cd QuickPAY-Project

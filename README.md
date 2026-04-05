```markdown
# Finance Data Processing and Access Control System

A full-stack Finance Dashboard system featuring a robust Spring Boot backend and an interactive Thymeleaf frontend. This application demonstrates secure role-based access control (RBAC), real-time session tracking, advanced dynamic data filtering, and on-the-fly financial analytics.

---

## 🚀 Features

### 1. User & Role Management (Access Control)
* **Role-Based Access Control (RBAC):** Leverages Spring Security to strictly enforce user permissions at both the API level and the UI layer.
    * `ADMIN`: Full management access. Can create, update, delete records, and view active users.
    * `ANALYST`: Can view records and access advanced analytics/summaries.
    * `VIEWER`: Read-only access restricted strictly to viewing table data.
* **Live Session Monitoring:** Admins can view a real-time list of active users directly in the dashboard using Spring's `SessionRegistry`.

### 2. Financial Records Management
* **Complete CRUD Operations:** Create, Read, Update, and Delete actions bound to specific authorization roles.
* **Multi-Criteria Table Filtering:** Instant frontend JavaScript filtering allowing users to cross-reference data by **Category**, **Type** (Income/Expense), and **Date** simultaneously.
* **Precision Currency Mapping:** Uses Java's `BigDecimal` to process precise ledger math, protecting floating-point accuracy.

### 3. Dashboard Summary & Analytics
* **Math Summaries:** Real-time generation of Total Income, Total Expenses, and net cash balance.
* **Data Aggregation:** Custom JPQL and Native queries to compute category-wise expenditure and track monthly cash flows.
* **Interactive Graphics:** Embedded Chart.js rendering to convert processed statistics into visually digestible circular breakups and bar grids.
* **Recent Operations Feed:** A log representing the last 5 operations processed to keep accounting streams transparent.

---

## 🛠️ Tech Stack

* **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
* **Frontend:** HTML5, CSS3, Thymeleaf Templating, Chart.js
* **Database:** H2 Database (In-Memory for zero-configuration setup)

---

## 📂 Project Structure

```text
src/main/java/com/example/finance/
│
├── controller/
│   └── FinanceController.java     # Handles mapped endpoints and security registry
├── model/
│   ├── FinancialRecord.java       # DB Entity mapped for currency calculations
│   └── TransactionType.java       # Enum declaring INCOME / EXPENSE bound parameters
├── repository/
│   └── FinancialRecordRepository.java # JPA interface containing native & aggregation queries
└── service/
    └── FinanceService.java        # Core business algorithms and analytics computation
```

---

## ⚙️ Setup and Installation

### Prerequisites
* **Java Development Kit (JDK) 17** or higher.
* **Maven** installed (or you can use the provided `./mvnw` wrapper).

### Steps to Run
1. **Clone the repository:**
   *(Note: Remember to replace `YOUR_USERNAME` and `YOUR_REPOSITORY_NAME` with your actual GitHub details!)*
   ```bash
   git clone [https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git)
   cd YOUR_REPOSITORY_NAME
   ```
2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Access the application:**
   Open your browser and navigate to `http://localhost:8080`

### 🔑 Mock Credentials
To evaluate the access control gates, the system is pre-configured with the following local users:
* **Admin:** `admin` / `admin123`
* **Analyst:** `analyst` / `analyst123`
* **Viewer:** `viewer` / `viewer123`

---

## 🔌 API Endpoints

### CRUD Operations
* `GET /api/finance/records` - Retrieves all stored financial entries.
* `POST /api/finance/records` - Creates a new financial ledger. *(Admin only)*
* `PUT /api/finance/records/{id}` - Updates an existing entry. *(Admin only)*
* `DELETE /api/finance/records/{id}` - Drops a record permanently. *(Admin only)*

### Metrics & Analytics
* `GET /api/finance/summary` - Computes total income, expenses, clean balance, and gathers recent active arrays. *(Admin & Analyst only)*
* `GET /api/finance/analytics/category` - Groups spending datasets tailored for Chart.js rendering. *(Admin & Analyst only)*
* `GET /api/finance/active-users` - Tracks users mapped to the security session registry. *(Admin only)*

---

## 🧠 Assumptions and Design Decisions

1. **H2 Database:** An in-memory database was adopted to ensure zero setup overhead for evaluation. If shipping to production, swapping variables in the `application.properties` to connect to PostgreSQL or MySQL can fulfill persistent storage.
2. **Thymeleaf + Security Taglibs:** Instead of separating the stack into a rigid Node backend and standalone React build, Thymeleaf was used. This directly allowed passing server-side security evaluations like `sec:authorize="hasRole('...)"` to safely omit dashboard components straight from the initial fetch.
3. **The Viewer UI Experience:** Per security standards, rather than showing empty grid components on the layout for restricted users, standard `VIEWERS` are given a modified Full-Width table layout. Grid column modifications are dictated dynamically through authorized server expressions.
```

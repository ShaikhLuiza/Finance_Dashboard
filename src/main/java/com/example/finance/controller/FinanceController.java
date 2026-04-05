package com.example.finance.controller;

import com.example.finance.model.FinancialRecord;
import com.example.finance.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    // 🆕 Inject the SessionRegistry from SecurityConfig
    @Autowired
    private SessionRegistry sessionRegistry;

    // --- CRUD OPERATIONS ---

    // 1. Create a record
    @PostMapping("/records")
    public ResponseEntity<FinancialRecord> createRecord(@RequestBody FinancialRecord record) {
        return ResponseEntity.ok(financeService.createRecord(record));
    }

    // 2. View all records
    @GetMapping("/records")
    public List<FinancialRecord> getAllRecords() {
        return financeService.getAllRecords();
    }

    // 3. Update a record
    @PutMapping("/records/{id}")
    public ResponseEntity<FinancialRecord> updateRecord(@PathVariable Long id, @RequestBody FinancialRecord record) {
        return ResponseEntity.ok(financeService.updateRecord(id, record));
    }

    // 4. Delete a record
    @DeleteMapping("/records/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id) {
        financeService.deleteRecord(id);
        return ResponseEntity.ok("Record deleted successfully");
    }

    // --- FILTERING ---

    // 5. Filter by Category
    @GetMapping("/records/filter")
    public List<FinancialRecord> filterByCategory(@RequestParam String category) {
        return financeService.filterByCategory(category);
    }

    // --- DASHBOARD & ANALYTICS ENDPOINTS ---

    // 6. Get Expense Totals by Category for the Bar Chart
    @GetMapping("/analytics/category")
    public ResponseEntity<List<Map<String, Object>>> getCategoryAnalytics() {
        return ResponseEntity.ok(financeService.getExpenseTotalsByCategory());
    }

    // 7. Get the math summaries
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        return ResponseEntity.ok(financeService.getDashboardSummary());
    }

    // 🆕 8. Get active users currently logged into the system
    @GetMapping("/active-users")
    public ResponseEntity<List<String>> getActiveUsers() {
        List<String> activeUsers = sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof User)
                .map(principal -> {
                    User user = (User) principal;
                    // Formats as: "admin (ROLE_ADMIN)" or "viewer (ROLE_VIEWER)"
                    return user.getUsername() + " (" + user.getAuthorities().toString().replaceAll("[\\[\\]]", "") + ")";
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(activeUsers);
    }
}
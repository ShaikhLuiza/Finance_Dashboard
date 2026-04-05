package com.example.finance.controller;

import com.example.finance.model.FinancialRecord;
import com.example.finance.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

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

}
package com.example.finance.service;

import com.example.finance.model.FinancialRecord;
import com.example.finance.model.TransactionType;
import com.example.finance.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    @Autowired
    private FinancialRecordRepository repository;

    // --- CRUD OPERATIONS ---
    public FinancialRecord createRecord(FinancialRecord record) {
        return repository.save(record);
    }

    public List<FinancialRecord> getAllRecords() {
        return repository.findAll();
    }

    public FinancialRecord updateRecord(Long id, FinancialRecord updatedRecord) {
        FinancialRecord existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        existing.setAmount(updatedRecord.getAmount());
        existing.setType(updatedRecord.getType());
        existing.setCategory(updatedRecord.getCategory());
        existing.setDate(updatedRecord.getDate());
        existing.setNotes(updatedRecord.getNotes());

        return repository.save(existing);
    }

    // --- ANALYTICS ---
    public List<Map<String, Object>> getExpenseTotalsByCategory() {
        // Changed this to use 'repository' so it matches your Autowired variable!
        return repository.findExpenseTotalsByCategory();
    }

    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    // --- FILTERING ---
    public List<FinancialRecord> filterByCategory(String category) {
        return repository.findByCategory(category);
    }

    // --- DASHBOARD SUMMARY LOGIC (Requirement #3) ---
    public Map<String, Object> getDashboardSummary() {
        List<FinancialRecord> records = repository.findAll();

        // 1. Calculate Total Income
        BigDecimal totalIncome = records.stream()
                .filter(r -> r.getType() == TransactionType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Calculate Total Expenses
        BigDecimal totalExpense = records.stream()
                .filter(r -> r.getType() == TransactionType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Category-wise Totals
        Map<String, BigDecimal> categoryTotals = records.stream()
                .collect(Collectors.groupingBy(
                        FinancialRecord::getCategory,
                        Collectors.mapping(FinancialRecord::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        // Package everything neatly into a Map to send to the frontend
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpense);
        summary.put("netBalance", totalIncome.subtract(totalExpense));
        summary.put("categoryWiseTotals", categoryTotals);

        return summary;
    }
}
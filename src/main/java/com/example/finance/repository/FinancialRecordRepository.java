package com.example.finance.repository;

import com.example.finance.model.FinancialRecord;
import com.example.finance.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // 1. Filter by Category
    List<FinancialRecord> findByCategory(String category);

    // 2. Filter by Date Range (Perfect for weekly/monthly dashboard views)
    List<FinancialRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // 3. Filter by Type (To get only Income or only Expenses)
    List<FinancialRecord> findByType(TransactionType type);

    // 4. Combined Filter: Type AND Date Range
    List<FinancialRecord> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    // 5. Custom query to sum up expenses grouped by category for analytics
    // Using Map<String, Object> allows us to bypass creating a separate DTO file!
    @Query("SELECT r.category as category, SUM(r.amount) as total FROM FinancialRecord r WHERE r.type = 'EXPENSE' GROUP BY r.category")
    List<Map<String, Object>> findExpenseTotalsByCategory();
}
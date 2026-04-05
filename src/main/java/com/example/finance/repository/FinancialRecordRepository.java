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
    @Query("SELECT r.category as category, SUM(r.amount) as total FROM FinancialRecord r WHERE r.type = 'EXPENSE' GROUP BY r.category")
    List<Map<String, Object>> findExpenseTotalsByCategory();

    // 🆕 6. Grab the 5 most recent activities based on the record's ID
    List<FinancialRecord> findTop5ByOrderByIdDesc();

    // 🆕 7. Monthly trends query (H2 Database compatible. If using MySQL, change FORMATDATETIME to DATE_FORMAT)
    @Query(value = "SELECT FORMATDATETIME(date, 'yyyy-MM') as month, type as type, SUM(amount) as total FROM financial_records GROUP BY month, type ORDER BY month ASC", nativeQuery = true)
    List<Map<String, Object>> getMonthlyTrends();
}
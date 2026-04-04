package com.example.finance.repository; // If IntelliJ complains, change this to com.example.finance.repository;

import com.example.finance.model.FinancialRecord;
import com.example.finance.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}
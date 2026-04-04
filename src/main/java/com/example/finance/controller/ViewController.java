package com.example.finance.controller;

import com.example.finance.repository.FinancialRecordRepository; // 1. Make sure this matches your actual repository package!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // 2. Inject your actual database repository
    @Autowired
    private FinancialRecordRepository recordRepository;

    @GetMapping("/")
    public String showLandingPage() {
        return "index"; // Shows the landing page with 3 buttons
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Points directly to login.html
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 3. Fetch real data from H2 database and pass it to Thymeleaf
        model.addAttribute("records", recordRepository.findAll());

        return "dashboard"; // Shows the professional table with a Logout button!
    }
}
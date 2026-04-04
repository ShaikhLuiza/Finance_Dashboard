package com.example.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // NEW: Whitelisted the dashboard URL!
                        .requestMatchers("/dashboard").hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        .requestMatchers("/api/finance/records").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers("/api/finance/summary").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers("/api/finance/records/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                // FIXED: Changed from withDefaults() to explicit success routing
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails viewer = User.builder()
                .username("viewer")
                .password("{noop}iew-Only-Pass-00")
                .roles("VIEWER")
                .build();

        UserDetails analyst = User.builder()
                .username("analyst")
                .password("{noop}Read-Data-Trend-88")
                .roles("ANALYST")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}Admin-Secure-99!")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(viewer, analyst, admin);
    }
}
package com.example.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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

                        .requestMatchers("/dashboard").hasAnyRole("VIEWER", "ANALYST", "ADMIN")

                        .requestMatchers("/api/finance/records").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .requestMatchers("/api/finance/summary").hasAnyRole("ANALYST", "ADMIN")

                        // 🆕 Added role restriction for the active viewers list
                        .requestMatchers("/api/finance/active-users").hasRole("ADMIN")

                        .requestMatchers("/api/finance/records/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Specifies the logout trigger URL
                        .logoutSuccessUrl("/") // Redirects to home or login page after logout
                        .invalidateHttpSession(true) // 🆕 Destroys the HTTP session
                        .clearAuthentication(true) // 🆕 Clears the security context
                        .deleteCookies("JSESSIONID") // 🆕 Wipes the session cookie
                        .permitAll()
                )

                // 🆕 Added session management to hook up the session registry
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry())
                );

        return http.build();
    }

    // 🆕 Keeps track of active sessions
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 🆕 Broadcasts login/logout events to destroy or create sessions
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails viewer = User.builder()
                .username("viewer")
                .password("{noop}View-Only-Pass-00")
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
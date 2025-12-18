package com.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class DigitalLibConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Jauthfilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
        		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        // Public endpoints
                        .requestMatchers("/user/add").permitAll()
                        .requestMatchers("/book/search/**").permitAll()
                        .requestMatchers("/book/getall").permitAll()
                        .requestMatchers("/transaction/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/transaction/borrow/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/transaction/return/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/user/getone/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user/login").permitAll()
                        // Admin-only endpoints
                        .requestMatchers("/book/add", "/book/update/**", "/book/delete/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/user/getall",
                                "/user/update/**",
                                "/user/delete/**",
                                "/user/suspend/**",
                                "/user/activate/**",
                                "/user/status/**",
                                "/user/active",
                                "/user/count/**"
                        ).hasRole("ADMIN")

                        .requestMatchers("/transaction/getall").hasRole("ADMIN")
                        .requestMatchers("/transaction/book/**").hasRole("ADMIN")
                        .requestMatchers("/transaction/current-borrowed").hasRole("ADMIN")
                        .requestMatchers("/transaction/overdue").hasRole("ADMIN")


                        // User-only endpoints (requires JWT)
                        .requestMatchers("/book/view/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("http://localhost:3000");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}



package com.example.statisticsprice.security;

import com.example.statisticsprice.repo.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableScheduling
@EnableWebSecurity
public class SpringConfig {

    @Bean
    public CustomUserDetailManagerService userDetailsService(PasswordEncoder passwordEncoder, UserRepo userRepo) {

        var service = new CustomUserDetailManagerService(userRepo, passwordEncoder);
        if(userRepo.findByUsername("user").isEmpty())
        {
            service.addUser("user", "password");
        }

        return service;
    }

    @Bean
    public AuthProvider authProvider(CustomUserDetailManagerService userDetailsService, PasswordEncoder passwordEncoder) {
        return new AuthProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/static/css/**", "/js/**", "/login", "/register", "/error").permitAll()
                        .anyRequest().hasRole("USER")
                )
                .formLogin(login ->
                        login.defaultSuccessUrl("/products", true)
                                .loginPage("/login")

                ).logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/login") )
//                .httpBasic(Customizer.withDefaults())
        /*.formLogin(login -> login.loginPage("/login")
                        .successForwardUrl("/").permitAll()

                )*/;
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}

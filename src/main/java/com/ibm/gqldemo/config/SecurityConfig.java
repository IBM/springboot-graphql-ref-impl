package com.ibm.gqldemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "authentication", name = "enabled", havingValue = "true")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/actuator").permitAll()
                        .requestMatchers("/actuator/*").permitAll()
                        .requestMatchers(toH2Console()).permitAll()
                        .anyRequest().authenticated();
                })
                .httpBasic(withDefaults())
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails adminUser = User.withDefaultPasswordEncoder()
                .username("admin")
                .roles("admin")
                .password("password123")
                .build();

        UserDetails regularUser = User.withDefaultPasswordEncoder()
                .username("user")
                .roles("readonly")
                .password("password")
                .build();

        return new InMemoryUserDetailsManager(adminUser, regularUser);

    }
}

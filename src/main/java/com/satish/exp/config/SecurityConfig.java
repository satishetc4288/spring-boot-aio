package com.satish.exp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
                http
                                .authorizeExchange(exchanges -> exchanges
                                                .anyExchange().permitAll())
                                // .oauth2ResourceServer(oauth2 -> oauth2
                                // .jwt(withDefaults()))
                                .csrf(csrf -> csrf.disable()); // Disable CSRF for stateless APIs

                return http.build();
        }
}

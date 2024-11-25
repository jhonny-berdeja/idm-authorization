package com.jberdeja.idm_authorization.security;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Autowired
    CsrfCookieFilter csrfCookieFilter;

    @Bean
    SecurityFilterChain securityFilterChaim(
                                                HttpSecurity http
                                                , JWTValidationFilter jwtValidationFilter
                                            ) throws Exception{

        http.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        var requestHandler = new CsrfTokenRequestAttributeHandler();

        http.authorizeHttpRequests(auth -> 
            auth
            .requestMatchers("/cards").hasRole("CARDS"));

        http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);

        http.cors(cors->corsConfigurationSource());

        http.csrf(csrf->csrf.csrfTokenRequestHandler(requestHandler)
        ).addFilterAfter(csrfCookieFilter,  BasicAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        var admin = User.withUsername("admin")
        .password("to_be_encoded")
        .authorities("ROLE_CARDS")
        .build();

        var user = User.withUsername("user")
        .password("to_be_encoded")
        .authorities("ROLE_CARDS")
        .build();

        return new InMemoryUserDetailsManager(admin, user);
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    } 

    CorsConfigurationSource corsConfigurationSource(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{

        return configuration.getAuthenticationManager();

    }
}

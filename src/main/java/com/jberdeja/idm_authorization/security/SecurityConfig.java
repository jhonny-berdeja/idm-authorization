package com.jberdeja.idm_authorization.security;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
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
    private static final String ROUTE_CARDS = "/cards";
    private static final String ROLE_CARDS = "CARDS";
    @Autowired
    CsrfCookieFilter csrfCookieFilter;

    @Bean
    SecurityFilterChain securityFilterChaim(
                                                HttpSecurity http
                                                , JWTValidationFilter jwtValidationFilter
                                            ) throws Exception{
        http.sessionManagement(sess-> sessionManagementConfigurer(sess));
        http.authorizeHttpRequests(auth -> authorizeHttpRequestsConfigurer(auth));
        http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);
        http.cors(cors->corsConfigurationSource());
        http.csrf(csrf->csrfConfigurer(csrf))
                        .addFilterAfter(csrfCookieFilter,  BasicAuthenticationFilter.class);
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
        var config = buildCorsConfiguration();
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    private CorsConfiguration buildCorsConfiguration(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        return config;
    }
    @SuppressWarnings("rawtypes")
    private CsrfConfigurer csrfConfigurer(CsrfConfigurer<HttpSecurity> httpSecurity){
        return httpSecurity.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
    }
        
    @SuppressWarnings("rawtypes")
    private AuthorizationManagerRequestMatcherRegistry authorizeHttpRequestsConfigurer( 
                            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        return auth.requestMatchers(ROUTE_CARDS).hasRole(ROLE_CARDS);
    }
        
    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer sessionManagementConfigurer(SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer){
        return sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

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
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROUTE_CREATE_USER = "/create-user";
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
        http.cors(cors->corsConfigurationSource(cors));
        http.csrf(csrf->csrfConfigurer(csrf))
                        .addFilterAfter(csrfCookieFilter,  BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    } 

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    CorsConfigurationSource corsConfigurationSource(CorsConfigurer<HttpSecurity> httpSecurity){
        var config = buildCorsConfiguration();
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        httpSecurity.configurationSource(source);
        return source;
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
        httpSecurity.csrfTokenRepository(withHttpOnlyTrue());
        httpSecurity.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
        return httpSecurity;
    }

    @SuppressWarnings("deprecation")
    public CookieCsrfTokenRepository withHttpOnlyTrue() {
        CookieCsrfTokenRepository result = new CookieCsrfTokenRepository();
        result.setCookieHttpOnly(true);
        return result;
    }
        
    @SuppressWarnings("rawtypes")
    private AuthorizationManagerRequestMatcherRegistry authorizeHttpRequestsConfigurer( 
                            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        return auth.requestMatchers("/**").hasRole(ROLE_ADMIN);
    }
        
    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer sessionManagementConfigurer(SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer){
        return sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

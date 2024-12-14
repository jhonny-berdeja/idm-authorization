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
    private static final String ROUTE_GET_APPLICATION = "/get-application/**";
    private static final String ROUTE_CREATE_APPLICATION = "/create-application";
    private static final String ROUTE_GET_APPLICATIONS = "/get-applications";
    private static final String ROUTE_DOCUMENT_ACCESS_MANAGEMENT = "/document-access-management";
    private static final String ROUTE_HELLO = "/hello";
    private static final String POST = "post";
    private static final String GET = "get";
    @Autowired
    CsrfCookieFilter csrfCookieFilter;

    @Bean
    SecurityFilterChain securityFilterChaim(
                                                HttpSecurity http
                                                , JWTValidationFilter jwtValidationFilter
                                            ) throws Exception{
        
        http.sessionManagement(sess-> sessionManagementConfigurer(sess));
        http.authorizeHttpRequests(auth -> authorizeHttpRequestsConfigurer(auth));
        http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);//?
        http.cors(cors->corsConfigurationSource(cors));
        http.csrf(csrf->csrfConfigurer(csrf));
        http.addFilterAfter(csrfCookieFilter,  BasicAuthenticationFilter.class);//?
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    } 

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    CorsConfigurationSource corsConfigurationSource(CorsConfigurer<HttpSecurity> httpSecurity){
        var configCreateUser = buildCorsConfigurationCreateUser();
        var configCreateApplication = buildCorsConfigurationCreateApplication();
        var configGetApplications = buildCorsConfigurationGetApplications();
        var configGetApplication = buildCorsConfigurationGetApplication();
        var configDocumentAccessManagement = buildCorsConfigurationDocumentAccessManagement();
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ROUTE_CREATE_USER, configCreateUser);
        source.registerCorsConfiguration(ROUTE_CREATE_APPLICATION, configCreateApplication);
        source.registerCorsConfiguration(ROUTE_GET_APPLICATIONS, configGetApplications);
        source.registerCorsConfiguration(ROUTE_GET_APPLICATION, configGetApplication);
        source.registerCorsConfiguration(ROUTE_DOCUMENT_ACCESS_MANAGEMENT, configDocumentAccessManagement);
        httpSecurity.configurationSource(source);
        return source;
    }

    private CorsConfiguration buildCorsConfigurationCreateUser(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));// limitar el origen cuando se cree un DNS para el frontend
        config.setAllowedMethods(List.of(POST));
        config.setAllowedHeaders(List.of("*"));//Limitar los headers, debe permitir X-XSRF-TOKEN
        return config;
    }

    private CorsConfiguration buildCorsConfigurationCreateApplication(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));// limitar el origen cuando se cree un DNS para el frontend
        config.setAllowedMethods(List.of(POST));
        config.setAllowedHeaders(List.of("*"));//Limitar los headers, debe permitir X-XSRF-TOKEN
        return config;
    }

    private CorsConfiguration buildCorsConfigurationGetApplications(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));// limitar el origen cuando se cree un DNS para el frontend
        config.setAllowedMethods(List.of(GET));
        config.setAllowedHeaders(List.of("*"));//Limitar los headers, debe permitir X-XSRF-TOKEN
        return config;
    }

    private CorsConfiguration buildCorsConfigurationGetApplication(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));// limitar el origen cuando se cree un DNS para el frontend
        config.setAllowedMethods(List.of(GET));
        config.setAllowedHeaders(List.of("*"));//Limitar los headers, debe permitir X-XSRF-TOKEN
        return config;
    }

    private CorsConfiguration buildCorsConfigurationDocumentAccessManagement(){
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));// limitar el origen cuando se cree un DNS para el frontend
        config.setAllowedMethods(List.of(POST));
        config.setAllowedHeaders(List.of("*"));//Limitar los headers, debe permitir X-XSRF-TOKEN
        return config;
    }

    @SuppressWarnings("rawtypes")
    private CsrfConfigurer csrfConfigurer(CsrfConfigurer<HttpSecurity> httpSecurity){
        httpSecurity.csrfTokenRepository(withHttpOnlyTrue());
        httpSecurity.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
        httpSecurity.ignoringRequestMatchers(ROUTE_HELLO);
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
        auth.requestMatchers(ROUTE_CREATE_USER).hasRole(ROLE_ADMIN);
        auth.requestMatchers(ROUTE_CREATE_APPLICATION).hasRole(ROLE_ADMIN);
        auth.requestMatchers(ROUTE_GET_APPLICATIONS).hasRole(ROLE_ADMIN);
        auth.requestMatchers(ROUTE_GET_APPLICATION).hasRole(ROLE_ADMIN);
        auth.requestMatchers(ROUTE_DOCUMENT_ACCESS_MANAGEMENT).hasRole(ROLE_ADMIN);
        auth.requestMatchers(ROUTE_HELLO).permitAll();
        return auth;
    }
        
    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer sessionManagementConfigurer(SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer){
        return createPolicyToNotSaveTokenInSessions(sessionManagementConfigurer);
    }

    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer createPolicyToNotSaveTokenInSessions(SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer){
        return sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
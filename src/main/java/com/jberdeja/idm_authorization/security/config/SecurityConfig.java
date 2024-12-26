package com.jberdeja.idm_authorization.security.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
import com.jberdeja.idm_authorization.security.filter.CsrfCookieFilter;
import com.jberdeja.idm_authorization.security.filter.JWTFilter;

@Configuration
@EnableWebSecurity
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
    private static final String STRING_NULL = null;

    @Autowired
    CsrfCookieFilter csrfCookieFilter;

/*     Razones para este orden
    1) sessionManagement primero: Asegura que la configuración de la sesión esté establecida antes de que otros filtros la usen.
    2) cors temprano: Garantiza que las solicitudes de origen cruzado sean procesadas correctamente antes de ejecutar los filtros personalizados.
    3) csrf básico antes de los filtros personalizados: Configura las políticas de CSRF antes de manejar los tokens relacionados.
    4) jwtFilter antes de otros filtros: Valida el token JWT y configura el contexto de seguridad para que otros filtros y configuraciones puedan depender de ello.
    5) csrfCookieFilter después de jwtFilter: Garantiza que el token CSRF se maneje después de que el SecurityContext haya sido configurado.
    6) authorizeHttpRequests al final: Define las reglas de autorización después de que todos los filtros hayan procesado la solicitud.
*/
    @Bean
    SecurityFilterChain securityFilterChaim(
                                                HttpSecurity http
                                                , JWTFilter jwtFilter
                                            ) throws Exception{
        
        http.sessionManagement(session-> sessionManagementConfigurer(session));
        http.cors(cors->corsConfigurationSource(cors));
        http.csrf(csrf->csrfConfigurer(csrf));
        http.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(csrfCookieFilter,  BasicAuthenticationFilter.class);
        http.authorizeHttpRequests(authorization -> authorizeHttpRequestsConfigurer(authorization));
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
        config.setAllowedHeaders(List.of("*"));//.of("Authorization", "Content-Type", "X-XSRF-TOKEN"));
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
        httpSecurity.csrfTokenRequestHandler(configureCsrfTokenCreationForPerRequest());
        httpSecurity.ignoringRequestMatchers(ROUTE_HELLO);
        return httpSecurity;
    }

    private CsrfTokenRequestAttributeHandler configureCsrfTokenCreationForPerRequest(){
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        csrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(STRING_NULL);
        return csrfTokenRequestAttributeHandler;
    }

    @SuppressWarnings("deprecation")
    public CookieCsrfTokenRepository withHttpOnlyTrue() {
        CookieCsrfTokenRepository result = new CookieCsrfTokenRepository();
        result.setCookieHttpOnly(true);
        return result;
    }
        
    @SuppressWarnings("rawtypes")
    private AuthorizationManagerRequestMatcherRegistry authorizeHttpRequestsConfigurer( 
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorization 
    ){   
        authorization.requestMatchers(ROUTE_CREATE_USER).hasRole(ROLE_ADMIN);
        authorization.requestMatchers(ROUTE_CREATE_APPLICATION).hasRole(ROLE_ADMIN);
        authorization.requestMatchers(ROUTE_GET_APPLICATIONS).hasRole(ROLE_ADMIN);
        authorization.requestMatchers(ROUTE_GET_APPLICATION).hasRole(ROLE_ADMIN);
        authorization.requestMatchers(ROUTE_DOCUMENT_ACCESS_MANAGEMENT).hasRole(ROLE_ADMIN);
        authorization.requestMatchers(ROUTE_HELLO).permitAll();
        return authorization;
    }
        
    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer sessionManagementConfigurer(
        SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer
    ){
        return disableTokenStorageInSession(sessionManagementConfigurer);
    }

    @SuppressWarnings("rawtypes")
    private SessionManagementConfigurer disableTokenStorageInSession(
        SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer
    ){
        return sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
package com.jberdeja.idm_authorization.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jberdeja.idm_authorization.buider.EntityBuilder;
import com.jberdeja.idm_authorization.config.UnitTestWithPostgrsSql;
import com.jberdeja.idm_authorization.connector.JwtConnector;
import com.jberdeja.idm_authorization.dto.common.UserIdm;
import com.jberdeja.idm_authorization.entity.UserIdmEntity;
import com.jberdeja.idm_authorization.repository.UserIdmRepository;
import com.jberdeja.idm_authorization.service.UserIdmService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc 
public class SecurityConfigTest extends UnitTestWithPostgrsSql{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserIdmService userIdmService;
    @MockitoBean
    private JwtConnector jwtConnector;
    @MockitoBean
    private UserIdmRepository userIdmRepository;

  
    @Test
    void createUser_happyCase_Ok() throws Exception {
        UserIdmEntity userIdmEntity = buildUserIdmEntityWithMockedRepository();
        String claimsResponse = generateClaimsResponse(userIdmEntity.getEmail());
        mockJwtConnector(claimsResponse);
        String requestBody = buildRequestBody();
    
        doNothing().when(userIdmService).createUser(any(UserIdm.class));
        mockMvc.perform(post("/create-user")
                    .header("Authorization", "Bearer test-token")
                    .header("Content-Type", "application/json")
                    .header("X-XSRF-TOKEN", "test-xsrf-token")
                    .header("Cookie", "XSRF-TOKEN=333524f2-ee81-4710-bf61-72c531c44d40")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content(requestBody))
                .andExpect(status().isOk());
    
        verify(userIdmService, times(1)).createUser(any(UserIdm.class));
    }

    
    @Test
    void createUser_withoutAuthorization_Forbidden() throws Exception {

        UserIdmEntity userIdmEntity = buildUserIdmEntityWithMockedRepository();
        String claimsResponse = generateClaimsResponse(userIdmEntity.getEmail());
        mockJwtConnector(claimsResponse);
        String requestBody = buildRequestBody();
        
        mockMvc.perform(post("/create-user")
            .header("Content-Type", "application/json")
            .header("X-XSRF-TOKEN", "test-xsrf-token")
            .header("Cookie", "XSRF-TOKEN=333524f2-ee81-4710-bf61-72c531c44d40")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(requestBody))
            .andExpect(status().isForbidden())
            .andExpect(content().string("error, authorization header is null or blank"));
    }  

     @Test
    void createUser_withoutXXsrfToken_Forbidden() throws Exception {

        UserIdmEntity userIdmEntity = buildUserIdmEntityWithMockedRepository();
        String claimsResponse = generateClaimsResponse(userIdmEntity.getEmail());
        mockJwtConnector(claimsResponse);
        String requestBody = buildRequestBody();
        
        mockMvc.perform(post("/create-user")
            .header("Authorization", "Bearer test-token")
            .header("Content-Type", "application/json")
            .header("Cookie", "XSRF-TOKEN=333524f2-ee81-4710-bf61-72c531c44d40")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(requestBody))
            .andExpect(status().isForbidden())
            .andExpect(content().string("error, x-xsrf-token header is null or blank"));
    }
    
    private UserIdmEntity buildUserIdmEntityWithMockedRepository() {
        UserIdmEntity userIdmEntity = EntityBuilder.buildUserIdmEntity();
        var userIdmEntityFound = Optional.of(userIdmEntity);
        when(userIdmRepository.findByEmail(anyString())).thenReturn(userIdmEntityFound);
        assertThat(userIdmRepository.findByEmail(anyString())).isEqualTo(userIdmEntityFound);
        return userIdmEntity;
    }
    
    private String generateClaimsResponse(String email) {
        ZonedDateTime currentDate = ZonedDateTime.now();
        long iatLong = currentDate.toEpochSecond();
        ZonedDateTime updatedDate = currentDate.plus(30, ChronoUnit.MINUTES);
        long expLong = updatedDate.toEpochSecond();
    
        String iat = String.valueOf(iatLong);
        String exp = String.valueOf(expLong);
    
        return """
            {
                "ROLES": "[ROLE_ADMIN]",
                "sub": "%s",
                "iat": %s,
                "exp": %s
            }
        """.formatted(email, iat, exp);
    }
    
    private void mockJwtConnector(String claimsResponse) {
        when(jwtConnector.requestTokenClaims(anyString())).thenReturn(claimsResponse);
        assertThat(jwtConnector.requestTokenClaims(new String())).isEqualTo(claimsResponse);
    }
    
    private String buildRequestBody() {
        return """
            {
                "userIdm": {
                    "email": "test@example.com",
                    "password": "password123",
                    "roles": ["ROLE_USER"]
                }
            }
        """;
    }

}

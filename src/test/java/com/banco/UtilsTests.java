package com.banco;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.entities.Entity;
import com.banco.entities.EntityType;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.banco.utils.EntityUtils;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UtilsTests {

    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EntityUtils entityUtils;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetEntityInfoOk() throws Exception {
        Entity testUser = Entity.builder()
            .id(null)
            .name("Jesus")
            .taxId("123456789A")
            .address("Mi casa")
            .addressCity("Mi ciudad")
            .addressCountry("Mi pais") 
            .type(EntityType.PHYSICAL)
            .phoneNumber("123 45 67 89")
            .locked(false)
            .emailConfirmed(false)
            .signActivated(false)
            .build();
        when(entityRepository.findByTaxId("123456789A")).thenReturn(Optional.of(testUser));

        Entity foundEntity = entityUtils.getEntityInfo("123456789A");
        Assertions.assertEquals("123 45 67 89", foundEntity.getPhoneNumber());
    }

    @Test
    void testGetEntityInfoFail() throws Exception {
        Assertions.assertThrowsExactly(CustomException.class, () -> {
            entityUtils.getEntityInfo("1234567890A");
        }, "User not found");
    }

    @Test
    void testGetEntityInfoWithCustomNotFoundMessageFail() throws Exception {
        Entity testUser = Entity.builder()
            .id(null)
            .name("Jesus")
            .taxId("123456789A")
            .address("Mi casa")
            .addressCity("Mi ciudad")
            .addressCountry("Mi pais") 
            .type(EntityType.PHYSICAL)
            .phoneNumber("123 45 67 89")
            .locked(false)
            .emailConfirmed(false)
            .signActivated(false)
            .build();
        when(entityRepository.findByTaxId("123456789A")).thenReturn(Optional.of(testUser));

        Assertions.assertThrowsExactly(CustomException.class, () -> {
            CustomException userNotFoundException = new CustomException("TEST-001", "User not found in mocked repository", 404);
            entityUtils.getEntityInfo("1234567890A", userNotFoundException);
        }, "User not found in mocked repository");
    }

    @Test
    void testGetCurrentUserInfoOk() throws Exception {
        // Simulate auth user
        Authentication auth = new UsernamePasswordAuthenticationToken("123456789A", "%Testing12");
        SecurityContextHolder.getContext().setAuthentication(auth); 

        Entity testUser = Entity.builder()
            .id(null)
            .name("Jesus")
            .taxId("123456789A")
            .password("%Testing12")
            .address("Mi casa")
            .addressCity("Mi ciudad")
            .addressCountry("Mi pais") 
            .type(EntityType.PHYSICAL)
            .phoneNumber("123 45 67 89")
            .locked(false)
            .emailConfirmed(false)
            .signActivated(false)
            .build();
        when(entityRepository.findByTaxId("123456789A")).thenReturn(Optional.of(testUser));

        Entity currentUser = entityUtils.getCurrentUserInfo();
        Assertions.assertEquals("123 45 67 89", currentUser.getPhoneNumber());
    }

    @Test
    void testSaveEntityInfoOk() throws Exception {
        Entity testUser = Entity.builder()
            .id(null)
            .name("Jesus")
            .taxId("123456789A")
            .password("%Testing12")
            .address("Mi casa")
            .addressCountry("Mi pais") 
            .type(EntityType.PHYSICAL)
            .phoneNumber("123 45 67 89")
            .locked(false)
            .emailConfirmed(false)
            .signActivated(false)
            .build();
        when(entityRepository.save(any(Entity.class))).thenReturn(testUser);

        Entity userModifications = Entity.builder().addressCity("Mi ciudad").build();
        Assertions.assertEquals("123 45 67 89", entityUtils.saveEntityInfo(userModifications).getPhoneNumber());
    }
    
    
}

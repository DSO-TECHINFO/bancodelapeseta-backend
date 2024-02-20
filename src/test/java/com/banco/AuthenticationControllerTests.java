package com.banco;

import com.banco.dtos.*;

import com.banco.entities.Entity;
import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityGender;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @BeforeEach
    public void configureRepositories(){

    }

    @Test
    public void testLoginOk() throws Exception {


        when(entityRepository.findByTaxId(any())).thenReturn(Optional.of(Entity.builder().lastIpAddress("").userBrowser("").build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(AuthenticationRequestDto.builder().username("123456789A").password("%Testing12").build())))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("token"));
    }

    @Test
    @Transactional
    public void testLoginUserIsLocked() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new LockedException("User is locked"));
        when(entityRepository.findByTaxId(any())).thenReturn(Optional.of(Entity.builder().lastAttempt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))).build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(AuthenticationRequestDto.builder().username("123456789A").password("Testing12").build()))).andExpect(status().isUnauthorized()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("User is locked"));
    }
    @Test
    @Transactional
    public void testLoginUserBadCredentials() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        when(entityRepository.findByTaxId(any())).thenReturn(Optional.of(Entity.builder().loginAttempts((short)0).build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(AuthenticationRequestDto.builder().username("123456789A").password("Testing12").build()))).andExpect(status().isUnauthorized()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("Wrong username or password"));
    }
    @Test
    @Transactional
    public void testLoginUserBadCredentialsLock() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        when(entityRepository.findByTaxId(any())).thenReturn(Optional.of(Entity.builder().loginAttempts((short)3).build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(AuthenticationRequestDto.builder().username("123456789A").password("Testing12").build()))).andExpect(status().isUnauthorized()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("User is locked, try again in: 10 minutes"));
    }

    @Test
    @Transactional
    public void testLoginUserEmailNotConfirmedAndPhoneConfirmed() throws Exception {

        when(authenticationManager.authenticate(any())).thenThrow(new DisabledException("User is disabled"));


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(AuthenticationRequestDto.builder().username("123456789A").password("Testing12").build()))).andExpect(status().isUnauthorized()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("Confirm your email and phone to continue, you can resend email and phone code."));
    }

    @Test
    @Transactional
    public void testRegisterPhysical() throws Exception {
        RegisterPhysicalDto registerPhysicalDto = RegisterPhysicalDto.builder()
                .name("Name")
                .surname("Surname")
                .birthday(new Date())
                .taxId("12345678A")
                .password("%Testing12")
                .nationalIdExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(200)))
                .address("Address")
                .addressAdditionalInfo("Address additional info")
                .postalCode("postal")
                .addressTown("address town")
                .addressCity("address city")
                .addressCountry("address country")
                .gender(EntityGender.M)
                .nationality("nationality")
                .birthCity("birth city")
                .phoneNumber("phone number")
                .email("email")
                .debtType(EntityDebtType.FREELANCE)
                .build();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register/physical")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(registerPhysicalDto)))
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    @Transactional
    public void testRegisterCompany() throws Exception {
        RegisterCompanyDto registerPhysicalDto = RegisterCompanyDto.builder()
                .name("Name")
                .taxId("12345678A")
                .password("%Testing12")
                .address("Address")
                .addressAdditionalInfo("Address additional info")
                .postalCode("postal")
                .addressTown("address town")
                .addressCity("address city")
                .addressCountry("address country")
                .phoneNumber("phone number")
                .email("email")
                .debtType(EntityDebtType.PYME)
                .settingUpDate(new Date())
                .build();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(registerPhysicalDto)))
                .andExpect(status().isCreated()).andReturn();
    }
    @Test
    @WithMockUser
    @Transactional
    public void testChangePasswordIsOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyWithSign(true)
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/password/change").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                .content(TestUtils.asJsonString(PasswordChangeDto.builder().newPassword("testing").signedTransactionCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    @Transactional
    public void testEmailChangeIsOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .emailConfirmed(true)
                        .emailConfirmationCodeAttempts(0)
                        .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmed(true)
                        .phoneConfirmationCodeAttempts(0)
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .sign(passwordEncoder.encode(mockedCode))
                        .signActivated(true)
                        .signAttempts(0)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyWithSign(true)
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/change/email").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(EmailChangeDto.builder().newEmail("testing").sign(mockedCode).phoneCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    @Transactional
    public void testPhoneChangeIsOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .emailConfirmed(true)
                        .emailConfirmationCodeAttempts(0)
                        .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmed(true)
                        .phoneConfirmationCodeAttempts(0)
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .sign(passwordEncoder.encode(mockedCode))
                        .signActivated(true)
                        .signAttempts(0)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyWithSign(true)
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/change/phone").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(PhoneChangeDto.builder().newPhone("testing").sign(mockedCode).emailCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    @Transactional
    public void testCreateSignIsOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyWithSign(true)
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/create/sign").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(SignCreateDto.builder().sign("123456").verificationCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
}

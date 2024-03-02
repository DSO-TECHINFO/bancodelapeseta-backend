package com.banco;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.util.StringInputStream;
import com.banco.dtos.*;

import com.banco.entities.Entity;
import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityGender;
import com.banco.entities.EntityType;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.InputStream;
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
    @MockBean
    private AmazonSimpleEmailService amazonSimpleEmailService;
    @MockBean
    private AmazonS3 s3Client;
    @MockBean
    private SnsClient snsClient;
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
                .content(TestUtils.asJsonString(PasswordChangeDto.builder().newPassword("%Testing12").signedTransactionCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    @Transactional
    public void testChangePasswordDoesNotFitPasswordRequirements() throws Exception {

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


        MvcResult resultMvc = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/password/change").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(PasswordChangeDto.builder().newPassword("1").signedTransactionCode(mockedCode).build())))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(resultMvc.getResponse().getContentAsString().contains("password requirements"));
    }
    @Test
    @Transactional
    public void testRecoveryPasswordIsOk() throws Exception {

        String property = "header<body>body</body>trailer";
        InputStream testInputStream = new StringInputStream(property);
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(testInputStream);
        when(s3Client.getObject((String) any(),any())).thenReturn(s3Object);
        when(amazonSimpleEmailService.sendEmail(any())).thenReturn(null);

        SdkHttpResponse response = SdkHttpResponse.builder().statusCode(HttpStatus.OK.value()).build();
        when(snsClient.publish((PublishRequest) any())).thenReturn((PublishResponse) PublishResponse.builder().sdkHttpResponse(response).build());

        Date date = new Date();
        String taxId = "123456789A";
        String phone = "123123123";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .taxId(taxId)
                        .phoneNumber(phone)
                        .birthday(date)
                        .nationalIdExpiration(date)
                        .type(EntityType.PHYSICAL)
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/recovery/password").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(RecoveryPasswordDto.builder().taxId(taxId).type(EntityType.PHYSICAL).nationalIdExpiration(date).phone(phone).birthday(date).build())))
                .andExpect(status().isOk());

    }
    @Test
    @Transactional
    public void testRecoveryChangePasswordIsOk() throws Exception {

        String mockedCode = "CODETEST";
        String taxId = "123456789A";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .taxId(taxId)
                        .password("ABCD")
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .passwordChangeCode(passwordEncoder.encode(mockedCode))
                        .passwordChangeCodeAttempts(0)
                        .passwordChangeCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/recovery/password/change").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(RecoveryPasswordChangeDto.builder().newPassword("%Testing12").taxId(taxId).recoveryCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
    @Test
    @Transactional
    public void testRecoveryPasswordCheckCode() throws Exception {

        String mockedCode = "CODETEST";
        String taxId = "123456789A";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmationCodeAttempts(0)
                        .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .emailConfirmationCodeAttempts(0)
                        .sign(passwordEncoder.encode(mockedCode))
                        .signActivated(true)
                        .signAttempts(0)
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/recovery/password/check/code").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(RecoveryPasswordCodeInputDto.builder().emailCode(mockedCode).phoneCode(mockedCode).sign(mockedCode).taxId(taxId).build())))
                .andExpect(status().isOk());

    }
    @Test
    @Transactional
    public void testRecoveryChangePasswordVerifyCodeIsWrongBadRequest() throws Exception {

        String mockedCode = "CODETEST";
        String taxId = "123456789A";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .taxId(taxId)
                        .password("ABCD")
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .passwordChangeCode(passwordEncoder.encode(mockedCode))
                        .passwordChangeCodeAttempts(0)
                        .passwordChangeCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/recovery/password/change").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(RecoveryPasswordChangeDto.builder().newPassword("%Testing12").taxId(taxId).recoveryCode("123").build())))
                .andExpect(status().isBadRequest());

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
                        .post("/auth/create/modify/sign").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(SignCreateDto.builder().sign("123456").verificationCode(mockedCode).build())))
                .andExpect(status().isOk());

    }
}

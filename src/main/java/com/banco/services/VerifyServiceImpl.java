package com.banco.services;

import com.banco.dtos.EmailPhoneVerificationDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.dtos.VerificationCodeReturnDto;
import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.utils.EntityUtils;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class VerifyServiceImpl implements VerifyService{

    private final EntityUtils entityUtils;
    private final PasswordEncoder passwordEncoder;

    private final CustomException USER_NOT_FOUND_EXCEPTION = new CustomException("VERIFICATIONS-014", "User not found", 404);

    @Override
    public void verifyEmail(EmailPhoneVerificationDto emailPhoneVerificationDto) throws CustomException {
        Entity entity = entityUtils.getCurrentUserInfo();
        emailCodeCheck(emailPhoneVerificationDto.getCode(), entity);
        entity.setEmailConfirmed(true);
        entity.setEmailConfirmationCode(null);
        entity.setEmailConfirmationCodeAttempts(0);
        entity.setEmailConfirmationCodeExpiration(null);
        entityUtils.saveEntityInfo(entity);

    }
    @Override
    public void verifyPhone(EmailPhoneVerificationDto emailPhoneVerificationDto) throws CustomException {
        String code = emailPhoneVerificationDto.getCode();
        Entity entity = entityUtils.getCurrentUserInfo();
        phoneCodeCheck(code, entity);
        entity.setPhoneConfirmed(true);
        entity.setPhoneConfirmationCode(null);
        entity.setPhoneConfirmationCodeAttempts(0);
        entity.setPhoneConfirmationCodeExpiration(null);
        entityUtils.saveEntityInfo(entity);

    }

    @Override
    public Entity verifyWithEmailCode(String emailCode) throws CustomException {
        Entity entity = entityUtils.getCurrentUserInfo();
        emailCodeCheck(emailCode,entity);
        return entity;
    }

    @Override
    public Entity verifyWithPhoneCode(String phoneCode) throws CustomException {
        Entity entity = entityUtils.getCurrentUserInfo();
        phoneCodeCheck(phoneCode, entity);
        return entity;
    }

    @Override
    public void verifyWithSign(String sign) throws CustomException {
        Entity entity = entityUtils.getCurrentUserInfo();
        signCheck(sign,entity);
    }


    @Override
    public VerificationCodeReturnDto verifyTransaction(TransactionVerificationDto transactionVerificationDto) throws CustomException {
        String emailCode = transactionVerificationDto.getEmailCode();
        String phoneCode = transactionVerificationDto.getPhoneCode();
        Entity entity = entityUtils.getCurrentUserInfo(USER_NOT_FOUND_EXCEPTION);
        emailCodeCheck(emailCode, entity);
        phoneCodeCheck(phoneCode, entity);
        if(!passwordEncoder.matches(phoneCode, entity.getPhoneConfirmationCode()) || !passwordEncoder.matches(emailCode, entity.getEmailConfirmationCode())) {
            String emailOrPhoneWrong = "";
            if(!passwordEncoder.matches(phoneCode, entity.getPhoneConfirmationCode())) {
                entity.setEmailConfirmationCodeAttempts(entity.getPhoneConfirmationCodeAttempts() + 1);
                emailOrPhoneWrong = emailOrPhoneWrong.concat("Email");
            }
            if(!passwordEncoder.matches(emailCode, entity.getEmailConfirmationCode())) {
                entity.setPhoneConfirmationCodeAttempts(entity.getPhoneConfirmationCodeAttempts() + 1);
                emailOrPhoneWrong = emailOrPhoneWrong.concat("Phone");
            }
            entityUtils.saveEntityInfo(entity);
            switch (emailOrPhoneWrong){
                case "Email":
                    throw new CustomException("VERIFICATIONS-016", "Incorrect email code", 400);
                case "Phone":
                    throw new CustomException("VERIFICATIONS-017", "Incorrect phone code", 400);
                default:
                    throw new CustomException("VERIFICATIONS-018", "Incorrect email and phone code", 400);
            }
        }
        else {

            final String code = RandomStringUtils.randomAlphanumeric(50);
            VerificationCodeReturnDto verificationCodeReturnDto = VerificationCodeReturnDto.builder().verificationCode(code).build();
            entity.setVerifyTransactionCode(passwordEncoder.encode(code));
            entity.setVerifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
            entity.setVerifyTransactionCodeAttempts(0);
            entity.setVerifyWithSign(false);
            entityUtils.saveEntityInfo(entity);
            return verificationCodeReturnDto;
        }

    }

    @Override
    public VerificationCodeReturnDto verifyTransactionWithSign(TransactionVerificationDto transactionVerificationDto) throws CustomException {
        String emailCode = transactionVerificationDto.getEmailCode();
        String phoneCode = transactionVerificationDto.getPhoneCode();
        String sign = transactionVerificationDto.getSign();
        Entity entity = entityUtils.getCurrentUserInfo(USER_NOT_FOUND_EXCEPTION);
        emailCodeCheck(emailCode, entity);
        phoneCodeCheck(phoneCode, entity);
        signCheck(sign,entity);
        if(!passwordEncoder.matches(phoneCode, entity.getPhoneConfirmationCode())
                || !passwordEncoder.matches(emailCode, entity.getEmailConfirmationCode())
                || !passwordEncoder.matches(sign, entity.getSign())) {
            String emailOrPhoneWrong = "";
            if(!passwordEncoder.matches(phoneCode, entity.getPhoneConfirmationCode())) {
                entity.setEmailConfirmationCodeAttempts(entity.getPhoneConfirmationCodeAttempts() + 1);
                emailOrPhoneWrong = emailOrPhoneWrong.concat("Email");
            }
            if(!passwordEncoder.matches(emailCode, entity.getEmailConfirmationCode())) {
                entity.setPhoneConfirmationCodeAttempts(entity.getPhoneConfirmationCodeAttempts() + 1);
                emailOrPhoneWrong = emailOrPhoneWrong.concat("Phone");
            }
            if(!passwordEncoder.matches(sign, entity.getSign())) {
                entity.setSignAttempts(entity.getSignAttempts() + 1);
                emailOrPhoneWrong = emailOrPhoneWrong.concat("Sign");
            }
            entityUtils.saveEntityInfo(entity);
            switch (emailOrPhoneWrong){
                case "Email":
                    throw new CustomException("VERIFICATIONS-016", "Incorrect email code", 400);
                case "Phone":
                    throw new CustomException("VERIFICATIONS-027", "Incorrect phone code", 400);
                case "Sign":
                    throw new CustomException("VERIFICATIONS-019", "Incorrect sign", 400);
                case "EmailPhone":
                    throw new CustomException("VERIFICATIONS-018", "Incorrect email and phone code", 400);
                case "EmailSign":
                    throw new CustomException("VERIFICATIONS-020", "Incorrect email code and sign", 400);
                default:
                    throw new CustomException("VERIFICATIONS-021", "Incorrect phone code and sign", 400);
            }
        }
        else {

            final String code = RandomStringUtils.randomAlphanumeric(50);
            VerificationCodeReturnDto verificationCodeReturnDto = VerificationCodeReturnDto.builder().verificationCode(code).build();
            entity.setVerifyTransactionCode(passwordEncoder.encode(code));
            entity.setVerifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
            entity.setVerifyTransactionCodeAttempts(0);
            entity.setVerifyWithSign(true);
            entityUtils.saveEntityInfo(entity);
            return verificationCodeReturnDto;
        }
    }

    @Override
    public void verifyPasswordRecoveryCode(String code, Entity entity) throws CustomException {
        if (code == null || code.isEmpty())
            throw new CustomException("VERIFICATIONS-031", "Code cannot be blank", 400);
        if(entity.getPasswordChangeCodeExpiration().before(new Date()))
            throw new CustomException("VERIFICATIONS-031", "Code expired", 400);
        if(!passwordEncoder.matches(code, entity.getPasswordChangeCode())) {
            entity.setPasswordChangeCodeAttempts(entity.getPasswordChangeCodeAttempts() + 1);
            entityUtils.saveEntityInfo(entity);
            throw new CustomException("VERIFICATIONS-031", "Wrong code", 400);
        }
    }

    public boolean verifyTransactionCode(String transactionCode, Boolean doesTransactionNeedsToBeSigned) throws CustomException {
        Entity entity = entityUtils.getCurrentUserInfo(USER_NOT_FOUND_EXCEPTION);
        if(!entity.getEmailConfirmed()&& !entity.getPhoneConfirmed())
            throw new CustomException("VERIFICATIONS-030", "You need to confirm your email and password first", 400);
        if(doesTransactionNeedsToBeSigned && !entity.getVerifyWithSign())
            throw new CustomException("VERIFICATIONS-022", "Transaction requires to be signed", 400);
        if(entity.getVerifyTransactionCodeAttempts()>2)
            throw new CustomException("VERIFICATIONS-023", "Transaction attempts limit reached, get new code", 400);
        if(entity.getVerifyTransactionCodeExpiration().before(new Date()))
            throw new CustomException("VERIFICATIONS-024", "Transaction code expired, get a new code", 400);
        if(!passwordEncoder.matches(transactionCode, entity.getVerifyTransactionCode())) {
            entity.setVerifyTransactionCode(entity.getVerifyTransactionCode() + 1);
            entityUtils.saveEntityInfo(entity);
            throw new CustomException("VERIFICATIONS-025", "Transaction code not valid, try again", 400);
        }
        entity.setVerifyTransactionCode(null);
        entity.setVerifyTransactionCodeAttempts(0);
        entity.setVerifyTransactionCodeExpiration(null);
        entity.setVerifyWithSign(false);
        return true;
    }
    void emailCodeCheck(String emailCode, Entity entity) throws CustomException{

        if (emailCode.isEmpty())
            throw new CustomException("VERIFICATIONS-001", "Email code cannot be blank", 400);
        if(entity.getEmailConfirmationCode() == null)
            throw new CustomException("VERIFICATIONS-004", "First, you need to get an email code", 400);
        if(entity.getEmailConfirmationCodeExpiration().before(new Date()))
            throw new CustomException("VERIFICATIONS-003", "Code expired, you need to get a new one", 400);
        if(entity.getEmailConfirmationCodeAttempts() > 2)
            throw new CustomException("VERIFICATIONS-005", "You entered incorrect code 3 times, you have to take new code", 400);
        if(!passwordEncoder.matches(emailCode, entity.getEmailConfirmationCode())) {
            entity.setEmailConfirmationCodeAttempts(entity.getEmailConfirmationCodeAttempts() + 1);
            entityUtils.saveEntityInfo(entity);
            throw new CustomException("VERIFICATIONS-006", "Incorrect code", 400);
        }
    }
    void phoneCodeCheck(String phoneCode, Entity entity) throws CustomException {
        if (phoneCode.isEmpty())
            throw new CustomException("VERIFICATIONS-007", "Phone code cannot be blank", 400);
        if(entity.getPhoneConfirmationCode() == null)
            throw new CustomException("VERIFICATIONS-009", "First, you need to get a phone code", 400);
        if(entity.getPhoneConfirmationCodeExpiration().before(new Date()))
            throw new CustomException("VERIFICATIONS-010", "Phone code expired, you need to get a new one", 400);
        if(entity.getPhoneConfirmationCodeAttempts() > 2)
            throw new CustomException("VERIFICATIONS-011", "You entered incorrect phone code 3 times, you have to take new code", 400);
        if(!passwordEncoder.matches(phoneCode, entity.getPhoneConfirmationCode())) {
            entity.setPhoneConfirmationCodeAttempts(entity.getPhoneConfirmationCodeAttempts() + 1);
            entityUtils.saveEntityInfo(entity);
            throw new CustomException("VERIFICATIONS-012", "Incorrect code", 400);
        }
    }
    void signCheck(String sign, Entity entity) throws CustomException {
        if(sign.isEmpty())
            throw new CustomException("VERIFICATIONS-0012", "Sign cannot be blank", 400);
        if(entity.getSign() == null || entity.getSign().isEmpty())
            throw new CustomException("VERIFICATIONS-0013", "You need to create a new sign first", 400);
        if(!entity.getSignActivated())
            throw new CustomException("VERIFICATIONS-014", "You need to activate your sign first", 400);
        if(entity.getSignAttempts()>2)
            throw new CustomException("VERIFICATIONS-015", "Sign attempts limit reached, create a new sign", 400);
        if(!passwordEncoder.matches(sign, entity.getSign())) {
            entity.setSignAttempts(entity.getSignAttempts() + 1);
            entityUtils.saveEntityInfo(entity);
            throw new CustomException("VERIFICATIONS-006", "Incorrect sign", 400);
        }
    }

}

package com.banco.services;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import com.amazonaws.services.simpleemail.model.*;

import com.amazonaws.util.IOUtils;
import com.banco.dtos.EmailPhoneCodeExpirationReturn;
import com.banco.entities.EmailType;
import com.banco.entities.Entity;
import com.banco.entities.SMSType;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class NotificationServiceImpl implements NotificationService{

    @Value("${aws.ses.mail.source}")
    private String sourceMail;
    @Value("${frontend.endpoint}")
    private String frontendEndpoint;
    @Value("${env}")
    private String environment;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityRepository entityRepository;
    @Autowired
    private AmazonSimpleEmailService amazonSimpleEmailService;
    @Autowired
    private SnsClient snsClient;
    @Autowired
    private AmazonS3 s3Client;


    @Override
    public EmailPhoneCodeExpirationReturn sendEmailVerificationCode() throws CustomException {
        String userTaxId =  SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Entity> userOptional = entityRepository.findByTaxId(userTaxId);
        if(userOptional.isEmpty())
            throw new CustomException("NOTIFICATIONS-001", "User not found", 404);
        Entity user = userOptional.get();
        String code = RandomStringUtils.randomNumeric(6);
        String coded = passwordEncoder.encode(code);
        user.setEmailConfirmationCode(coded);
        user.setEmailConfirmationCodeAttempts(0);
        user.setEmailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("subject", "Verification code");
        sendMail(user, map, EmailType.EMAIL_VERIFICATION);
        user.setNextSendEmail(new Date( System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
        entityRepository.save(user);
        return  EmailPhoneCodeExpirationReturn.builder().expirationDate(user.getNextSendEmail()).build();
    }

    @Override
    public EmailPhoneCodeExpirationReturn sendPhoneVerificationCode() throws CustomException {
        String userTaxId =  SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Entity> userOptional = entityRepository.findByTaxId(userTaxId);
        if(userOptional.isEmpty())
            throw new CustomException("NOTIFICATIONS-002", "User not found", 404);
        Entity user = userOptional.get();
        String code = RandomStringUtils.randomNumeric(6);
        String coded = passwordEncoder.encode(code);
        Map<String, Object> data = new HashMap<>();
        data.put("code", code);
        sendSMS(data, user, SMSType.VERIFY);
        user.setPhoneConfirmationCode(coded);
        user.setPhoneConfirmationCodeAttempts(0);
        user.setPhoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
        entityRepository.save(user);
        return EmailPhoneCodeExpirationReturn.builder().expirationDate(user.getNextSendEmail()).build();
    }

    //TODO
    @Override
    public void sendAccountDataModification() throws CustomException {

    }
    //TODO
    @Override
    public void sendCardCharge() throws CustomException {

    }
    //TODO
    @Override
    public void sendCompletedLoan() throws CustomException {

    }
    //TODO
    @Override
    public void sendCreatedNewBankAccount() throws CustomException {

    }
    //TODO
    @Override
    public void sendCreatedNewCard() throws CustomException {

    }
    //TODO
    @Override
    public void sendCreatedNewLoan() throws CustomException {

    }
    //TODO
    @Override
    public void sendEmailModification() throws CustomException {

    }
    //TODO
    @Override
    public void sendNewLogin(Entity entity, String address) throws CustomException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("user.name", entity.getName());
        data.put("new.ip", address);
        sendMail(entity, data,EmailType.NEW_LOGIN);
    }
    //TODO
    @Override
    public void sendRecalculatedLoan() throws CustomException {

    }
    //TODO
    @Override
    public void sendSignModification() throws CustomException {

    }
    //TODO
    @Override
    public void sendTransactionVerification() throws CustomException {

    }
    //TODO
    @Override
    public void sendTransferReceived() throws CustomException {

    }
    //TODO
    @Override
    public void sendTransferSent() throws CustomException {

    }
    //TODO
    @Override
    public void sendUnpaidLoanSubscription() throws CustomException {

    }
    //TODO
    @Override
    public void sendWelcome() throws CustomException {

    }


    @Override
    public void sendMail(Entity entity, Map<String, Object> keysToReplace, EmailType emailType) throws CustomException {
        if(entity.getNextSendEmail() != null && entity.getNextSendEmail().after(new Date()))
            throw new CustomException("NOTIFICATIONS-003", "You have to wait 10 minutes to send it again", 400);
        try {
            Map<String, Object> template = loadMailTemplate(keysToReplace, emailType);
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(entity.getEmail()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData((String) template.get("template"))))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData((String) template.get("subject"))))
                    .withSource(sourceMail);
            amazonSimpleEmailService.sendEmail(request);
            entityRepository.save(entity);

        } catch (Exception ex) {
            if(environment.equals("local")||environment.equals("dev"))
                throw new CustomException("NOTIFICATIONS-005", ex.getMessage(), 500);
            throw new CustomException("NOTIFICATIONS-005", "Email could not be sent", 500);
        }
    }

    @Override
    public void sendSMS(Map<String, Object> keysToReplace, Entity entity, SMSType smsType) throws CustomException {
        if(entity.getNextSendPhone() != null && entity.getNextSendPhone().after(new Date()))
            throw new CustomException("NOTIFICATIONS-006", "You have to wait 10 minutes to send it again", 400);
        Map<String, Object> template = loadSMSTemplate(keysToReplace, smsType);
        String message = (String) template.get("text");
        String phoneNumber = entity.getPhoneNumber();

        pubTextSMS(snsClient, message, phoneNumber);
        entity.setNextSendPhone(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)));
        entityRepository.save(entity);
    }
    private Map<String,Object> loadMailTemplate(Map<String, Object> data, EmailType emailType) throws IOException {
        String template;
        Map<String, Object> returnTemplate = new HashMap<>();
        switch (emailType){
            case ACCOUNT_DATA_MODIFICATION -> {
                template = "templates/account_data_modification.html";
                returnTemplate.put("subject", "Account data modified");
            }
            case CARD_CHARGE -> {
                template = "templates/card_charge.html";
                returnTemplate.put("subject", "New charge in one of your cards");
            }
            case CARD_SENT -> {
                template = "templates/card_sent.html";
                returnTemplate.put("subject", "Your card was sent");
            }
            case COMPLETED_LOAN -> {
                template = "templates/completed_loan.html";
                returnTemplate.put("subject", "Your loan was completed and closed");
            }
            case CREATED_NEW_BANK_ACCOUNT -> {
                template = "templates/created_new_bank_account.html";
                returnTemplate.put("subject", "New bank account created");
            }
            case CREATED_NEW_CARD ->{
                template = "templates/created_new_card.html";
                returnTemplate.put("subject", "New card was created");
            }
            case CREATED_NEW_LOAN -> {
                template = "templates/created_new_loan.html";
                returnTemplate.put("subject", "New loan was created");
            }
            case EMAIL_VERIFICATION -> {
                template = "templates/email_verification.html";
                returnTemplate.put("subject", "Email verification");
            }
            case EMAIL_MODIFICATION -> {
                template = "templates/email_modification.html";
                returnTemplate.put("subject", "Enter this code to modify your current email");
            }
            case NEW_LOGIN -> {
                template = "templates/new_login.html";
                returnTemplate.put("subject", "New login detected");
            }
            case PHONE_MODIFICATION -> {
                template = "templates/phone_modification.html";
                returnTemplate.put("subject", "Your phone was modified");
            }
            case RECALCULATED_LOAN -> {
                template = "templates/recalculated_loan.html";
                returnTemplate.put("subject", "Your loan has been recalculated");
            }
            case SIGN_MODIFICATION -> {
                template = "templates/sign_modification.html";
                returnTemplate.put("subject", "Enter this code to modify sign");
            }
            case TRANSACTION_VERIFICATION -> {
                template = "templates/transaction_verification.html";
                returnTemplate.put("subject", "Enter this code to verify transaction");
            }
            case TRANSFER_RECEIVED -> {
                template = "templates/transfer_received.html";
                returnTemplate.put("subject", "Transfer was received");
            }
            case TRANSFER_SENT -> {
                template = "templates/transfer_sent.html";
                returnTemplate.put("subject", "Transfer was sent from your account");
            }
            case UNPAID_LOAN_SUBSCRIPTION -> {
                template = "templates/unpaid_loan_subscription.html";
                returnTemplate.put("subject", "A loan monthly subscription was not successfully paid");
            }
            default -> {
                template = "templates/welcome.html";
                returnTemplate.put("subject", "Welcome to Banco De la Peseta");
            }
        }
        InputStream htmlTemplate = new ByteArrayInputStream(downloadFile(template));
        String content = IOUtils.toString(htmlTemplate);

        for(String key: data.keySet()){
            Object value = data.get(key);
            String valueString;
            try{
                valueString = (String) value;
            }catch (Exception e){
                continue;
            }
            content = content.replace("{{" + key + "}}", valueString );
        }
        returnTemplate.put("template", content);
        return returnTemplate;
    }
    private Map<String, Object> loadSMSTemplate(Map<String, Object> data, SMSType smsType){
        Map<String, Object> map = new HashMap<>();
        if (Objects.requireNonNull(smsType) == SMSType.APPROVE) {
            map.put("text", "Here is your verification code: " + data.get("code"));
        } else {
            map.put("text", "To verify your phone number, enter the following code: " + data.get("code"));
        }
        return map;
    }

    private void pubTextSMS(SnsClient snsClient, String message, String phoneNumber) throws CustomException {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .phoneNumber(phoneNumber)
                    .build();

            PublishResponse result = snsClient.publish(request);
            if(!result.sdkHttpResponse().isSuccessful()){
                throw new CustomException("NOTIFICATIONS-007", "Message not sent", 500);
            }

    }
    
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

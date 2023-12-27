package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dto.EReceiptDataDto;
import com.cba.core.wirememobile.dto.EmailRequestDto;
import com.cba.core.wirememobile.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
//    @Qualifier("asyncExecutor")
//    private final TaskExecutor taskExecutor;

    @Override
    @Async("asyncExecutor")
    public void sendEmail(EmailRequestDto emailRequestDto) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.addAttachment("template-cover.png", new ClassPathResource("static/javabydeveloper-email.PNG"));

        Context context = new Context();
        context.setVariables(emailRequestDto.getProps());

        final String template = emailRequestDto.getProps().get("type").equals("NEWSLETTER") ? "newsletter-template" : "inlined-css-template";
        String html = templateEngine.process(template, context);

        helper.setTo(emailRequestDto.getMailTo());
        helper.setText(html, true);
        helper.setSubject(emailRequestDto.getSubject());
//        helper.setFrom(emailRequestDto.getFrom());

        javaMailSender.send(message);
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmail(String userMail, String messageBody) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(userMail);
        helper.setText(messageBody);
        helper.setSubject("This is auto generated email - include credentials/OTP");
//        helper.setFrom(emailRequestDto.getFrom());

        javaMailSender.send(message);
    }

    @Override
    @Async("asyncExecutor")
    public void sendEmail(String userMail, EReceiptDataDto data) throws Exception {


        Thread.sleep(5000);
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setTo(userMail);
        helper.setText("A " + data.getPaymentMode() + " " + data.getTranType() + " tx on your " + data.getCardLabel() +
                " card " + data.getPan() + " for " + data.getCurrency() + data.getAmount() + " on " + data.getDateTime() +
                " at '12:18:58 PM', bearing Inv-No " + data.getInvoiceNo() + ", Auth-Code " + data.getAuthCode() + " is approved.\n");

        helper.setSubject("This is auto generated email - include e-Receipt");
//        helper.setFrom(emailRequestDto.getFrom());

        javaMailSender.send(message);
        System.out.println("Copy of e-Receipt mail has been sent successfully.!!!");
    }
}

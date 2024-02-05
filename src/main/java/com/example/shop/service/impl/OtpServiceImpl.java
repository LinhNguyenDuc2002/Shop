package com.example.shop.service.impl;

import com.example.shop.cache.TempUser;
import com.example.shop.config.MailConfig;
import com.example.shop.constant.ResponseMessage;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.OtpService;
import com.example.shop.util.DateUtil;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {
    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void verifyOTP(String otp, HttpSession session) {

    }

    @Override
    public void sendOTP(String email, String username, String otp) {
        try {
            Message message = new MimeMessage(authenEmail());
            message.setFrom(new InternetAddress(mailConfig.getEmail(), "OTP authentication project"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Please verify your account");
            message.setText(formatMessage(username, otp));

            Transport.send(message);

            log.info("Sending otp code is successful");
        } catch (MessagingException e) {
            log.error("Sending otp code is failed");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            log.error("Sending otp code is failed");
            throw new RuntimeException(e);
        }
    }

    private String formatMessage(String username, String otp) {
        String message = "Dear " + username + ",\n" +
                "\n" +
                "Thank you for choosing our services. To complete your account verification, please enter the one-time password (OTP) provided below:\n" +
                "\n" +
                "OTP: " + otp + "\n" +
                "\n" +
                "This OTP is valid for a limited time, and it should be used within 1 minutes. If you did not request this OTP or need any assistance, please contact our support team at linhnd.b20at109@stu.ptit.edu.vn.\n" +
                "\n" +
                "Please ensure that you do not share this OTP with anyone for security reasons. It is used for the sole purpose of verifying your account.\n" +
                "\n" +
                "Thank you for choosing us. We look forward to serving you.\n" +
                "\n" +
                "Best regards,\n";

        return message;
    }

    private Session authenEmail() {
        Session session = Session.getInstance(
                mailConfig.getProperties(),
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfig.getEmail(), mailConfig.getPassword());
                    }
                });
        return session;
    }

}

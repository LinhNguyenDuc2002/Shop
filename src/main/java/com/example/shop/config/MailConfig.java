package com.example.shop.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Setter
@Getter
public class MailConfig {
    @Value("${outlook.email}")
    private String email;

    @Value("${outlook.password}")
    private String password;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

    @Value("${mail.smtp.auth}")
    private String smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String smtpStartTLSEnable;

    private Properties properties;

    @PostConstruct
    public void initProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.starttls.enable", smtpStartTLSEnable); //TLS

        this.properties = prop;
    }
}

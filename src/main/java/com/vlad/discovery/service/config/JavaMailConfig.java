package com.vlad.discovery.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.*;
import java.util.Properties;

@Configuration
public class JavaMailConfig {
    @Value("${smtpip.email.host}")
    private String smtpip;
    @Value("${smtp.mail.port}")
    private String smtpPort;
    @Value("${smtp.STARTTLS.enable:false}")
    private boolean setTls;
    @Value("${mail.smtp.user}")
    private  String mailuser;
    @Value("${mail.smtp.password}")
    private String mailpassword;
    @Value("${use.smtp.port:false}")
    private boolean setPort;
    @Value("${email.authentication.enabled}")
    private boolean emailAuthEnable;
    @Value("${email.session.debug.enabled:false}")
    private boolean mailSessionDebug;


    @Bean("vladTransport")
    public Transport transport() throws NoSuchProviderException {
        return session().getTransport();
    }

    @Bean("vladSession")
    public Session session(){
        Session mailSession = Session.getInstance(getProps(), getAuthentication());
        mailSession.setDebug(mailSessionDebug);
        return mailSession;
    }

    private Properties getProps() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", smtpip);
        props.setProperty("mail.smtp.auth", String.valueOf(emailAuthEnable));
        props.setProperty("mail.smtp.starttls.enable", String.valueOf(setTls));
        if(setPort){
            props.setProperty("mail.smtp.port",smtpPort);
        }
        if(emailAuthEnable){
            props.setProperty("mail.user", mailuser);
            props.setProperty("mail.password", mailpassword);
        }
        return props;
    }

    private Authenticator getAuthentication() {
        if(emailAuthEnable){
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailuser,mailpassword);
                }
            };
        }
        return null;
    }
}

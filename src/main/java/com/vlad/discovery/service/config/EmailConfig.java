package com.vlad.discovery.service.config;

import com.vlad.discovery.service.model.EmailNotification;
import com.vlad.discovery.service.model.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class EmailConfig {
    @Value("${smtpip.email.host}")
    private String smtpip;
    @Value("${smtp.mail.port}")
    private String smtpPort;
    @Value("${smtp.STARTTLS.enable:false}")
    private boolean setTls;

    @Value("${mail.user}")
    private  String mailuser;
    @Value("${mail.password}")
    private String mailpassword;

    @Value("${use.smtp.port:false}")
    private boolean setPort;


    @Value("${email.authentication.enabled}")
    private boolean emailAuthEnable;

    private static final String RESPONSE_SUCCESS="EMAIL SUCCESSFULLY PROCESSED";
    private static final String RESPONSE_FAILED="EMAIL FAILED";


    public EmailResponse sendEmail(EmailNotification email){
            try{
                Properties props = new Properties();
                Session mailSession = Session.getDefaultInstance(props, null);
                MimeMessage message = new MimeMessage(mailSession);
                message.setSubject(email.getSubject());

                message.setFrom(new InternetAddress(email.getFrom(),email.getEmailName()));

                props.setProperty("mail.transport.protocol", "smtp");
                props.setProperty("mail.host", smtpip);
                props.setProperty("mail.smtp.auth", String.valueOf(emailAuthEnable));
                props.setProperty("mail.smtp.starttls.enable", String.valueOf(setTls));
                if(setPort){
                    props.setProperty("mail.port",smtpPort);
                }
                if(emailAuthEnable) {
                    props.setProperty("mail.user", mailuser);
                    props.setProperty("mail.smtp.user", mailuser);
                    props.setProperty("mail.password", mailpassword);
                    props.setProperty("mail.smtp.password", mailpassword);
                }
                Transport transport = mailSession.getTransport();
                mailSession.setDebug(true);

                BodyPart messageBodyPart = new MimeBodyPart();

                MimeMultipart multipart = new MimeMultipart("related");
                transport.connect();

                messageBodyPart.setContent(email.getMailBody(), "text/html");
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);


                List<String> recipientList = email.getReceivers();
                InternetAddress[] recipientAddress = new InternetAddress[recipientList.size()];
                int counter = 0;
                for (String receiver : recipientList) {
                    recipientAddress[counter] = new InternetAddress(receiver.trim());
                    counter++;
               }
                message.addRecipients(Message.RecipientType.TO, recipientAddress);

                transport.send(message);

                return EmailResponse.builder().response("00").message(RESPONSE_SUCCESS).build();

        }catch(Exception ex) {
            log.error("Email sending failed for service {}",email.getSubject());
            return  EmailResponse.builder().response("99").message(RESPONSE_FAILED).build();
        }

    }
}

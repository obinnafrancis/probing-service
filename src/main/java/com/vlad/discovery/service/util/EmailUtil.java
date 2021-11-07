package com.vlad.discovery.service.util;

import com.vlad.discovery.service.model.EmailNotification;
import com.vlad.discovery.service.model.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Slf4j
@Component
public class EmailUtil {
    private static final String RESPONSE_SUCCESS="EMAIL SUCCESSFULLY PROCESSED";
    private static final String RESPONSE_FAILED="EMAIL FAILED";

    @Autowired
    @Qualifier("vladTransport")
    private Transport transport;

    @Autowired
    @Qualifier("vladSession")
    private Session session;

    public EmailResponse sendEmail(EmailNotification email){
        try {
            MimeMessage message = new MimeMessage(session);
            message.setSubject(email.getSubject());
            message.setFrom(new InternetAddress(email.getFrom(),session.getProperties().getProperty("mail.smtp.user")));
            List<String> recipientList = email.getReceivers();
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.size()];
            int counter = 0;
            for (String receiver : recipientList) {
                recipientAddress[counter] = new InternetAddress(receiver.trim());
                counter++;
            }
            message.addRecipients(Message.RecipientType.TO, recipientAddress);
            BodyPart messageBodyPart = new MimeBodyPart();
            MimeMultipart multipart = new MimeMultipart("related");
            messageBodyPart.setContent(email.getMailBody(), "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            transport.connect();
            transport.send(message);
            transport.close();
            return EmailResponse.builder().response("00").message(RESPONSE_SUCCESS).build();
        }catch (Exception e){
            log.error("Email sending failed for service {}",email.getSubject());
            return  EmailResponse.builder().response("99").message(RESPONSE_FAILED).build();
        }
    }
}

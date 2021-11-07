package com.vlad.discovery.service.service.impl;

import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.model.EmailNotification;
import com.vlad.discovery.service.model.EmailResponse;
import com.vlad.discovery.service.service.Notifications;
import com.vlad.discovery.service.util.EmailUtil;
import com.vlad.discovery.service.util.TemplateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailNotificationImpl implements Notifications<EmailNotification> {
    @Autowired
    private Configuration freemarkerConfig;
    Map<String, String> detail=new HashMap();
    @Value("${default.mail.sender}")
    private String fromEmail;
    @Value("${service.down.image.uri}")
    private String downIcon;
    @Value("${service.up.image.uri}")
    private String upIcon;


    @Autowired
    EmailUtil emailUtil;

    @Override
    public EmailResponse send(EmailNotification emailNotification) {
           return  emailUtil.sendEmail(emailNotification);
      }

    @Override
    public void sendAsync(EmailNotification emailNotification) {
        emailUtil.sendEmail(emailNotification);
    }


    @Override
    public EmailNotification generateNotification(ServiceInformation service) {
        //Set fields that will be displayed in the mail body
//        detail.put("serviceId",service.getServiceId());
        detail.put("service",service.getServiceName());
        detail.put("status",service.isDown()?"down":"up");
        detail.put("icon", service.isDown()?downIcon:upIcon);
        detail.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        String mailBody=getTemplate(detail);
        List<String> beneficiaries = Arrays.stream(service.getStakeholders().split(",")).collect(Collectors.toList());
      return EmailNotification.builder()
              .from(fromEmail)
              .mailBody(mailBody)
              .emailName("SERVICE NOTIFICATION")
              .subject(service.getServiceName()+" STATUS NOTIFICATION")
              .receivers(beneficiaries)
              .build();
    }

    public  String getTemplate(Map data){

        Template t = null;
        try {
            t = freemarkerConfig.getTemplate(TemplateUtil.DEFAULT_EMAIL_TEMPLATE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
           // log.("{}", e);
        }
        String defaultEmailBody = "";
        try {
            defaultEmailBody = FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
        } catch (Exception e) {
          //  log.error(" Error while generating template {}", e);
        }

        return defaultEmailBody;
    }
    public EmailResponse pushEmail(EmailNotification notice){
       EmailNotification emailnotice= generateNotification(notice.getServiceInformation());
       return send(emailnotice);

    }

    public void pushEmailAsync(EmailNotification notice) {
        EmailNotification emailnotice = generateNotification(notice.getServiceInformation());
        sendAsync(emailnotice);
    }
}

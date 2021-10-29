package com.vlad.discovery.service.service.impl;

import com.vlad.discovery.service.config.EmailConfig;
import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.model.EmailNotification;
import com.vlad.discovery.service.model.EmailResponse;
import com.vlad.discovery.service.service.Notifications;
import freemarker.template.Configuration;
import freemarker.template.Template;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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

    @Autowired
    EmailConfig emailConfig;

    @Override
    public EmailResponse send(EmailNotification emailNotification) {
           return  emailConfig.sendEmail(emailNotification);
      }

    @Override
    public void sendAsync(EmailNotification emailNotification) {
        emailConfig.sendEmail(emailNotification);
    }


    @Override
    public EmailNotification generateNotification(ServiceInformation service) {
        //Set fields that will be displayed in the mail body
        detail.put("serviceId",service.getServiceId());
        detail.put("serviceName",service.getServiceName());
        detail.put("statusExpectedValue",service.getStatusExpectedValue());
        String mailBody=getTemplate(detail);
        List<String> beneficiaries = Arrays.stream(service.getStakeholders().split(",")).collect(Collectors.toList());
      return EmailNotification.builder()
              .mailBody(mailBody)
              .emailName("SERVICE NOTIFICATION")
              .subject(service.getServiceName()+" STATUS NOTIFICATION")
              .receivers(beneficiaries)
              .build();
    }

    public  String getTemplate(Map data){

        Template t = null;
        try {
            t = freemarkerConfig.getTemplate("service-notification.ftl");
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

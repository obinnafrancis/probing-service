package com.vlad.discovery.service;

import com.google.gson.Gson;
import com.vlad.discovery.service.model.EmailNotification;
import com.vlad.discovery.service.model.EmailResponse;
import com.vlad.discovery.service.service.impl.EmailNotificationImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-vlad.properties"})
public class EmailServiceTest {

    @Autowired
    EmailNotificationImpl emailNotification;
    @Value("${service.down.image.uri}")
    private String downIcon;
    @Value("${service.up.image.uri}")
    private String upIcon;

    public String generateUpTemplate(){
        Map<String,String> details = new HashMap<>();
        details.put("service","Finch Platform Service");
        details.put("status","up");
        details.put("icon", upIcon);
        details.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        String upHtml = emailNotification.getTemplate(details);
        log.info(upHtml);
        return upHtml;
    }

    public String generateDownTemplate(){
        Map<String,String> details = new HashMap<>();
        details.put("service","Finch Platform Service");
        details.put("status","down");
        details.put("icon", downIcon);
        details.put("date", LocalDateTime.now().toString());
        String downHtml = emailNotification.getTemplate(details);
        log.info(downHtml);
        return downHtml;
    }

    @Test
    public void testSendUpTemplate(){
        EmailNotification email = EmailNotification.builder()
                .emailName("Notification")
                .subject("Test Uptime")
                .mailBody(generateUpTemplate())
                .from("no-reply@mail.com")
//                .receivers(Collections.singletonList("ernefrancis@gmail.com"))
                .receivers(Stream.of("nasaconcepts@yahoo.com","ernefrancis@gmail.com").collect(Collectors.toList()))
                .build();
        EmailResponse emailResponse = emailNotification.send(email);
        log.info(new Gson().toJson(emailResponse));
        Assert.assertEquals("00",emailResponse.getResponse());
    }

    @Test
    public void testSendDownTemplate(){
        EmailNotification email = EmailNotification.builder()
                .emailName("Notification")
                .subject("Test Uptime")
                .mailBody(generateDownTemplate())
                .from("no-reply@mail.com")
                .receivers(Stream.of("nasaconcepts@yahoo.com","ernefrancis@gmail.com").collect(Collectors.toList()))
//                .receivers(Collections.singletonList("ernefrancis@gmail.com"))
                .build();
        EmailResponse emailResponse = emailNotification.send(email);
        log.info(new Gson().toJson(emailResponse));
        Assert.assertEquals("00",emailResponse.getResponse());
    }
}

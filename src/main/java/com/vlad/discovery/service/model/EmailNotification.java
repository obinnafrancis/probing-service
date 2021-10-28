package com.vlad.discovery.service.model;

import com.vlad.discovery.service.dto.ServiceInformation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmailNotification {
 private String from;
 private String subject;
 private String emailName;
 private ServiceInformation serviceInformation;
 private List<String> receivers;
 private  String mailBody;

}

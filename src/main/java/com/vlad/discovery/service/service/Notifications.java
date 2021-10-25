package com.vlad.discovery.service.service;

import com.vlad.discovery.service.dto.ServiceInformation;
import com.vlad.discovery.service.model.EmailResponse;

public interface Notifications<T> {
    public EmailResponse send (T t);
    public void sendAsync (T t);
    public T generateNotification(ServiceInformation service);
}

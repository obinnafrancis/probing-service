package com.vlad.discovery.service.util;

import java.util.Base64;
import java.util.UUID;

public class Utils {
    public static boolean isNullorEmpty(String str){
        return str==null || str.strip()=="";
    }

    public static String generateUniqueClientId() {
        return "CLI-"+ UUID.randomUUID();
    }

    public static String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }


}

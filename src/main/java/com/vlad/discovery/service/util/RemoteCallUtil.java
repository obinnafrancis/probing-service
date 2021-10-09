package com.vlad.discovery.service.util;

import com.vlad.discovery.service.model.RemoteResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RemoteCallUtil {
    @Autowired
    @Qualifier("baseClient")
    OkHttpClient okHttpClient;

    public RemoteResponse getCall(String url, Map headers){
        HttpUrl route = HttpUrl.parse(url);
        Request request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(route)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        Response response;
        try{
            response = call.execute();
            return RemoteResponse.builder()
                    .code(response.code())
                    .responseBody(response.body().string())
                    .build();
        }catch (Exception e){
            log.error("Failed to make call {}",e);
            return null;
        }
    }
}

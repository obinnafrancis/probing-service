package com.vlad.discovery.service.config;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkhttpConfig {
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private String proxyPort;
    @Value("${read.timeout:10000}")
    private long readTimeout;
    @Value("${write.timeout:10000}")
    private long writeTimeout;
    @Value("${connection.timeout:10000}")
    private long connectionTimeout;
    @Value("${use.proxy:false}")
    private boolean useProxy;

    @Bean("baseClient")
    public OkHttpClient okHttpClient(){
        OkHttpClient.Builder builder = initDefaultClient();
        if(useProxy){
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
        }
        return builder.build();
    }

    private OkHttpClient.Builder initDefaultClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionSpec spec
                = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledCipherSuites()
                .build();
        builder
                .connectionSpecs(Arrays.asList(spec, ConnectionSpec.CLEARTEXT))
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectionTimeout,TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout,TimeUnit.MILLISECONDS);
        return builder;
    }
}

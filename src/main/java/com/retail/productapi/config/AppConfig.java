package com.retail.productapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * Configuration class that holds configs for the application.
 */
@Configuration
@EnableRetry
public class AppConfig {

    @Value("${external.api.conn.timeout}")
    private int externalAPIConnTimeout;

    @Value("${external.api.read.timeout}")
    private int externalAPIReadTimeout;

    @Bean
    RestTemplate appRestTemplate(){

        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.setConnectTimeout(Duration.ofSeconds(externalAPIConnTimeout));
        builder.setReadTimeout(Duration.ofSeconds(externalAPIReadTimeout));

        return builder.build();
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("ExternalApiServiceThread-");
        executor.initialize();
        return executor;
    }
}
